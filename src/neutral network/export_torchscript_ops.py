"""导出 vital_mini 推理要用的 TorchScript 方法；不导出 generate（beam search 在 Java 里）。

- final_norm、head、判断分支的基础层：每层一个方法；
- CNN（含残差块、池化和二维位置编码）、两个视觉 encoder、六个 decoder：直接 script model.py 里的原模块，
  不在这里重写；
- decoder_start / decoder_step：model.incremental_decoder 的逐 token 解码（KV 缓存）。

运行：.venv/bin/python 'src/neutral network/export_torchscript_ops.py'
生成 checkpoints/model_ops.pt 和 model_ops.json（方法清单）。
"""

import argparse
import hashlib
import importlib.util
import json
from pathlib import Path
import sys
import tempfile
from typing import Optional, Tuple

import torch
from torch import nn
import torch.nn.functional as F

from model import incremental_decoder, vital_mini


ROOT = Path(__file__).resolve().parents[2]
LAYER_TYPES = (nn.Conv2d, nn.BatchNorm2d, nn.Linear, nn.LayerNorm)
DECODER_PREFIXES = ('decoder1', 'decoder2', 'decoder3', 'decoder4', 'decoder5', 'judge_decoder')
ENCODER_PREFIXES = ('encoder1', 'encoder2')
CNN_PREFIX = 'cnn'
BLOCK_PREFIXES = (CNN_PREFIX,) + ENCODER_PREFIXES + DECODER_PREFIXES


def layer_methods(model):
    """块外的基础层（final_norm、head、判断分支）；CNN、encoder、decoder 内部的层随整块导出。"""
    return {name.replace('.', '_'): (name, layer)
            for name, layer in model.named_modules()
            if isinstance(layer, LAYER_TYPES) and name.split('.')[0] not in BLOCK_PREFIXES}


def optional_mask(mask: Optional[torch.Tensor]) -> Optional[torch.Tensor]:
    """Java 的 NDList 不能传 None；零元素 BOOL 张量表示不使用该 mask。"""
    if mask is None or mask.numel() == 0:
        return None
    return mask


class TensorOps(nn.Module):
    def __init__(self, model):
        super().__init__()
        for name, (_, layer) in layer_methods(model).items():
            self.add_module('layer_' + name, layer)
        for name in BLOCK_PREFIXES:
            self.add_module('block_' + name, getattr(model, name))
        self.incremental = incremental_decoder(model)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        raise RuntimeError('Use a named exported method, not forward.')

    @torch.jit.export
    def silu(self, x: torch.Tensor) -> torch.Tensor:
        return F.silu(x)

    @torch.jit.export
    def decoder_start(self, memory: torch.Tensor) -> Tuple[torch.Tensor, torch.Tensor]:
        """memory [B,S,256] -> (图像 K/V 缓存 [5,2,B,4,S,64], 空的自注意力缓存 [5,2,B,4,0,64])"""
        return self.incremental.start(memory)

    @torch.jit.export
    def decoder_step(self, tokens: torch.Tensor, parents: torch.Tensor, self_cache: torch.Tensor,
                     cross_cache: torch.Tensor, memory_key_padding_mask: Optional[torch.Tensor] = None
                     ) -> Tuple[torch.Tensor, torch.Tensor]:
        """先按 parents [K] 重排上一步的自注意力缓存，再解码新 token [K,N]
        -> (最后一个新位置的 logits [K,vocab], 新缓存 [5,2,K,4,T+N,64])"""
        return self.incremental(tokens, self_cache.index_select(2, parents), cross_cache,
                                optional_mask(memory_key_padding_mask))


def load_model(checkpoint_path):
    checkpoint = torch.load(checkpoint_path, map_location='cpu', weights_only=True)
    state = checkpoint.get('model', checkpoint)
    model = vital_mini(num_tokens=state['token_embedding.weight'].size(0))
    # 不用随机值补缺失权重；导出必须严格匹配已有模型，包括判断分支。
    model.load_state_dict(state, strict=True)
    return model.eval()


def build_scripted(model):
    """为每个具名层生成简单包装方法，不手工维护数十份重复代码。

    TorchScript 需要可读取的 Python 源码，因此先写入临时 .py 文件。
    导出后的 .pt 自带程序和权重，不依赖该临时文件或 Python 环境。
    """
    module_name = '_generated_model_ops'
    source = ('import torch\nfrom typing import Optional\n'
              'from export_torchscript_ops import TensorOps, optional_mask\n\nclass ModelOps(TensorOps):\n')
    for name in layer_methods(model):
        source += (f'    @torch.jit.export\n'
                   f'    def {name}(self, x: torch.Tensor) -> torch.Tensor:\n'
                   f'        return self.layer_{name}(x)\n\n')
    source += (f'    @torch.jit.export\n'
               f'    def {CNN_PREFIX}(self, x: torch.Tensor) -> torch.Tensor:\n'
               f'        return self.block_{CNN_PREFIX}(x)\n\n')
    for name in ENCODER_PREFIXES:
        source += (f'    @torch.jit.export\n'
                   f'    def {name}(self, x: torch.Tensor, '
                   f'memory_key_padding_mask: Optional[torch.Tensor] = None) -> torch.Tensor:\n'
                   f'        return self.block_{name}(x, optional_mask(memory_key_padding_mask))\n\n')
    for name in DECODER_PREFIXES:
        source += (f'    @torch.jit.export\n'
                   f'    def {name}(self, x: torch.Tensor, memory: torch.Tensor, '
                   f'tgt_key_padding_mask: Optional[torch.Tensor] = None, '
                   f'memory_key_padding_mask: Optional[torch.Tensor] = None) -> torch.Tensor:\n'
                   f'        return self.block_{name}(x, memory, optional_mask(tgt_key_padding_mask), '
                   f'optional_mask(memory_key_padding_mask))\n\n')
    with tempfile.TemporaryDirectory(prefix='model-ops-') as directory:
        path = Path(directory) / (module_name + '.py')
        path.write_text(source, encoding='utf-8')
        spec = importlib.util.spec_from_file_location(module_name, path)
        generated = importlib.util.module_from_spec(spec)
        sys.modules[module_name] = generated
        try:
            spec.loader.exec_module(generated)
            return torch.jit.script(generated.ModelOps(model).eval())
        finally:
            sys.modules.pop(module_name, None)


def export(checkpoint_path, output_path):
    model = load_model(checkpoint_path)
    scripted = build_scripted(model)
    output_path.parent.mkdir(parents=True, exist_ok=True)
    # 不调用 freeze：保留所有按名称调用的方法。
    torch.jit.save(scripted, str(output_path))
    reloaded = torch.jit.load(str(output_path), map_location='cpu').eval()
    methods = {name: str(reloaded._c._get_method(name).schema)
               for name in reloaded._c._method_names() if not name.startswith('_') and name != 'forward'}
    manifest = {
        'checkpoint': checkpoint_path.name,
        'checkpoint_sha256': hashlib.sha256(checkpoint_path.read_bytes()).hexdigest(),
        'torch_version': torch.__version__,
        'mode': 'eval (inference only; dropout is identity, BN uses saved statistics)',
        'vocab_size': model.token_embedding.num_embeddings,
        'layers': {name: {'python_path': path, 'type': type(layer).__name__}
                   for name, (path, layer) in layer_methods(model).items()},
        'cnn_block': {'python_path': CNN_PREFIX,
                      'includes': 'model.vital_pixel scripted as is: convolutions, BatchNorm, SiLU, max pooling, '
                                  'the residual block after con4 and the 2D sinusoidal position embedding; '
                                  'image FLOAT32 [B,3,H,W] (H, W multiples of 16) -> FLOAT32 [B,256,H/16,W/16]'},
        'decoder_blocks': {name: {'python_path': name, 'includes': 'model.decoder scripted as is: causal self-attention, cross-attention, FFN (including SwiGLU), all normalization and residual connections'}
                           for name in DECODER_PREFIXES},
        'encoder_blocks': {name: {'python_path': name, 'includes': 'model.encoder scripted as is: non-causal visual self-attention, FFN (including SwiGLU), all normalization and residual connections'}
                           for name in ENCODER_PREFIXES},
        'incremental_decoding': {
            'python_path': 'model.incremental_decoder over decoder1..decoder5, token_embedding, final_norm, head',
            'decoder_start': 'memory FLOAT32 [B,S,256] -> (cross_cache [5,2,B,4,S,64], empty self_cache [5,2,B,4,0,64])',
            'decoder_step': 'tokens INT64 [K,N], parents INT64 [K] (rows of the previous self_cache), self_cache, '
                            'cross_cache (batch 1 is shared by all K), memory mask BOOL [1 or K,S] or empty '
                            '-> (logits FLOAT32 [K,vocab] of the last new position, self_cache [5,2,K,4,T+N,64]); '
                            'positions continue from T, at most max_len in total',
        },
        'mask_convention': 'True = blocked; omitted/None or an empty BOOL tensor = no padding mask. Encoders are non-causal; each decoder creates its own causal mask.',
        'scope': 'LayerNorm and Linear layers outside the CNN/encoder/decoder blocks, one shared silu, '
                 'the complete CNN, two complete visual encoders, six complete decoders and the cached decoding steps. '
                 'Layers inside the blocks are not exported separately.',
        'methods': methods,
    }
    output_path.with_suffix('.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    print(f'Exported {len(methods)} methods to {output_path}')
    return reloaded


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--checkpoint', type=Path, default=ROOT / 'checkpoints/model.pt')
    parser.add_argument('--output', type=Path, default=ROOT / 'checkpoints/model_ops.pt')
    args = parser.parse_args()
    if args.checkpoint.resolve() == args.output.resolve():
        parser.error('Output must not overwrite the training checkpoint.')
    export(args.checkpoint, args.output)
