"""将已有 234 词检查点扩展到矩阵词表；备份原文件并验证旧参数逐元素不变。"""
import argparse
import hashlib
from datetime import datetime, timezone
from pathlib import Path

import torch

from model import load_training_weights, load_vocab, vital_mini, vocab_size


def migrate(path: Path, backup_root: Path):
    original = path.read_bytes()
    digest = hashlib.sha256(original).hexdigest()
    checkpoint = torch.load(path, map_location='cpu', weights_only=True)
    old = checkpoint.get('model', checkpoint)
    old_size = old['token_embedding.weight'].shape[0]
    if old_size == vocab_size:
        print(f'已是 {vocab_size} 词，跳过 {path}')
        return
    if old_size != 234:
        raise ValueError(f'{path}: 预期 234 词，实际 {old_size}')
    model = vital_mini()
    assert load_training_weights(model, old)
    updated = model.state_dict()
    for key, value in old.items():
        actual = updated[key]
        if key in ('token_embedding.weight', 'head.weight'):
            actual = actual[:old_size]
        if not torch.equal(actual, value):
            raise ValueError(f'旧参数发生变化: {key}')
    backup = backup_root / digest[:12] / path.name
    backup.parent.mkdir(parents=True, exist_ok=True)
    if backup.exists() and backup.read_bytes() != original:
        raise ValueError(f'备份冲突: {backup}')
    backup.write_bytes(original)
    if 'model' not in checkpoint:
        checkpoint = {'model': updated}
    else:
        checkpoint['model'] = updated
    evaluation_keys = ('val_loss', 'exp_rate', 'validation_signature', 'validation', 'validation_by_source')
    checkpoint['pre_matrix_vocab_evaluation'] = {k: checkpoint.pop(k) for k in evaluation_keys if k in checkpoint}
    checkpoint['vocab_size'] = vocab_size
    checkpoint['matrix_vocab_migration'] = {
        'timestamp_utc': datetime.now(timezone.utc).isoformat(),
        'source_sha256': digest, 'backup': str(backup),
        'from_vocab_size': old_size, 'to_vocab_size': vocab_size,
        'added_tokens': {t: i for i, t in enumerate(load_vocab()) if i >= old_size},
        'initialization': 'mean of existing rows 0..66; tied embedding/head; not trained',
    }
    temporary = path.with_suffix('.matrix-tmp.pt')
    torch.save(checkpoint, temporary)
    reloaded = torch.load(temporary, map_location='cpu', weights_only=True)
    for key, value in updated.items():
        assert torch.equal(reloaded['model'][key], value), key
    if hashlib.sha256(path.read_bytes()).hexdigest() != digest:
        raise RuntimeError(f'文件在迁移时被其他进程修改: {path}')
    temporary.replace(path)
    print(f'已扩展并校验 {path}，备份 {backup}')


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('checkpoints', nargs='+', type=Path)
    parser.add_argument('--backup-root', type=Path, default=Path('checkpoints/before-matrix-vocab241'))
    args = parser.parse_args()
    torch.set_num_threads(2)
    for path in args.checkpoints:
        migrate(path, args.backup_root)
