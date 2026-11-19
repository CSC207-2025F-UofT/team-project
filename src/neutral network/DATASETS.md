# 公式训练数据

在项目根目录运行（使用已安装依赖的 Python）：

```bash
python -m pip install -r "src/neutral network/requirement-data.txt"
python "src/neutral network/data.py"
python "src/neutral network/train.py" --data "src/neutral network/data/mixed"
```

`data.py` 默认准备三个数据源，原始数据保留在各自目录；混合清单位于
`data/mixed/train.jsonl`、`valid.jsonl`、`test.jsonl`，实际条数与去重统计写入
`data/mixed/summary.json`。`train.py` 默认使用全部可用样本；调试时可以传
`--max-samples 1000`。更换验证集时会重新比较最佳验证损失，仍然加载已有模型权重；
优化器和学习率调度沿用原行为，每次运行重新初始化。

## 下载与复用

- MathWriting 使用 Google 官方压缩包，复用原来的 `data/mathwriting-2024/`，补齐 `synthetic` 和 `test`。
- UniMER 使用 `wanderkid/UniMER_Dataset`，HME100K 使用 `ConstantHao/HME100K`。
  HF 版本固定在 `data.py` 的 `HF_SOURCES`，避免重跑时自动切换数据版本。
- 完整下载有本地完成记录，大小或修改时间不变时不再请求下载；下载中断时保留临时文件并续传。
- 已完成的解压和转换使用完成记录复用。`--rebuild` 重建转换清单，仍复用完整下载。
- 可用 `--sources mathwriting` 或 `--sources unimer hme100k-hf` 仅准备指定来源。
  输出混合清单只包含本次选择的来源；再次运行默认命令可恢复三者混合。
- `--sample` 只准备 MathWriting 示例包，输出到独立的 `data/mixed-sample`。
- 全量运行需要 `huggingface_hub`、`pyarrow` 和 Pillow；这组依赖单独使用 PyPI，
  不受 CUDA 版 PyTorch 的下载源影响。

## 混合与验证划分

MathWriting 的 train 和 synthetic 用于训练，保留官方 valid/test。
UniMER、HME100K 的官方 test 只进入 test；从它们的训练集按公式哈希固定划分
5% 为 valid（`--valid-fraction` 可调整），因此重跑不会随机改变划分。

去重按图片解码后的灰度像素或 InkML 文件内容进行。跨分区相同 token 序列的
公式采用 test > valid > train 的优先级，避免相同公式进入训练与评估两边。
这会减少可用训练量，也会使混合 test 与原始官方基准的样本集合不同。
不同缩放、裁切或不同 LaTeX 写法的近似重复，不保证全部识别。

UniMER 的图片名整数对应标签文件的零基行号；保留空行的索引位置，空标签样本跳过。
混合时不会改写词表；训练仍按 `--oov skip` 和模型长度上限过滤，所以清单条数
不等于最终可训练条数。图片和 InkML 共用 batch 与损失函数，InkML 保留原有随机笔迹增强。
图片保留 RGB 颜色及原始明暗，裁切、缩放后输出 `[3,H,W]`，与推理共用
`preprocess.py`。InkML 渲染仍为单通道，组批时复制为三通道；图片不再转灰度。

缩放和画布尺寸由 `formula_layout.py` 决定，图片、InkML 和 Java 的 `FormulaLayout` 共用这一套规则：
笔迹按竖直方向切成"行带"（相邻笔迹之间的空白不小于笔迹总高的 3%，像素图片至少 2 像素），
缩放比例让最高的行带高 112 像素，四周留 8 像素。单行公式只有一个带，仍是高 128；
多行公式每行保持单行的大小，高度向上取整到 16 的倍数，最高 512。只有高度超过 512，或特征格数
（高/16 × 宽/16）超过 1280 时，才把整张图等比缩小。宽度不再单独限制。组批时高度和宽度都补到批内最大值，
`memory_mask` 同时屏蔽补出的行和列。这改变了缩放规则，原有权重需要继续训练后再使用。

高图和宽图同批时补出的画布可达单张上限的数倍，视觉自注意力的显存随 子批大小 × 格数² 增长。
因此 `train.py` 用 `collate_chunks` 把每个 batch 按补边后的画布拆成子批（`canvas_chunks`：
子批大小 × 格数 ≤ 32 × 1280，子批大小 × 格数² ≤ 32 × 1280²，即 32 张恰好 1280 格的图，峰值约 10 GiB），
各子批损失按 token 数加权累积梯度，等价于整批一起算的平均损失；验证也逐子批计算。
CNN 的 BatchNorm 按子批统计，子批变小时统计更嘈杂。
改动 `formula_layout.py` 的常量或算法时，必须同步修改 `FormulaLayout.java`，
并让 `tests/test_formula_layout.py` 与 `FormulaLayoutTest.java` 的期望值保持一致。

### 缩放系数模型（crop_scale）

在上面的规则之上，每张图再乘一个系数 m：实际缩放 = 112 / (最高行带 × m)，m 从 0.35 到 4
共 15 档。m 由 LightGBM 树模型按二值笔迹图的特征预测（`crop_scale.py`），模型由 `crop.py` 训练：
对验证图逐档试 m，以识别模型的 teacher forcing 损失最低者为标签。训练（`train.py`）、
Python 推理（`start.py`）和 Java 运行时（`CropScale.java` / `FormulaImagePreprocessor`）用同一份模型：

- 训练默认读取 `checkpoints/crop/crop_scale.json`，`--no-crop-scale` 关闭；所用模型的哈希写入权重元数据。
- Java 读取 `src/main/java/model/checkpoints/crop_scale.json`，换模型时把同一份文件复制过去。
- 长时间训练前先运行 `python crop.py cache`，给全部训练样本预先算好 m（约 20–30 分钟），
  缓存按模型哈希命名放在 `data/mixed/`；缺的样本训练时现算。
- 识别模型重新训练后缩放偏好会变，应重新 `crop.py label` + `crop.py fit`，再复制到 Java 端。
- 改动 `crop_scale.py` 的特征时必须同步修改 `CropScale.java`，并运行
  `tests/generate_crop_scale_fixtures.py` 重新生成对照数据；`test_crop.py` 和 `CropScaleTest.java` 会核对两边逐项一致。

## 训练数据增强

`train.py`、`train_decoder.py` 和 `train_test.py` 默认以 70% 概率增强每个训练样本，
其余样本使用原有预处理。图片在内存中动态增强，不增加清单条数、不改原文件或标签。

- 图片：最多 ±4° 旋转（长公式自动降低上限）、轻微水平倾斜、缩放和平移，
  以及随机的笔迹颜色、明暗、轻微模糊和噪声变化。旋转和倾斜先扩展画布，再完整缩入
  模型输入尺寸，不使用翻转、遮挡或随机裁剪。
- InkML：沿用已有的笔迹坐标旋转、倾斜、缩放和笔画粗细增强，不叠加图片几何变换。
- 验证集、测试集与推理不增强。`--seed` 控制训练随机性；固定 worker 数等配置时可复现。

使用 `--no-augment` 关闭全部训练增强，或 `--augment-prob 0.5` 调整概率。
`--augment-prob 0` 也表示不增强。生成训练会把增强配置记入日志和检查点，
是否改善识别率仍需比较固定验证集的 ExpRate。

## 个人手写训练图片

**默认检测 `train.py` 同级的 `train/` 文件夹，而不是 `val/`。**
每张图片放一个完整公式，推荐清晰的白底深色笔迹；文件夹可包含子目录。

```text
src/neutral network/train/
├── formula_0001.png
├── formula_0002.jpg
└── labels.jsonl
```

`labels.jsonl` 每行一个 JSON 对象，图片路径相对于该标签文件所在目录。
JSON 中 LaTeX 的反斜杠必须写成两个：

```jsonl
{"image":"formula_0001.png","latex":"x+1=2"}
{"image":"formula_0002.jpg","latex":"\\frac{a}{b}"}
```

默认训练会把这些标注图片加入混合训练数据；空目录会跳过，有图片却没有标签文件
会明确报错。`val/` 不会自动加入训练。可用 `--personal-train 路径` 指定另一个
个人训练目录，用 `--no-personal-train` 禁用自动加入。

还支持额外数据根目录 `--image-data my_dataset`，其结构可以是：

```text
my_dataset/
├── train/labels.jsonl
└── valid/labels.jsonl   # 可选；没有时只加入训练数据
```

或在该根目录直接放 `train.jsonl`、`valid.jsonl`。清单中的 `image` 也可写作 `path`；
InkML 记录使用 `"format":"inkml"`，图片默认是 `"format":"image"`。
同一原始页面的裁切图片应放在同一个分区，不要把训练图片的副本放进验证集。

## 数据来源

- [MathWriting 官方](https://github.com/google-research/google-research/tree/master/mathwriting)
- [UniMER 数据集](https://huggingface.co/datasets/wanderkid/UniMER_Dataset)
- [UniMER 官方图片/标签加载规则](https://github.com/opendatalab/UniMERNet/blob/main/unimernet/datasets/datasets/formula.py)
- [HME100K HF 社区版](https://huggingface.co/datasets/ConstantHao/HME100K)
- [HME100K 官方](https://github.com/Phymond/HME100K)
