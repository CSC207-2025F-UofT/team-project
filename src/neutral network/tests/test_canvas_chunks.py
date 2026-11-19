"""训练分子批：补边后的画布有上限，子批梯度累积与整批一起算等价。"""
import random
import sys
import unittest
from pathlib import Path
from unittest.mock import patch

import torch

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import train
from formula_layout import MAX_TOKENS, STRIDE
from model import vital_mini


def cells(chunk, sizes):
    return (max(sizes[i][0] for i in chunk) // STRIDE) * (max(sizes[i][1] for i in chunk) // STRIDE)


class CanvasChunkTests(unittest.TestCase):
    def test_tall_and_wide_images_are_not_padded_together(self):
        sizes = [(512, 640)] * 16 + [(128, 2560)] * 16          # 各自恰好 MAX_TOKENS 格
        chunks = train.canvas_chunks(sizes)
        self.assertEqual(sorted(i for chunk in chunks for i in chunk), list(range(32)))
        for chunk in chunks:
            self.assertEqual(len({sizes[i] for i in chunk}), 1)
            self.assertLessEqual(cells(chunk, sizes), MAX_TOKENS)

    def test_random_batches_stay_within_budgets(self):
        rng = random.Random(0)
        for _ in range(200):
            sizes = []
            for _ in range(rng.randint(1, 64)):
                h = rng.choice(range(128, 513, 16))
                w = rng.choice(range(64, MAX_TOKENS * STRIDE * STRIDE // h + 1, 16))
                sizes.append((h, w))
            chunks = train.canvas_chunks(sizes)
            self.assertEqual(sorted(i for chunk in chunks for i in chunk), list(range(len(sizes))))
            for chunk in chunks:
                c = cells(chunk, sizes)
                if len(chunk) > 1:
                    self.assertLessEqual(len(chunk) * c, train.CHUNK_CELL_BUDGET)
                    self.assertLessEqual(len(chunk) * c * c, train.CHUNK_ATTENTION_BUDGET)

    def test_small_uniform_batch_stays_whole(self):
        self.assertEqual(train.canvas_chunks([(128, 256)] * 32), [list(range(32))])

    def test_chunked_step_matches_whole_batch_gradient(self):
        # float64：CNN 前几层的梯度很小，float32 的累加顺序差异就足以让相对误差失真
        previous = torch.get_default_dtype()
        torch.set_default_dtype(torch.float64)
        self.addCleanup(torch.set_default_dtype, previous)
        torch.manual_seed(0)
        model = vital_mini().eval()                           # 关掉 dropout，BatchNorm 用滑动统计，结果可比
        samples = [(torch.rand(3, 128, 96), torch.randint(0, 200, (n,)).tolist()) for n in (3, 7, 5, 9)]

        def gradients(chunks):
            optimizer = torch.optim.SGD(model.parameters(), lr=0.0)   # lr=0：只取梯度，不改权重
            loss = train.train_step(model, chunks, optimizer, "cpu", False)
            grads = [p.grad.clone() for p in model.parameters() if p.grad is not None]
            return loss, grads

        whole_loss, whole = gradients([train.collate(samples)])
        with patch.object(train, "CHUNK_ATTENTION_BUDGET", 1), patch.object(train, "CHUNK_CELL_BUDGET", 1):
            chunks = train.collate_chunks(samples)
        self.assertEqual(len(chunks), 4)
        chunked_loss, chunked = gradients(chunks)
        self.assertAlmostEqual(whole_loss, chunked_loss, places=5)
        for a, b in zip(whole, chunked):
            # run_batch 把 logits 转成 float32，留下约 1e-6 的相对舍入；k_proj 偏置的真实梯度为 0
            torch.testing.assert_close(a, b, rtol=1e-5, atol=1e-9)


if __name__ == "__main__":
    unittest.main()
