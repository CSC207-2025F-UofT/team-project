"""训练与去重共用的保守标签规范化；原始标签不覆盖。"""
import re

NORMALIZATION_VERSION = 4
TOKEN_RE = re.compile(r"\\[a-zA-Z]+|\\.|\S", re.S)
ALIASES = {r"\le": r"\leq", r"\ge": r"\geq", r"\ne": r"\neq", r"\to": r"\rightarrow",
           r"\Box": r"\square", "□": r"\square", "⟨": r"\langle", "⟩": r"\rangle",
           "⊥": r"\perp", "‖": r"\|", r"\Vert": r"\|", r"\lVert": r"\|", r"\rVert": r"\|",
           # 撇号统一为词表里的 '（f^{\prime} 由 normalize_structure 再化成 f'）
           r"\prime": "'", "′": "'", "’": "'", "´": "'"}
LABEL_RE = re.compile(r'<annotation type="normalizedLabel">(.*?)</annotation>', re.S)
GREEK_ALIASES = dict(zip("αβγδεζηθικλμνξοπρστυφχψω", ("\\" + name for name in
    "alpha beta gamma delta varepsilon zeta eta theta iota kappa lambda mu nu xi omicron pi rho sigma tau upsilon varphi chi psi omega".split())))
GREEK_ALIASES.update(dict(zip("ΓΔΘΛΞΠΣΥΦΨΩ", ("\\" + name for name in
    "Gamma Delta Theta Lambda Xi Pi Sigma Upsilon Phi Psi Omega".split()))))
GREEK_ALIASES.update({"ϵ": r"\epsilon", "ϑ": r"\vartheta", "ϰ": r"\varkappa", "ϖ": r"\varpi",
                      "ϱ": r"\varrho", "ς": r"\varsigma", "ϕ": r"\phi"})
ALIASES.update(GREEK_ALIASES)
TEXT_START_RE = re.compile(r"\\text\s*\{")
TEXT_TOKEN_RE = re.compile(r"\\[a-zA-Z]+|\\.|\s+|\S", re.S)
FUNCTIONS = tuple(sorted(("sin", "cos", "tan", "log", "ln", "lim",
                         "arcsin", "arccos", "arctan", "sinh", "cosh", "tanh",
                         "exp", "max", "min", "cot", "sec", "csc"), key=lambda x: (-len(x), x)))
PROTECTED_WORDS = frozenset("since cost costs sinc sinhc coshc arcsinh arccosh arctanh "
                            "arccot arcsec arccsc logit logistic logarithm colim limsup liminf "
                            "limit limits minimum maximum minimal maximal maxwell second secant "
                            "cotangent distance constant".split())
WORD_RE = re.compile(r"[A-Za-z]+")
LEXEME_RE = re.compile(r"\\[a-zA-Z]+|\\.|[A-Za-z]+|\s+|.", re.S)
SPACED_WORD_RE = re.compile(r"(?<![A-Za-z\\])[A-Za-z](?:\s+[A-Za-z])+(?![A-Za-z])")
WRAPPER_RE = re.compile(r"\\(?:operatorname|mathrm|mathop)\s*\{\s*([A-Za-z\s]+)\s*\}")
PROTECTED_GROUP_RE = re.compile(r"\\(?:operatorname|mathrm|mathit|mathbf|mathsf|mathtt|begin|end)\*?\s*\{")


def _word_tokens(word: str) -> list[str]:
    if word.lower() in PROTECTED_WORDS or len(word) > 64:
        return list(word)

    def parse(rest: str):
        for offset in range(min(2, len(rest)) + 1):
            prefix = rest[:offset]
            if any(c not in "abceijkmnprstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" for c in prefix):
                continue
            for name in FUNCTIONS:
                if not rest[offset:].startswith(name):
                    continue
                tail = rest[offset + len(name):]
                result = list(prefix) + ["\\" + name]
                if not tail or len(tail) == 1 or re.fullmatch(r"[xyzt]?d[xyzt]", tail):
                    return result + list(tail)
                following = parse(tail)
                if following is not None:
                    return result + following
        return None
    return parse(word) or list(word)


def _plain_math(text: str) -> list[str]:
    text = WRAPPER_RE.sub(lambda m: "\\" + "".join(m[1].split()) + " "
                          if "".join(m[1].split()) in FUNCTIONS else m[0], text)
    text = SPACED_WORD_RE.sub(lambda m: " ".join(_word_tokens("".join(m[0].split()))), text)
    result = []
    for match in LEXEME_RE.finditer(text):
        token = match[0]
        if token.isspace() or token in {r"\left", r"\right"}:
            continue
        if WORD_RE.fullmatch(token):
            result.extend(_word_tokens(token))
        else:
            result.append(ALIASES.get(token, token))
    return result


def math_tokens(text: str) -> list[str]:
    result, position = [], 0
    while match := PROTECTED_GROUP_RE.search(text, position):
        result.extend(_plain_math(text[position:match.start()]))
        depth, end = 1, match.end()
        for item in TOKEN_RE.finditer(text, end):
            depth += (item[0] == "{") - (item[0] == "}")
            end = item.end()
            if depth == 0:
                break
        wrapped = text[match.start():end]
        exact = WRAPPER_RE.fullmatch(wrapped)
        if exact and "".join(exact[1].split()) in FUNCTIONS:
            result.append("\\" + "".join(exact[1].split()))
        else:
            result.extend(TOKEN_RE.findall(wrapped))
        position = end
    result.extend(_plain_math(text[position:]))
    return result


def tokenize_label(label: str) -> list[str]:
    result, position = [], 0
    while match := TEXT_START_RE.search(label, position):
        result.extend(math_tokens(label[position:match.start()]))
        result.extend([r"\text", "{"])
        depth = 1
        for token_match in TEXT_TOKEN_RE.finditer(label, match.end()):
            token = token_match.group()
            if token == "{":
                depth += 1
            elif token == "}":
                depth -= 1
            result.append(" " if token.isspace() else token)
            if depth == 0:
                position = token_match.end()
                break
        else:
            raise ValueError(f"标签的 \\text 区域缺少右花括号: {label}")
    result.extend(math_tokens(label[position:]))
    return normalize_structure(result)


# ---------- 结构规范化：同一渲染结果只保留一种写法 ----------
# 上下标及 \frac/\sqrt 等参数一律加花括号；同时有上下标时先 _ 后 ^；
# 去掉不影响渲染的分组括号（{dx}^2 -> dx^{2}）；三个连续的 . 统一为 \ldots；
# 撇号写在下标之后、上标之前（f^{\prime}_{1} -> f_{1}'，f^{\prime 2} -> f'^{2}）。
# \text、\begin/\end、\mathrm 等受保护区域原样保留；花括号不配对的标签整体不改。
SCRIPT_OPS = ("_", "^")
PRIME = "'"
SCRIPT_MARKS = SCRIPT_OPS + (PRIME,)
ARGUMENT_COUNTS = {**dict.fromkeys(r"\frac \dfrac \tfrac \binom \overset \underset \stackrel".split(), 2),
                   **dict.fromkeys(r"\sqrt \vec \hat \overline \bar \dot \ddot \mathbb \mathcal \mathfrak "
                                   r"\mathscr \boldsymbol \bm \pmb \textrm \textbf \textit \tilde \widehat "
                                   r"\widetilde \overrightarrow \underline \overbrace \underbrace \boxed "
                                   r"\mathring \check \breve \acute \grave".split(), 1)}
# 与 PROTECTED_GROUP_RE 保护的命令一致，另加 \text
OPAQUE_COMMANDS = frozenset(r"\text \operatorname \mathrm \mathit \mathbf \mathsf \mathtt \begin \end".split())
ROW_TOKENS = frozenset(("&", r"\\"))


class _Group(list):
    """{...} 内的节点"""


class _Raw(list):
    """原样输出的受保护 token 片段"""


def _braces_balanced(tokens: list[str]) -> bool:
    depth = 0
    for token in tokens:
        depth += (token == "{") - (token == "}")
        if depth < 0:
            return False
    return depth == 0


def _parse(tokens: list[str], i: int = 0, nested: bool = False) -> tuple[list, int]:
    nodes = []
    while i < len(tokens):
        token = tokens[i]
        if token == "}" and nested:
            return nodes, i + 1
        if token == "{":
            children, i = _parse(tokens, i + 1, True)
            nodes.append(_Group(children))
        elif token in OPAQUE_COMMANDS:
            end = i + 1 + (i + 1 < len(tokens) and tokens[i + 1] == "*")
            if end < len(tokens) and tokens[end] == "{":
                depth = 0
                for end in range(end, len(tokens)):
                    depth += (tokens[end] == "{") - (tokens[end] == "}")
                    if depth == 0:
                        break
                end += 1
            nodes.append(_Raw(tokens[i:end]))
            i = end
        else:
            nodes.append(token)
            i += 1
    return nodes, i


def _top_level(nodes: list, wanted) -> bool:
    return any(isinstance(node, str) and node in wanted for node in nodes)


def _is_command(token) -> bool:
    return isinstance(token, str) and token[:1] == "\\" and token[1:].isalpha()


def _keep_group(nodes: list, i: int, after_command: bool) -> bool:
    children = nodes[i]
    while len(children) == 1 and isinstance(children[0], _Group):      # {{{C^{\prime}}}}_4 看最内层
        children = children[0]
    if (not children or _top_level(children, ROW_TOKENS) or after_command or children[0] in SCRIPT_MARKS
            or (isinstance(children[-1], str) and children[-1] in ARGUMENT_COUNTS)):
        return True                     # {}^{a} 占位、矩阵行、未知命令的参数、{_2F_1} 前置角标、缺参数的 {\sqrt}
    following = nodes[i + 1] if i + 1 < len(nodes) else None
    return following in SCRIPT_MARKS and _top_level(children, SCRIPT_MARKS)  # {10^{2}}^{3}、{f^{2}}'、{C'}_4


def _argument(nodes: list, j: int) -> tuple[list[str], int]:
    """命令或上下标的一个参数，统一加花括号"""
    if j >= len(nodes) or nodes[j] in SCRIPT_OPS:
        return [], j
    node = nodes[j]
    if isinstance(node, _Group):
        while len(node) == 1 and isinstance(node[0], _Group):   # {{x}} -> {x}
            node = node[0]
        return ["{", *_emit(node), "}"], j + 1
    if isinstance(node, str) and node in ARGUMENT_COUNTS:
        tokens, j = _command(nodes, j)
        return ["{", *tokens, "}"], j
    return ["{", *(node if isinstance(node, _Raw) else [node]), "}"], j + 1


def _command(nodes: list, i: int) -> tuple[list[str], int]:
    tokens, j = [nodes[i]], i + 1
    if nodes[i] == r"\sqrt" and j < len(nodes) and nodes[j] == "[" and "]" in nodes[j:]:
        close = nodes.index("]", j)
        tokens += ["[", *_emit(nodes[j + 1:close]), "]"]
        j = close + 1
    for _ in range(ARGUMENT_COUNTS[nodes[i]]):
        argument, j = _argument(nodes, j)
        tokens += argument
    return tokens, j


def _scripts(nodes: list, i: int) -> tuple[list[str], int]:
    """一串连续的 _ ^ ' -> 下标、撇号、上标余下部分；^{'} 里开头的撇号移出上标。"""
    items = []                                              # (标记, 参数 token；撇号为 None)
    while i < len(nodes) and nodes[i] in SCRIPT_MARKS:
        if nodes[i] == PRIME:
            items.append((PRIME, None))
            i += 1
            continue
        argument, next_i = _argument(nodes, i + 1)
        items.append((nodes[i], argument))
        i = next_i if next_i > i + 1 else i + 1
    marks = [mark for mark, _ in items]
    sup_at = marks.index("^") if "^" in marks else len(marks)
    if (marks.count("^") > 1 or marks.count("_") > 1 or PRIME in marks[sup_at:]
            or any(argument == [] for _, argument in items)):
        # 重复上下标、上标后的撇号、缺参数: 不是合法写法，保持原顺序
        return [token for mark, argument in items for token in (mark, *(argument or []))], i
    scripts = dict(items)
    primes = marks.count(PRIME)
    sup = scripts.get("^", [])[1:-1]
    leading = next((k for k, token in enumerate(sup) if token != PRIME), len(sup))
    tokens = ["_", *scripts["_"]] if "_" in scripts else []
    tokens += [PRIME] * (primes + leading)
    if "^" in scripts and (sup[leading:] or not leading):
        tokens += ["^", "{", *sup[leading:], "}"]
    return tokens, i


def _emit(nodes: list) -> list[str]:
    out, i = [], 0
    while i < len(nodes):
        node = nodes[i]
        # 紧跟在原样输出的命令 token 之后的分组可能是它的参数（只看实际输出，参数已被消费的不算）
        after_command = i > 0 and _is_command(nodes[i - 1]) and out[-1:] == [nodes[i - 1]]
        if isinstance(node, _Raw):
            out += node
            i += 1
        elif isinstance(node, _Group):
            out += ["{", *_emit(node), "}"] if _keep_group(nodes, i, after_command) else _emit(node)
            i += 1
        elif node in SCRIPT_MARKS:
            tokens, i = _scripts(nodes, i)
            out += tokens
        elif node in ARGUMENT_COUNTS:
            tokens, i = _command(nodes, i)
            out += tokens
        elif node == "." and nodes[i:i + 3] == [".", ".", "."] and nodes[i + 3:i + 4] != ["."] \
                and nodes[i - 1:i] != ["."]:
            out.append(r"\ldots")
            i += 3
        else:
            out.append(node)
            i += 1
    return out


def normalize_structure(tokens: list[str]) -> list[str]:
    """tokenize_label 的最后一步；结果再规范化一次不变。"""
    if not _braces_balanced(tokens):
        return tokens
    return _emit(_parse(tokens)[0])
