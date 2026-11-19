import json
import math
from dataclasses import dataclass, field
from pathlib import Path
import torch
import torch.nn as nn
import torch.nn.functional as F

d_model = 256
attention_heads = 4
vocab_size = 234
legacy_vocab_sizes = (147, 148, 149, 154, 186, 187, 188, 200)
pad_id, bos_id, eos_id = 67, 68, 69   # 与 tokeniser.json 中的特殊 token 对应
max_len = 300                        # LaTeX token 序列的最大长度(含 <bos>/<eos>)

'''输入的照片应该满足以下格式
宽度和高度像素都是16的倍数，高度至少128像素（多行公式更高，见 formula_layout.py）
原始照片可以通过缩放或补齐背景来满足要求，补齐背景的颜色最好是白色
'''

def SwiGLU(gate: torch.Tensor, content: torch.Tensor) -> torch.Tensor:
    """gate/content 均为 [B,T,m]；SiLU 门控与内容逐元素相乘，不混合位置。"""
    return F.silu(gate) * content

def load_vocab(path: Path = Path(__file__).with_name("tokeniser.json")) -> list[str]:
    # tokeniser.json 里是 token -> id,翻转成下标即 id 的列表
    def unique_object(pairs):
        result = {}
        for key, value in pairs:
            if key in result:
                raise ValueError(f"tokeniser.json 存在重复键: {key}")
            result[key] = value
        return result
    with open(path, encoding="utf-8") as f:
        vocab = json.load(f, object_pairs_hook=unique_object)["base"]["vocab"]
    if len(vocab) != vocab_size or any(type(i) is not int for i in vocab.values()) or set(vocab.values()) != set(range(vocab_size)):
        raise ValueError(f"tokeniser.json 必须包含 {vocab_size} 个 token，编号连续且唯一为 0–{vocab_size - 1}")
    table = [""] * len(vocab)
    for token, i in vocab.items():
        table[i] = token
    return table

def ids_to_latex(ids: list[int], table: list[str]) -> str:
    # 仅在命令后紧接英文字母时补分隔符，保留 \text{...} 内真正的空格。
    out = []
    for position, i in enumerate(ids):
        t = table[i]
        next_token = table[ids[position + 1]] if position + 1 < len(ids) else ""
        needs_separator = (t.startswith("\\") and t[1:].isascii() and t[1:].isalpha()
                           and next_token and next_token[0].isascii() and next_token[0].isalpha())
        out.append(t + " " if needs_separator else t)
    return "".join(out).strip()


def complete_judge_weights(model: nn.Module, state_dict: dict) -> dict:
    """迁移旧词表分数检测头；本体不变，新版检测头缺损或形状错误仍报错。"""
    state_dict = dict(state_dict)
    prefixes = ("judge_decoder.", "judge_norm.", "process.", "test.")
    defaults = {key: value for key, value in model.state_dict().items() if key.startswith(prefixes)}
    if any(key.startswith(("judge_decoder.", "judge_norm.")) for key in state_dict):
        missing = defaults.keys() - state_dict.keys()
        if missing:
            raise ValueError(f"新版检测头权重不完整: {sorted(missing)}")
        return state_dict

    # 历史版本可能没有检测头、只有 test，或同时有 process/test。
    old_size = state_dict["token_embedding.weight"].shape[0]
    for name, shapes in (("test", ((2, old_size), (2,))),
                         ("process", ((old_size, old_size), (old_size,)))):
        keys = (f"{name}.weight", f"{name}.bias")
        present = set(keys) & state_dict.keys()
        if present and present != set(keys):
            raise ValueError(f"旧判断层 {name} 权重不完整")
        for key, shape in zip(keys, shapes):
            if key in state_dict and tuple(state_dict[key].shape) != shape:
                raise ValueError(f"旧判断层 {key} 形状不兼容: {tuple(state_dict[key].shape)}")
    if "process.weight" in state_dict and "test.weight" not in state_dict:
        raise ValueError("旧判断层 process 存在但 test 缺失")
    # 旧分数空间与新的图片/前缀表示不同，整个检测分支重新初始化。
    state_dict = {key: value for key, value in state_dict.items() if not key.startswith(prefixes)}
    state_dict.update({key: value.detach().clone() for key, value in defaults.items()})
    return state_dict


def load_training_weights(model: nn.Module, state_dict: dict) -> bool:
    """兼容历史词表和检测结构，保留生成本体及新版检测头的已有参数。"""
    state_dict = complete_judge_weights(model, state_dict)
    keys = ("token_embedding.weight", "head.weight")
    old = state_dict[keys[0]]
    old_size = old.shape[0]
    expanded = old_size in legacy_vocab_sizes and all(
        state_dict[key].shape == (old_size, d_model) for key in keys)
    if expanded:
        if load_vocab()[147:154] != [" ", r"\square", r"\langle", r"\rangle", r"\perp", r"\overline", r"\bar"]:
            raise ValueError("旧权重仅支持在原有词表末尾追加符号，不支持修改旧 token 编号")
        # 两个层共享权重；新行用原有字符嵌入均值初始化，之后参与训练。
        if not torch.equal(state_dict[keys[0]], state_dict[keys[1]]):
            raise ValueError("旧权重的词嵌入与输出层不一致")
        new_rows = old[:67].mean(dim=0, keepdim=True).expand(vocab_size - old_size, -1)
        extended = torch.cat((old, new_rows), dim=0)
        for key in keys:
            state_dict[key] = extended
    model.load_state_dict(complete_cnn_residual_weights(model, state_dict), strict=True)
    return expanded


def complete_cnn_residual_weights(model: nn.Module, state_dict: dict) -> dict:
    """没有 CNN 残差块的旧权重:补上零初始化的残差块(输出与旧模型一致);残差块不完整仍报错。"""
    prefix = "cnn.res_"
    defaults = {key: value for key, value in model.state_dict().items() if key.startswith(prefix)}
    present = {key for key in state_dict if key.startswith(prefix)}
    if present:
        if present != defaults.keys():
            raise ValueError(f"CNN 残差块权重不完整: {sorted(defaults.keys() - present)}")
        return state_dict
    model.cnn.reset_residual()
    state_dict = dict(state_dict)
    state_dict.update({key: value.detach().clone() for key, value in model.state_dict().items()
                       if key.startswith(prefix)})
    return state_dict

@dataclass
class BeamState:
    """beam search 在两步之间要带着走的全部状态"""
    tokens: torch.Tensor                           # [k,T] 活跃假设,第 0 列是 <bos>
    scores: torch.Tensor                           # [k] 每条假设的累计 log 概率
    finished: list = field(default_factory=list)   # 已生成 <eos> 的序列: [(归一化得分, [token id])]
    done: bool = False
    length_alpha: float = 0.7
    parents: torch.Tensor | None = None            # [k] 每条活跃假设来自上一步的第几条,用来重排 KV 缓存

    @staticmethod
    def start(device, length_alpha: float = 0.7) -> "BeamState":
        return BeamState(tokens=torch.full((1, 1), bos_id, dtype=torch.long, device=device),
                         scores=torch.zeros(1, device=device),
                         length_alpha=length_alpha)

    def norm_score(self, score: float, n: int) -> float:
        # 总 log 概率每多一个 token 就更低,除以 n^alpha 抵消偏向短序列的倾向;只用于比较序列
        return score / (max(n, 1) ** self.length_alpha)

    def best(self) -> list[int]:
        """得分最高的序列(不含 <bos>/<eos>);一个 <eos> 都没生成时取最好的活跃假设"""
        cands = list(self.finished)
        if not cands:
            i = int(self.scores.argmax())
            cands.append((self.norm_score(float(self.scores[i]), self.tokens.size(1) - 1),
                          self.tokens[i, 1:].tolist()))
        return max(cands, key=lambda c: c[0])[1]

def parse_tokens(logits: torch.Tensor, state: BeamState | None = None, beam_width: int = 3,
                 max_steps: int = max_len, length_alpha: float = 0.7) -> BeamState:
    """beam search 的一步: 吃进 decode(或 forward)的 logits,吐出新的 BeamState
    logits [k,V] 或 [k,T,V](取最后一位): 当前 k 条活跃假设各自的"下一个 token"分布
    state: 上一步返回的状态,第一步传 None
    beam_width: 束宽,同时保留的候选序列数,1 时等价于贪心
    max_steps: 序列最长长度(含 <bos>/<eos>),不能超过 max_len
    length_alpha: 长度归一化指数,0 表示不归一化;只在 state 为 None 时生效"""
    assert beam_width >= 1 and max_steps <= max_len
    if logits.dim() == 3:
        logits = logits[:, -1]
    dev = logits.device
    if state is None:
        state = BeamState.start(dev, length_alpha)
    k = state.tokens.size(0)
    assert logits.size(0) == k, "logits 的行数必须等于活跃假设数"

    # 1) 新得分 = 旧得分 + log 概率;所有假设 × 所有 token 摊平后一起比较 -> [k*V]
    logp = F.log_softmax(logits.float(), dim=-1)
    logp[:, [pad_id, bos_id]] = float("-inf")      # 这两个只用于输入,不允许被生成
    cand = (state.scores[:, None] + logp).reshape(-1)
    top_scores, top_idx = cand.topk(min(2 * beam_width, cand.numel()))   # 多取一倍,用来补 <eos> 占掉的名额
    output_size = logits.size(-1)                 # 旧权重推理可能使用更小的词表
    src = (top_idx // output_size).tolist()        # 这个候选来自第几条假设
    tok = (top_idx % output_size).tolist()         # 这个候选选了哪个 token
    sc = top_scores.tolist()

    # 2) 按得分从高到低扫描: <eos> 放进完成列表,其余的补满 beam_width 条活跃假设
    finished = list(state.finished)
    keep: list[int] = []
    for rank in range(len(tok)):
        if tok[rank] == eos_id:
            if rank < beam_width:                  # 排名靠后的 <eos> 不算数
                seq = state.tokens[src[rank], 1:].tolist()                 # 去掉 <bos>
                finished.append((state.norm_score(sc[rank], len(seq) + 1), seq))
        else:
            keep.append(rank)
            if len(keep) == beam_width:
                break
    if not keep:                                   # 前几名全是 <eos>,没有可以继续的假设
        return BeamState(state.tokens, state.scores, finished, True, state.length_alpha)

    # 3) 活跃假设 = 来源假设的前缀 + 新选的 token
    parent = torch.tensor([src[r] for r in keep], device=dev)
    tokens = torch.cat([state.tokens[parent], torch.tensor([[tok[r]] for r in keep], device=dev)], dim=1)
    scores = top_scores[torch.tensor(keep, device=dev)]
    new = BeamState(tokens, scores, finished, False, state.length_alpha, parent)

    # 4) 停止条件: 到达最大长度;或(启发式)已有 beam_width 条完成,且最好的活跃假设已追不上最好的完成序列
    if tokens.size(1) >= max_steps:
        new.done = True
    elif len(finished) >= beam_width:
        best_alive = new.norm_score(scores.max().item(), tokens.size(1) - 1)
        new.done = best_alive <= max(f[0] for f in finished)
    return new

class vital_pixel(nn.Module):
    # 不写类级别的子模块注解: TorchScript 整块导出时会把它们当成不支持的类型
    def __init__(self):
        super(vital_pixel, self).__init__()
        self.conv = nn.Conv2d(3, 16, kernel_size=5, stride=1, padding=2, padding_mode="replicate")
        self.norm = nn.BatchNorm2d(16)
        self.conv2 = nn.Conv2d(16, 32, kernel_size=5, stride=1, padding=2)
        self.norm2 = nn.BatchNorm2d(32)
        self.conv3 = nn.Conv2d(32, 64, kernel_size=5, stride=1, padding=2, padding_mode="replicate")
        self.norm3 = nn.BatchNorm2d(64)
        self.con4 = nn.Conv2d(64, 128, kernel_size=5, stride=2, padding=2)
        self.con5 = nn.Conv2d(128, d_model, kernel_size=5, stride=1, padding=2)
        self.norm4 = nn.BatchNorm2d(128)
        self.norm5 = nn.BatchNorm2d(d_model)
        # con4 之后的残差块(128 通道,1/16 分辨率): x + norm_b(conv_b(silu(norm_a(conv_a(x)))))
        self.res_conv1 = nn.Conv2d(128, 128, kernel_size=3, stride=1, padding=1)
        self.res_norm1 = nn.BatchNorm2d(128)
        self.res_conv2 = nn.Conv2d(128, 128, kernel_size=3, stride=1, padding=1)
        self.res_norm2 = nn.BatchNorm2d(128)
        self.reset_residual()

    def reset_residual(self):
        """残差分支末尾 BN 的 γ、β 置零: 分支输出恒为 0,加入残差块不改变原有 CNN 的输出(训练和推理模式都一样)。"""
        nn.init.zeros_(self.res_norm2.weight)
        nn.init.zeros_(self.res_norm2.bias)

    @staticmethod
    def encode_positions(n: int, half: int, x: torch.Tensor) -> torch.Tensor:  # -> [n, half]
        pos = torch.arange(n, device=x.device, dtype=x.dtype).unsqueeze(1)
        freq = torch.exp(torch.arange(0, half, 2, device=x.device, dtype=x.dtype) * (-math.log(10000.0) / half))
        ang = pos * freq
        return torch.stack([torch.sin(ang), torch.cos(ang)], dim=2).flatten(1)

    def position_embedding(self, x: torch.Tensor) -> torch.Tensor:
        # 2D sinusoidal encoding: first half of channels encodes the row, second half the column
        _, c, h, w = x.shape
        half = c // 2
        row = self.encode_positions(h, half, x).t().unsqueeze(2).expand(half, h, w)
        col = self.encode_positions(w, half, x).t().unsqueeze(1).expand(half, h, w)
        return x + torch.cat([row, col], dim=0).unsqueeze(0)

    def forward(self, x: torch.Tensor):
        x = self.conv(x)
        x = self.norm(x)
        x = F.silu(x)
        x = F.max_pool2d(x, kernel_size=2, stride=2)
        x = self.conv2(x)
        x = self.norm2(x)
        x = F.silu(x)
        x = F.max_pool2d(x, kernel_size=2, stride=2)
        x = self.conv3(x)
        x = self.norm3(x)
        x = F.silu(x)
        x = F.max_pool2d(x, kernel_size=2, stride=2)
        x = self.con4(x)
        x = self.norm4(x)
        x = F.silu(x)
        branch = F.silu(self.res_norm1(self.res_conv1(x)))
        x = x + self.res_norm2(self.res_conv2(branch))      # 相加后不再接激活,保证零初始化时等价于原网络
        x = self.con5(x)
        x = self.norm5(x)
        x = self.position_embedding(x)
        return x


class attention_module(nn.Module):
    def __init__(self, dropout: float = 0.1):
        super(attention_module, self).__init__()
        assert d_model % attention_heads == 0
        self.head_dim = d_model // attention_heads
        self.q_proj = nn.Linear(d_model, d_model)
        self.k_proj = nn.Linear(d_model, d_model)
        self.v_proj = nn.Linear(d_model, d_model)
        self.out_proj = nn.Linear(d_model, d_model)
        self.dropout = nn.Dropout(dropout)
        self.norm = nn.LayerNorm(d_model)
        self.heads = attention_heads
        self.width = d_model

    def split_heads(self, x: torch.Tensor) -> torch.Tensor:
        b, t, _ = x.shape
        return x.view(b, t, self.heads, self.head_dim).transpose(1, 2)                  # [B,T,d] -> [B,H,T,hd]

    def project_kv(self, kv: torch.Tensor) -> tuple[torch.Tensor, torch.Tensor]:
        # kv [B,Tk,d] -> K, V 各 [B,H,Tk,hd];逐 token 解码时缓存的就是它们
        return self.split_heads(self.k_proj(kv)), self.split_heads(self.v_proj(kv))

    def attend(self, residual: torch.Tensor, normalised: torch.Tensor, k: torch.Tensor, v: torch.Tensor,
               attn_mask: torch.Tensor | None = None,
               key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # normalised [B,Tq,d] 是 norm(residual);k/v [B或1,H,Tk,hd],batch 为 1 时按广播共享
        b, tq, _ = normalised.shape
        q = self.split_heads(self.q_proj(normalised))                                  # [B,H,Tq,hd]
        scores = q @ k.transpose(-2, -1) / math.sqrt(self.head_dim)                     # [B,H,Tq,Tk] q*k^T / sqrt(d_k)

        if attn_mask is not None:
            scores = scores.masked_fill(attn_mask, float("-inf"))
        if key_padding_mask is not None:
            scores = scores.masked_fill(key_padding_mask[:, None, None, :], float("-inf"))

        attn = self.dropout(F.softmax(scores, dim=-1))
        out = (attn @ v).transpose(1, 2).reshape(b, tq, self.width)                     # merge heads
        out = self.dropout(self.out_proj(out))
        return residual + out  # residual connection

    def forward(self, x: torch.Tensor, memory: torch.Tensor | None = None,
                attn_mask: torch.Tensor | None = None,
                key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # x [B,Tq,d]; memory [B,Tk,d] for cross-attention (None -> self-attention)
        # attn_mask [Tq,Tk] and key_padding_mask [B,Tk] are bool, True = do not attend
        normalised = self.norm(x)
        k, v = self.project_kv(normalised if memory is None else memory)
        return self.attend(x, normalised, k, v, attn_mask, key_padding_mask)

    def forward_cached(self, x: torch.Tensor, past_k: torch.Tensor,
                       past_v: torch.Tensor) -> tuple[torch.Tensor, torch.Tensor, torch.Tensor]:
        """因果自注意力的增量版本: x [B,N,d] 是接在已缓存的 P 个位置后面的新 token,
        past_k/past_v [B,H,P,hd](P 可为 0) -> (输出 [B,N,d], 拼上新 token 的 K, V [B,H,P+N,hd])"""
        normalised = self.norm(x)
        new_k, new_v = self.project_kv(normalised)
        k, v = torch.cat((past_k, new_k), dim=2), torch.cat((past_v, new_v), dim=2)
        n, total = x.size(1), k.size(2)
        causal: torch.Tensor | None = None
        if n > 1:                                      # 只有一个新 token 时它本来就能看全部缓存
            rows = torch.arange(n, device=x.device).unsqueeze(1) + (total - n)
            causal = torch.arange(total, device=x.device).unsqueeze(0) > rows
        return self.attend(x, normalised, k, v, causal), k, v

class FFN(nn.Module):

    fc1 : nn.Linear
    fc2 : nn.Linear
    def __init__(self):
        super(FFN, self).__init__()
        m = 1024
        self.norm = nn.LayerNorm(d_model)
        self.gate = nn.Linear(d_model, m)
        self.up = nn.Linear(d_model, m)
        self.down = nn.Linear(m, d_model)
        self.dropout = nn.Dropout(0.2)

    def forward(self, x: torch.Tensor) -> torch.Tensor:
        normalised = self.norm(x)
        hidden = SwiGLU(self.gate(normalised), self.up(normalised))
        return self.dropout(self.down(hidden)) + x

class encoder(nn.Module):
    """视觉特征的全局自注意力与 FFN；归一化和残差由子模块提供。"""

    def __init__(self):
        super(encoder, self).__init__()
        self.self_attention = attention_module()
        self.ffn = FFN()

    def forward(self, x: torch.Tensor,
                memory_key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # x [B,S,d] 是展平后的图像特征；mask [B,S]，True 屏蔽补边，不使用因果遮罩。

        x = self.self_attention(x, key_padding_mask=memory_key_padding_mask)
        return self.ffn(x)

class decoder(nn.Module):
    # 不写 self_attention: attention_module 这类类级注解:TorchScript 会把它当属性类型解析而报错,
    # 导出脚本直接 script 这个类。

    def __init__(self):
        super(decoder, self).__init__()
        self.self_attention = attention_module()
        self.ffn = FFN()
        self.cross_attention = attention_module()

    def forward(self, x: torch.Tensor, memory: torch.Tensor,
                tgt_key_padding_mask: torch.Tensor | None = None,
                memory_key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # x [B,T,d]: token 序列; memory [B,S,d]: 图像特征(S = h*w)
        # tgt_key_padding_mask [B,T]: <pad> token 的位置; memory_key_padding_mask [B,S]: 图像补边的格子; True = 屏蔽
        T = x.size(1)
        # 因果 mask: 第 i 个 token 只能看 0..i,上三角(不含对角线)为 True 即屏蔽未来
        causal = torch.triu(torch.ones(T, T, dtype=torch.bool, device=x.device), diagonal=1)
        # 自注意力: 看自己已经写了什么(括号配对、语法)
        x = self.self_attention(x, attn_mask=causal, key_padding_mask=tgt_key_padding_mask)
        # 交叉注意力: 去图像里找下一个该写什么;图像一次给全,所以不用因果 mask
        x = self.cross_attention(x, memory=memory, key_padding_mask=memory_key_padding_mask)
        return self.ffn(x)

    def cross_kv(self, memory: torch.Tensor) -> tuple[torch.Tensor, torch.Tensor]:
        """图像特征 memory [B,S,d] 在交叉注意力里的 K, V;逐 token 解码时整张图只算一次"""
        return self.cross_attention.project_kv(memory)

    def step(self, x: torch.Tensor, self_k: torch.Tensor, self_v: torch.Tensor,
             cross_k: torch.Tensor, cross_v: torch.Tensor,
             memory_key_padding_mask: torch.Tensor | None = None
             ) -> tuple[torch.Tensor, torch.Tensor, torch.Tensor]:
        """forward 的增量版本(只做推理): x [B,N,d] 是新 token;self_k/self_v 是之前位置的缓存,
        cross_k/cross_v 来自 cross_kv -> (输出 [B,N,d], 更新后的 self_k, self_v)"""
        x, self_k, self_v = self.self_attention.forward_cached(x, self_k, self_v)
        x = self.cross_attention.attend(x, self.cross_attention.norm(x), cross_k, cross_v,
                                        None, memory_key_padding_mask)
        return self.ffn(x), self_k, self_v


class incremental_decoder(nn.Module):
    """vital_mini 五个 decoder 的逐 token 推理:缓存自注意力的 K/V,图像的 K/V 只算一次。
    结果与 vital_mini.decode 对整条序列取最后一位相同,但每步只计算新 token。
    不持有新参数,只引用 vital_mini 的层;generate 和 TorchScript 导出共用这一份实现。
    缓存: 自注意力 [L,2,B,H,T,hd](2 = K/V,T = 已解码的位置数);图像 [L,2,B,H,S,hd],B 为 1 时广播给所有假设。"""

    def __init__(self, model: "vital_mini"):
        super().__init__()
        self.layers = nn.ModuleList(model.decoder_layers())
        self.token_embedding = model.token_embedding
        self.embed_dropout = model.embed_dropout
        self.final_norm = model.final_norm
        self.head = model.head
        self.register_buffer("token_pos", model.token_pos, persistent=False)
        self.scale = math.sqrt(d_model)

    def start(self, memory: torch.Tensor) -> tuple[torch.Tensor, torch.Tensor]:
        """memory [B,S,d] -> (图像缓存 [L,2,B,H,S,hd], 空的自注意力缓存 [L,2,B,H,0,hd])"""
        cross = []
        for layer in self.layers:
            k, v = layer.cross_kv(memory)
            cross.append(torch.stack((k, v)))
        cross_cache = torch.stack(cross)
        empty = cross_cache.new_zeros((cross_cache.size(0), 2, cross_cache.size(2), cross_cache.size(3),
                                       0, cross_cache.size(5)))
        return cross_cache, empty

    def forward(self, tokens: torch.Tensor, self_cache: torch.Tensor, cross_cache: torch.Tensor,
                memory_key_padding_mask: torch.Tensor | None = None) -> tuple[torch.Tensor, torch.Tensor]:
        """tokens [B,N] 接在缓存的位置之后(逐步解码时 N = 1)
        -> (最后一个新位置的 logits [B,vocab], 更新后的自注意力缓存 [L,2,B,H,T+N,hd])"""
        start, n = self_cache.size(4), tokens.size(1)
        if start + n > self.token_pos.size(1):
            raise ValueError("token sequence is longer than max_len")
        x = self.token_embedding(tokens) * self.scale + self.token_pos[:, start:start + n]
        x = self.embed_dropout(x)
        caches = []
        for i, layer in enumerate(self.layers):
            x, k, v = layer.step(x, self_cache[i, 0], self_cache[i, 1], cross_cache[i, 0], cross_cache[i, 1],
                                 memory_key_padding_mask)
            caches.append(torch.stack((k, v)))
        return self.head(self.final_norm(x[:, -1])), torch.stack(caches)


class vital_mini(nn.Module):

    cnn : vital_pixel
    decoder1 : decoder
    decoder2 : decoder
    decoder3 : decoder
    token_embedding : nn.Embedding
    final_norm : nn.LayerNorm
    head : nn.Linear
    judge_decoder : decoder
    judge_norm : nn.LayerNorm
    process : nn.Linear
    test :nn.Linear

    def __init__(self, num_tokens: int = vocab_size):
        super(vital_mini, self).__init__()
        self.cnn = vital_pixel()
        self.decoder1 = decoder()
        self.decoder2 = decoder()
        self.decoder3 = decoder()
        self.decoder4 = decoder()
        self.decoder5 = decoder()
        self.encoder1 = encoder()
        self.encoder2 = encoder()

        # token id -> 向量; <pad> 对应的行保持为 0
        self.token_embedding = nn.Embedding(num_tokens, d_model, padding_idx=pad_id)
        nn.init.normal_(self.token_embedding.weight, mean=0.0, std=d_model ** -0.5)  # 配合 forward 里乘 sqrt(d)
        with torch.no_grad():
            self.token_embedding.weight[pad_id].zero_()
        self.embed_dropout = nn.Dropout(0.1)
        # token 序列的 1D 正弦位置表(固定值,不训练,不进 state_dict);图像的 2D 位置编码在 vital_pixel 里
        self.register_buffer("token_pos", self.build_token_pos(max_len), persistent=False)

        self.final_norm = nn.LayerNorm(d_model)
        self.head = nn.Linear(d_model, num_tokens, bias=False)
        self.head.weight = self.token_embedding.weight

        # 检测分支直接核对前缀与图片，不再使用下一 token 的词表分数。
        self.judge_decoder = decoder()
        self.judge_norm = nn.LayerNorm(d_model)
        self.process = nn.Linear(d_model, d_model)
        self.test = nn.Linear(d_model, 2)

    @staticmethod
    def build_token_pos(n: int) -> torch.Tensor:  # -> [1, n, d_model]
        pos = torch.arange(n, dtype=torch.float).unsqueeze(1)
        freq = torch.exp(torch.arange(0, d_model, 2, dtype=torch.float) * (-math.log(10000.0) / d_model))
        ang = pos * freq
        return torch.stack([torch.sin(ang), torch.cos(ang)], dim=2).flatten(1).unsqueeze(0)

    def decoder_layers(self) -> tuple[decoder,...]:
        return (self.decoder1, self.decoder2, self.decoder3, self.decoder4, self.decoder5)

    def encode(self, x: torch.Tensor) -> torch.Tensor:#[B,C,H,W] -> [B,H*W,C]
        # [B,C,h,w] -> [B,h*w,C]: flatten h,w row-major, then move channels last
        if x.requires_grad:
            # transpose 的反向梯度是 channels-last 布局的非连续张量；PyTorch 2.11 在 CPU 上 batch=1 时
            # 拿它反传 CNN 会算错参数梯度（CUDA 正常）。训练按画布分子批后单张子批更常见，先转成连续内存。
            x.register_hook(torch.Tensor.contiguous)
        return x.flatten(2).transpose(1, 2)

    def encode_image(self, image: torch.Tensor,
                     memory_key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        memory = self.encode(self.cnn(image))
        memory = self.encoder1(memory, memory_key_padding_mask)
        return self.encoder2(memory, memory_key_padding_mask)

    def decode(self, tokens: torch.Tensor, memory: torch.Tensor,
               tgt_key_padding_mask: torch.Tensor | None = None,
               memory_key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # tokens [B,T] (long), memory [B,S,d] -> logits [B,T,vocab];导出 ONNX 时这一段就是 decoder_step.onnx
        T = tokens.size(1)
        assert T <= max_len, "token sequence is longer than max_len"
        # 嵌入乘 sqrt(d) 再加位置编码,让两者量级相当
        x = self.token_embedding(tokens) * math.sqrt(d_model) + self.token_pos[:, :T]
        x = self.embed_dropout(x)
        for layer in self.decoder_layers():
            x = layer(x, memory, tgt_key_padding_mask, memory_key_padding_mask)
        return self.head(self.final_norm(x))#[B,T,vocab]

    def forward(self, image: torch.Tensor, tokens: torch.Tensor,
                tgt_key_padding_mask: torch.Tensor | None = None,
                memory_key_padding_mask: torch.Tensor | None = None) -> torch.Tensor:
        # 训练(teacher forcing): tokens 是 <bos> y1..yn,logits[:, t] 预测的是 y_{t+1}
        memory = self.encode_image(image, memory_key_padding_mask)
        decoded = self.decode(tokens, memory, tgt_key_padding_mask, memory_key_padding_mask)
        return decoded

    def judge_modules(self):
        return (self.judge_decoder, self.judge_norm, self.process, self.test)

    def judge_parameters(self):
        """判断器的全部参数，供冻结、优化器和梯度裁剪共用。"""
        for module in self.judge_modules():
            yield from module.parameters()

    def judge_prefix(self, image: torch.Tensor, tokens: torch.Tensor,
                     tgt_key_padding_mask: torch.Tensor | None = None,
                     memory_key_padding_mask: torch.Tensor | None = None,
                     *, return_logits: bool = False) -> torch.Tensor:
        """图片 [B,3,H,W] + 前缀 [B,T] -> [B,T,2]；内部编码图片，位置 t 只看 0..t。"""
        if tokens.ndim != 2 or not 1 <= tokens.size(1) <= max_len:
            raise ValueError(f"前缀必须为 [B,T]，且 1 <= T <= {max_len}")
        if image.ndim != 4 or image.size(0) != tokens.size(0) or image.size(1) != 3:
            raise ValueError("图片必须为与前缀批量对齐的 [B,3,H,W]")
        memory = self.encode_image(image, memory_key_padding_mask)
        if tgt_key_padding_mask is None:
            tgt_key_padding_mask = tokens.eq(pad_id)
        x = self.token_embedding(tokens) * math.sqrt(d_model) + self.token_pos[:, :tokens.size(1)]
        x = self.judge_decoder(x, memory, tgt_key_padding_mask, memory_key_padding_mask)
        logits = self.test(F.silu(self.process(self.judge_norm(x))))
        return logits if return_logits else F.softmax(logits, dim=-1)



    #todo: reconsiderate searching method to avoid missing the correct sequence as much as possible
    #note: Dijkstra's algorithm might be a good choice
    @torch.no_grad()
    def generate(self, image: torch.Tensor, beam_width: int = 3, max_steps: int = max_len,
                 length_alpha: float = 0.7) -> list[list[int]]:
        # image [B,3,H,W] -> B 条 token id 序列(不含 <bos>/<eos>);转成字符串用 ids_to_latex
        # 每一轮循环: 只把每条假设新选的 token 喂进去,decoder 的 KV 缓存记住前面的位置
        # -> parse_tokens 选出新的 k 条假设 -> 按 parents 重排缓存
        was_training = self.training
        self.eval()                                           # 关 dropout;BatchNorm 使用滑动统计
        stepper = incremental_decoder(self)
        results = []
        for i in range(image.size(0)):                        # 每张图各做一次 beam search
            memory = self.encode_image(image[i:i + 1])        # 每张图只过一次 CNN 和视觉 encoder
            cross_cache, cache = stepper.start(memory)        # 图像 K/V 只算一次,batch 1 广播给各假设
            tokens = torch.full((1, 1), bos_id, dtype=torch.long, device=image.device)
            state = None
            while True:
                logits, cache = stepper(tokens[:, -1:], cache, cross_cache)
                state = parse_tokens(logits, state, beam_width, max_steps, length_alpha)
                if state.done:
                    break
                cache = cache.index_select(2, state.parents)
                tokens = state.tokens
            results.append(state.best())
        self.train(was_training)
        return results
