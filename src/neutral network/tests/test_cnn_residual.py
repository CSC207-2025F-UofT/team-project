import sys
import unittest
from pathlib import Path

import torch
import torch.nn.functional as F

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from model import load_training_weights, vital_mini


def cnn_without_residual(cnn, x):
    """加入残差块之前的 vital_pixel.forward。"""
    for conv, norm in ((cnn.conv, cnn.norm), (cnn.conv2, cnn.norm2), (cnn.conv3, cnn.norm3)):
        x = F.max_pool2d(F.silu(norm(conv(x))), kernel_size=2, stride=2)
    x = F.silu(cnn.norm4(cnn.con4(x)))
    return cnn.position_embedding(cnn.norm5(cnn.con5(x)))


class CnnResidualTests(unittest.TestCase):
    def setUp(self):
        torch.manual_seed(0)
        self.image = torch.rand(2, 3, 128, 96)

    def test_zero_initialised_block_keeps_previous_output(self):
        cnn = vital_mini().cnn
        for training in (True, False):
            cnn.train(training)
            with self.subTest(training=training), torch.no_grad():
                torch.testing.assert_close(cnn(self.image), cnn_without_residual(cnn, self.image))

    def test_old_checkpoint_loads_and_residual_branch_can_learn(self):
        old = vital_mini().eval()
        state = {k: v for k, v in old.state_dict().items() if not k.startswith("cnn.res_")}
        model = vital_mini()
        load_training_weights(model, state)
        model.eval()
        with torch.no_grad():
            torch.testing.assert_close(model.cnn(self.image), old.cnn(self.image))
        model.train()
        model.cnn(self.image).square().mean().backward()
        self.assertGreater(model.cnn.res_norm2.weight.grad.abs().sum().item(), 0)

    def test_incomplete_residual_weights_are_rejected(self):
        state = vital_mini().state_dict()
        del state["cnn.res_conv2.weight"]
        with self.assertRaises(ValueError):
            load_training_weights(vital_mini(), state)


if __name__ == "__main__":
    unittest.main()
