"""KV 缓存的逐 token 解码（model.incremental_decoder）必须与整条序列重算的 decode 一致。"""
import sys
import tempfile
import unittest
from pathlib import Path

import torch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from export_torchscript_ops import build_scripted
from model import bos_id, incremental_decoder, parse_tokens, vital_mini


def full_recompute_generate(model, image, beam_width, max_steps):
    """改用 KV 缓存之前的 generate：每步把整条前缀重新送进 decode。"""
    memory = model.encode_image(image)
    tokens = torch.full((1, 1), bos_id, dtype=torch.long)
    state = None
    while True:
        logits = model.decode(tokens, memory.expand(tokens.size(0), -1, -1))[:, -1]
        state = parse_tokens(logits, state, beam_width, max_steps)
        if state.done:
            return state.best()
        tokens = state.tokens


class IncrementalDecoderTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        torch.set_num_threads(2)
        torch.manual_seed(20261117)
        cls.model = vital_mini().eval()
        cls.stepper = incremental_decoder(cls.model).eval()

    @torch.no_grad()
    def test_steps_with_beam_reordering_match_full_decode(self):
        memory = self.model.encode_image(torch.rand(1, 3, 128, 96))
        mask = torch.zeros(1, memory.size(1), dtype=torch.bool)
        mask[:, -3:] = True
        for memory_mask in (None, mask):
            with self.subTest(masked=memory_mask is not None):
                cross, cache = self.stepper.start(memory)
                tokens = torch.full((1, 1), bos_id, dtype=torch.long)
                for step in range(12):
                    logits, cache = self.stepper(tokens[:, -1:], cache, cross, memory_mask)
                    expected = self.model.decode(tokens, memory.expand(tokens.size(0), -1, -1),
                                                 None, memory_mask)[:, -1]
                    torch.testing.assert_close(logits, expected, rtol=1e-4, atol=1e-4)
                    # 模拟 beam search：3 条假设，来源可以重复、可以换序
                    parents = torch.randint(0, tokens.size(0), (3,))
                    cache = cache.index_select(2, parents)
                    tokens = torch.cat((tokens[parents], torch.randint(0, 200, (3, 1))), dim=1)

    @torch.no_grad()
    def test_several_new_tokens_at_once_stay_causal(self):
        memory = self.model.encode_image(torch.rand(2, 3, 128, 64))
        tokens = torch.randint(0, 200, (2, 9))
        tokens[:, 0] = bos_id
        expected = self.model.decode(tokens, memory)
        cross, cache = self.stepper.start(memory)
        logits, cache = self.stepper(tokens[:, :5], cache, cross)
        torch.testing.assert_close(logits, expected[:, 4], rtol=1e-4, atol=1e-4)
        logits, cache = self.stepper(tokens[:, 5:], cache, cross)
        torch.testing.assert_close(logits, expected[:, -1], rtol=1e-4, atol=1e-4)
        self.assertEqual(cache.shape[4], 9)

    @torch.no_grad()
    def test_rejects_positions_beyond_max_len(self):
        memory = self.model.encode_image(torch.rand(1, 3, 128, 32))
        cross, cache = self.stepper.start(memory)
        too_long = torch.zeros(1, self.stepper.token_pos.size(1) + 1, dtype=torch.long)
        with self.assertRaises(ValueError):
            self.stepper(too_long, cache, cross)

    @torch.no_grad()
    def test_generate_matches_full_recompute_beam_search(self):
        for width in (1, 3):
            for seed in range(3):
                with self.subTest(beam_width=width, seed=seed):
                    torch.manual_seed(seed)
                    image = torch.rand(1, 3, 128, 64 + 32 * seed)
                    expected = full_recompute_generate(self.model, image, width, 24)
                    self.assertEqual(self.model.generate(image, beam_width=width, max_steps=24)[0], expected)

    @torch.no_grad()
    def test_exported_steps_match_native(self):
        with tempfile.TemporaryDirectory() as temporary:
            path = Path(temporary) / 'ops.pt'
            torch.jit.save(build_scripted(self.model), str(path))
            exported = torch.jit.load(str(path)).eval()
        memory = self.model.encode_image(torch.rand(1, 3, 128, 80))
        mask = torch.zeros(1, memory.size(1), dtype=torch.bool)
        mask[:, -2:] = True
        cross, cache = exported.decoder_start(memory)
        native_cross, native_cache = self.stepper.start(memory)
        torch.testing.assert_close(cross, native_cross)
        tokens = torch.full((1, 1), bos_id, dtype=torch.long)
        parents = torch.zeros(1, dtype=torch.long)
        for memory_mask, native_mask in ((torch.empty(0, dtype=torch.bool), None), (mask, mask)):
            exported_cache, reference_cache, current, rows = cache, native_cache, tokens, parents
            for _ in range(5):
                logits, exported_cache = exported.decoder_step(current[:, -1:], rows, exported_cache, cross,
                                                               memory_mask)
                expected, reference_cache = self.stepper(current[:, -1:], reference_cache.index_select(2, rows),
                                                         native_cross, native_mask)
                torch.testing.assert_close(logits, expected, rtol=1e-5, atol=1e-5)
                rows = torch.tensor([0, 0, 0][:min(3, current.size(0) + 2)])
                current = torch.cat((current[rows], torch.randint(0, 200, (rows.numel(), 1))), dim=1)


if __name__ == '__main__':
    unittest.main()
