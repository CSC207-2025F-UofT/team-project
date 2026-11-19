import json
import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from labels import FUNCTIONS, TOKEN_RE, normalize_structure, tokenize_label
from model import ids_to_latex, load_vocab
from data import label_key


class FunctionLabelsTests(unittest.TestCase):
    def test_all_function_spellings_and_longest_match(self):
        for name in FUNCTIONS:
            for raw in (name + '(x)', ' '.join(name) + ' ( x )', '\\' + name + '(x)',
                        r'\operatorname{' + name + '}(x)', r'\mathrm{' + name + '}(x)'):
                with self.subTest(raw=raw):
                    self.assertEqual(tokenize_label(raw), ['\\' + name, '(', 'x', ')'])

    def test_observed_attached_variables(self):
        cases = {
            r'ntcos\theta': ['n', 't', r'\cos', r'\theta'],
            r'ecos\theta': ['e', r'\cos', r'\theta'],
            r'vtan\alpha': ['v', r'\tan', r'\alpha'],
            'klogv': ['k', r'\log', 'v'],
            'loglogN': [r'\log', r'\log', 'N'],
            'sinxcosxdx': [r'\sin', 'x', r'\cos', 'x', 'd', 'x'],
            'sin h': [r'\sin', 'h'],
            'cos t': [r'\cos', 't'],
        }
        for raw, expected in cases.items():
            with self.subTest(raw=raw):
                self.assertEqual(tokenize_label(raw), expected)

    def test_preserve_ambiguous_words_and_commands(self):
        for raw in ('since', 'cost', 'distance', 'logistic', 'arcsinh', 'limsup', 'colim',
                    's i n c e', r'\sinx', r'\operatorname{colim}', r'\mathrm{cost}',
                    r'\begin{matrix}x\end{matrix}'):
            with self.subTest(raw=raw):
                self.assertEqual(tokenize_label(raw), TOKEN_RE.findall(raw))

    def test_preserve_text_and_aliases(self):
        self.assertEqual(tokenize_label(r'cos x+\text{cos  x {sin}}'),
                         [r'\cos', 'x', '+', r'\text', '{', 'c', 'o', 's', ' ', 'x', ' ',
                          '{', 's', 'i', 'n', '}', '}'])
        self.assertEqual(tokenize_label(r'\left(α\le x\right)'), ['(', r'\alpha', r'\leq', 'x', ')'])
        with self.assertRaises(ValueError):
            tokenize_label(r'\text{unclosed')

    def test_round_trip_and_dedup_use_training_tokens(self):
        table = load_vocab()
        lookup = {t: i for i, t in enumerate(table)}
        for raw in ('sinxcosxdx', 's i n h (x)', 'ntcos x', r'\text{cos x}+arccos y'):
            tokens = tokenize_label(raw)
            self.assertEqual(tokenize_label(ids_to_latex([lookup[t] for t in tokens], table)), tokens)
        self.assertEqual(label_key('cos x'), label_key(r'\cos x'))
        self.assertEqual(label_key('c o s ( x )'), label_key(r'\cos(x)'))
        self.assertNotEqual(label_key(r'\sin x'), label_key(r'\sinx'))
        self.assertNotEqual(label_key(r'\text{a b}'), label_key(r'\text{ab}'))

    def test_structure_normalization_unifies_equivalent_spellings(self):
        cases = {
            r'a_n^2': r'a_{n}^{2}',
            r'ds^2_{d+1}=-{dx}^2': r'ds_{d+1}^{2}=-dx^{2}',
            r'\sum_{i=0}^{n-1}{t^i}': r'\sum_{i=0}^{n-1}t^{i}',
            r'\sqrt2m+\frac12': r'\sqrt{2}m+\frac{1}{2}',
            r'\sqrt[3]x': r'\sqrt[3]{x}',
            r'x^\frac12': r'x^{\frac{1}{2}}',
            r'{{x}}^{{2}}': r'x^{2}',
            r'\bar x_1+\mathbb R': r'\bar{x}_{1}+\mathbb{R}',
            r'1,2,...,n': r'1,2,\ldots,n',
        }
        for raw, expected in cases.items():
            with self.subTest(raw=raw):
                self.assertEqual(tokenize_label(raw), tokenize_label(expected))
                self.assertEqual(tokenize_label(expected), normalize_structure(tokenize_label(expected)))

    def test_structure_normalization_keeps_meaningful_braces(self):
        kept = {
            r'{10^{218}}^{10}': ['{', '1', '0', '^', '{', '2', '1', '8', '}', '}', '^', '{', '1', '0', '}'],
            r'x^{a}{}^{b}': ['x', '^', '{', 'a', '}', '{', '}', '^', '{', 'b', '}'],
            r'\text{a ... b}': [r'\text', '{', 'a', ' ', '.', '.', '.', ' ', 'b', '}'],
            r'a....b': ['a', '.', '.', '.', '.', 'b'],
            r'x^{a': ['x', '^', '{', 'a'],
            r'{_2F_1}': ['{', '_', '{', '2', '}', 'F', '_', '{', '1', '}', '}'],
            r'{{{C^\prime}}}_4': ['{', 'C', "'", '}', '_', '{', '4', '}'],
            r'{\sqrt}B': ['{', r'\sqrt', '}', 'B'],
            r'\frac C \rho {X}': [r'\frac', '{', 'C', '}', '{', r'\rho', '}', 'X'],
            r'\rho {X}': [r'\rho', '{', 'X', '}'],
        }
        for raw, expected in kept.items():
            with self.subTest(raw=raw):
                self.assertEqual(tokenize_label(raw), expected)
        self.assertEqual(label_key('x^2_1'), label_key('x_{1}^{2}'))

    def test_primes_use_the_existing_apostrophe_token(self):
        same = {
            r"f'(x)": (r'f^{\prime}(x)', r"f^{'}(x)", r'f^\prime(x)', 'f′(x)', r'y\prime(x)'.replace('y', 'f')),
            r"f''": (r'f^{\prime\prime}', r"f^{''}"),
            r"f_{1}'": (r"f'_1", r'f^{\prime}_{1}', r'f_1^{\prime}'),
            r"f'^{2}": (r'f^{\prime 2}', r"f'^2"),
        }
        for expected, spellings in same.items():
            for raw in spellings:
                with self.subTest(raw=raw):
                    self.assertEqual(tokenize_label(raw), tokenize_label(expected))
        self.assertEqual(tokenize_label(r"f'(x)"), ['f', "'", '(', 'x', ')'])
        self.assertEqual(tokenize_label(r'L^{\sigma\prime}'), ['L', '^', '{', r'\sigma', "'", '}'])
        # 上标之后的撇号不是合法写法，保持原样；\text 里的撇号不动
        self.assertEqual(tokenize_label(r"f^{2}'"), ['f', '^', '{', '2', '}', "'"])
        self.assertEqual(tokenize_label(r"\text{it's}"), [r'\text', '{', 'i', 't', "'", 's', '}'])

    def test_python_java_vocab_and_new_ids(self):
        root = Path(__file__).resolve().parents[2]
        java = json.loads((root / 'main/java/model/checkpoints/tokeniser.json').read_text())['base']['vocab']
        table = load_vocab()
        self.assertEqual({t: i for i, t in enumerate(table)}, java)
        self.assertEqual(table[188:200], ['\\' + n for n in
            'arcsin arccos arctan sinh cosh tanh exp max min cot sec csc'.split()])


if __name__ == '__main__':
    unittest.main()
