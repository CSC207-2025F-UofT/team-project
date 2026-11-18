package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;

/**
 * Turns a LaTeX string such as {@code y=\sin(x-\frac{\pi}{2})+1} into a {@link MathFunction} of x.
 *
 * <p>Supported: numbers, x, e, \pi, + - \times \cdot * \div /, implicit multiplication (3x, 2\pi x),
 * ^ powers, \frac, \sqrt (with optional [n]), |abs|, brackets (including \left( \right)),
 * \sin \cos \tan \cot \sec \csc, their arc and hyperbolic versions, \ln, \log (with optional _base),
 * \exp, and powers on functions such as \sin^2 x or \sin^{-1} x.
 * An equation is accepted when one side is just {@code y} or {@code f(x)}.
 * {@link #parseSurface} reads a function of x and y the same way, named {@code z} or {@code f(x,y)}.
 */
public class LatexFunctionParser {

    private static final Set<String> FUNCTIONS = new HashSet<>(Arrays.asList(
            "sin", "cos", "tan", "cot", "sec", "csc",
            "arcsin", "arccos", "arctan", "sinh", "cosh", "tanh",
            "ln", "log", "lg", "exp"));

    private static final Set<String> TRIG = new HashSet<>(Arrays.asList(
            "sin", "cos", "tan"));

    /** Words recognised even without a backslash, longest first so "sinh" wins over "sin". */
    private static final List<String> BARE_WORDS = Arrays.asList(
            "arcsin", "arccos", "arctan", "sqrt", "sinh", "cosh", "tanh",
            "sin", "cos", "tan", "cot", "sec", "csc", "exp", "log", "ln", "pi");

    private static final Map<String, String> SYMBOL_COMMANDS = new HashMap<>();

    static {
        SYMBOL_COMMANDS.put("times", "*");
        SYMBOL_COMMANDS.put("cdot", "*");
        SYMBOL_COMMANDS.put("ast", "*");
        SYMBOL_COMMANDS.put("div", "/");
    }

    private enum Kind { NUMBER, LETTER, COMMAND, SYMBOL }

    private static final class Token {
        final Kind kind;
        final String text;

        Token(Kind kind, String text) {
            this.kind = kind;
            this.text = text;
        }

        boolean is(Kind k, String t) {
            return kind == k && text.equals(t);
        }

        boolean isSymbol(String t) {
            return is(Kind.SYMBOL, t);
        }

        @Override
        public String toString() {
            return kind == Kind.COMMAND ? "\\" + text : text;
        }
    }

    private List<Token> tokens;
    private int pos;
    private int absDepth;
    private boolean surface;  // y is a variable, not the function name

    /**
     * Parses the LaTeX string into a function of x.
     *
     * @throws LatexParseException if the string is empty, malformed, or not an explicit function of x
     */
    public MathFunction parse(String latex) throws LatexParseException {
        DoubleBinaryOperator rule = parseRule(latex, false);
        return new MathFunction(latex, x -> rule.applyAsDouble(x, 0));
    }

    /**
     * Parses a function of x and y, e.g. {@code z = x^2 + y^2} or {@code f(x,y) = \sin x \cos y}.
     *
     * @throws LatexParseException if the string is not an explicit function of x and y
     */
    public SurfaceFunction parseSurface(String latex) throws LatexParseException {
        return new SurfaceFunction(latex, parseRule(latex, true));
    }

    // same grammar both ways, surface just lets y in as a variable
    private DoubleBinaryOperator parseRule(String latex, boolean twoVariables) throws LatexParseException {
        if (latex == null || latex.trim().isEmpty()) {
            throw new LatexParseException("The expression is empty.");
        }
        surface = twoVariables;
        List<Token> all = tokenize(latex);
        tokens = selectFunctionSide(all);
        pos = 0;
        absDepth = 0;
        if (tokens.isEmpty()) {
            throw new LatexParseException("The expression is empty.");
        }
        DoubleBinaryOperator rule = parseExpression();
        if (pos < tokens.size()) {
            throw new LatexParseException("Unexpected '" + tokens.get(pos) + "'.");
        }
        return rule;
    }

    // ---------------------------------------------------------------- tokenizer

    private List<Token> tokenize(String s) throws LatexParseException {
        List<Token> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c) || c == '$') {
                i++;
            } else if (Character.isDigit(c) || (c == '.' && i + 1 < s.length()
                    && Character.isDigit(s.charAt(i + 1)))) {
                int start = i;
                while (i < s.length() && (Character.isDigit(s.charAt(i)) || s.charAt(i) == '.')) {
                    i++;
                }
                out.add(new Token(Kind.NUMBER, s.substring(start, i)));
            } else if (Character.isLetter(c) && c < 128) {
                String word = matchBareWord(s, i);
                if (word != null) {
                    out.add(new Token(Kind.COMMAND, word));
                    i += word.length();
                } else {
                    out.add(new Token(Kind.LETTER, String.valueOf(c)));
                    i++;
                }
            } else if (c == '\\') {
                i = readCommand(s, i, out);
            } else if (c == '×' || c == '·' || c == '∗') {
                out.add(new Token(Kind.SYMBOL, "*"));
                i++;
            } else if (c == '÷') {
                out.add(new Token(Kind.SYMBOL, "/"));
                i++;
            } else if (c == '−' || c == '–') {
                out.add(new Token(Kind.SYMBOL, "-"));
                i++;
            } else if (c == 'π') {
                out.add(new Token(Kind.COMMAND, "pi"));
                i++;
            } else if ("+-*/^_()[]{}|=,".indexOf(c) >= 0) {
                out.add(new Token(Kind.SYMBOL, String.valueOf(c)));
                i++;
            } else {
                throw new LatexParseException("Unexpected character '" + c + "'.");
            }
        }
        return out;
    }

    private static String matchBareWord(String s, int i) {
        for (String word : BARE_WORDS) {
            if (s.startsWith(word, i)) {
                return word;
            }
        }
        return null;
    }

    /** Reads a backslash command starting at index i, appends its tokens, and returns the next index. */
    private int readCommand(String s, int i, List<Token> out) throws LatexParseException {
        int start = i + 1;
        if (start >= s.length()) {
            throw new LatexParseException("Dangling '\\' at the end.");
        }
        if (!Character.isLetter(s.charAt(start))) {
            char next = s.charAt(start);
            if (",;:! \\".indexOf(next) >= 0) {
                return start + 1;  // spacing / line break
            }
            if (next == '{' || next == '}') {
                out.add(new Token(Kind.SYMBOL, next == '{' ? "(" : ")"));
                return start + 1;
            }
            throw new LatexParseException("Unsupported LaTeX command '\\" + next + "'.");
        }
        int end = start;
        while (end < s.length() && Character.isLetter(s.charAt(end))) {
            end++;
        }
        String name = s.substring(start, end);
        switch (name) {
            case "left":
            case "right":
                // The delimiter that follows stays; "\left." is an invisible delimiter.
                if (end < s.length() && s.charAt(end) == '.') {
                    end++;
                }
                return end;
            case "operatorname":
            case "mathrm":
            case "mathit":
                return readBracedContent(s, end, out);
            case "text":
            case "mbox":
                throw new LatexParseException("Text cannot be graphed; please write a function of x.");
            default:
                if (SYMBOL_COMMANDS.containsKey(name)) {
                    out.add(new Token(Kind.SYMBOL, SYMBOL_COMMANDS.get(name)));
                } else {
                    out.add(new Token(Kind.COMMAND, name));
                }
                return end;
        }
    }

    /** Handles \operatorname{sin} and \mathrm{e}: tokenizes the braced content in place. */
    private int readBracedContent(String s, int i, List<Token> out) throws LatexParseException {
        if (i >= s.length() || s.charAt(i) != '{') {
            return i;
        }
        int close = s.indexOf('}', i);
        if (close < 0) {
            throw new LatexParseException("Missing '}'.");
        }
        out.addAll(tokenize(s.substring(i + 1, close)));
        return close + 1;
    }

    // ---------------------------------------------------------------- equations

    /** For "y = ..." or "f(x) = ..." keep the side that defines the function. */
    private List<Token> selectFunctionSide(List<Token> all) throws LatexParseException {
        List<Integer> equals = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).isSymbol("=")) {
                equals.add(i);
            }
        }
        if (equals.isEmpty()) {
            return all;
        }
        if (equals.size() > 1) {
            throw new LatexParseException("Only one '=' is allowed, e.g. y = x^2.");
        }
        List<Token> lhs = all.subList(0, equals.get(0));
        List<Token> rhs = all.subList(equals.get(0) + 1, all.size());
        if (isFunctionName(lhs)) {
            return new ArrayList<>(rhs);
        }
        if (isFunctionName(rhs)) {
            return new ArrayList<>(lhs);
        }
        throw new LatexParseException(surface
                ? "Write the surface explicitly, e.g. z = x^2 + y^2 or f(x,y) = x y."
                : "Write the function explicitly, e.g. y = x^2 or f(x) = x^2.");
    }

    // y / f(x) for curves, z / f(x,y) for surfaces
    private boolean isFunctionName(List<Token> side) {
        if (side.size() == 1) {
            return side.get(0).is(Kind.LETTER, surface ? "z" : "y");
        }
        boolean named = side.size() >= 4
                && side.get(0).kind == Kind.LETTER
                && side.get(1).isSymbol("(")
                && side.get(2).is(Kind.LETTER, "x")
                && side.get(side.size() - 1).isSymbol(")");
        if (!named) {
            return false;
        }
        return surface
                ? side.size() == 6 && side.get(3).isSymbol(",") && side.get(4).is(Kind.LETTER, "y")
                : side.size() == 4;
    }

    // ---------------------------------------------------------------- parser

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    private boolean peekSymbol(String s) {
        Token t = peek();
        return t != null && t.isSymbol(s);
    }

    private void expectSymbol(String s) throws LatexParseException {
        if (!peekSymbol(s)) {
            Token t = peek();
            throw new LatexParseException("Expected '" + s + "' but found "
                    + (t == null ? "the end of the expression" : "'" + t + "'") + ".");
        }
        pos++;
    }

    private DoubleBinaryOperator parseExpression() throws LatexParseException {
        DoubleBinaryOperator left = parseTerm();
        while (peekSymbol("+") || peekSymbol("-")) {
            boolean plus = peek().isSymbol("+");
            pos++;
            DoubleBinaryOperator a = left;
            DoubleBinaryOperator b = parseTerm();
            left = plus ? (x, y) -> a.applyAsDouble(x, y) + b.applyAsDouble(x, y)
                    : (x, y) -> a.applyAsDouble(x, y) - b.applyAsDouble(x, y);
        }
        return left;
    }

    private DoubleBinaryOperator parseTerm() throws LatexParseException {
        DoubleBinaryOperator left = parseUnary();
        while (true) {
            DoubleBinaryOperator a = left;
            if (peekSymbol("*")) {
                pos++;
                DoubleBinaryOperator b = parseUnary();
                left = (x, y) -> a.applyAsDouble(x, y) * b.applyAsDouble(x, y);
            } else if (peekSymbol("/")) {
                pos++;
                DoubleBinaryOperator b = parseUnary();
                left = (x, y) -> a.applyAsDouble(x, y) / b.applyAsDouble(x, y);
            } else if (startsImplicitFactor(true)) {
                DoubleBinaryOperator b = parsePower();
                left = (x, y) -> a.applyAsDouble(x, y) * b.applyAsDouble(x, y);
            } else {
                return left;
            }
        }
    }

    private DoubleBinaryOperator parseUnary() throws LatexParseException {
        if (peekSymbol("-")) {
            pos++;
            DoubleBinaryOperator a = parseUnary();
            return (x, y) -> -a.applyAsDouble(x, y);
        }
        if (peekSymbol("+")) {
            pos++;
            return parseUnary();
        }
        return parsePower();
    }

    private DoubleBinaryOperator parsePower() throws LatexParseException {
        DoubleBinaryOperator base = parsePrimary();
        if (peekSymbol("^")) {
            pos++;
            DoubleBinaryOperator exponent = parseScriptArgument();
            return (x, y) -> Math.pow(base.applyAsDouble(x, y), exponent.applyAsDouble(x, y));
        }
        return base;
    }

    /** True if the next token can start a factor written without an explicit operator, as in 3x. */
    private boolean startsImplicitFactor(boolean allowFunctions) {
        Token t = peek();
        if (t == null) {
            return false;
        }
        switch (t.kind) {
            case NUMBER:
            case LETTER:
                return true;
            case COMMAND:
                return allowFunctions || !FUNCTIONS.contains(t.text);
            default:
                return t.isSymbol("(") || t.isSymbol("[") || t.isSymbol("{")
                        || (t.isSymbol("|") && absDepth == 0);
        }
    }

    private DoubleBinaryOperator parsePrimary() throws LatexParseException {
        Token t = peek();
        if (t == null) {
            throw new LatexParseException("The expression ends unexpectedly.");
        }
        pos++;
        switch (t.kind) {
            case NUMBER:
                double value = parseNumber(t.text);
                return (x, y) -> value;
            case LETTER:
                if (t.text.equals("x")) {
                    return (x, y) -> x;
                }
                if (surface && t.text.equals("y")) {
                    return (x, y) -> y;
                }
                if (t.text.equals("e")) {
                    return (x, y) -> Math.E;
                }
                throw new LatexParseException("Unknown variable '" + t.text + "'. "
                        + (surface ? "Only x and y can be graphed." : "Only x can be graphed."));
            case COMMAND:
                return parseCommand(t.text);
            default:
                return parseBracketed(t);
        }
    }

    private DoubleBinaryOperator parseBracketed(Token open) throws LatexParseException {
        String close;
        switch (open.text) {
            case "(":
                close = ")";
                break;
            case "[":
                close = "]";
                break;
            case "{":
                close = "}";
                break;
            case "|":
                absDepth++;
                DoubleBinaryOperator inner = parseExpression();
                expectSymbol("|");
                absDepth--;
                return (x, y) -> Math.abs(inner.applyAsDouble(x, y));
            default:
                throw new LatexParseException("Unexpected '" + open + "'.");
        }
        int savedAbs = absDepth;
        absDepth = 0;
        DoubleBinaryOperator inner = parseExpression();
        expectSymbol(close);
        absDepth = savedAbs;
        return inner;
    }

    private DoubleBinaryOperator parseCommand(String name) throws LatexParseException {
        switch (name) {
            case "pi":
                return (x, y) -> Math.PI;
            case "frac":
            case "dfrac":
            case "tfrac": {
                DoubleBinaryOperator num = parseScriptArgument();
                DoubleBinaryOperator den = parseScriptArgument();
                return (x, y) -> num.applyAsDouble(x, y) / den.applyAsDouble(x, y);
            }
            case "sqrt": {
                DoubleBinaryOperator degree = null;
                if (peekSymbol("[")) {
                    pos++;
                    degree = parseExpression();
                    expectSymbol("]");
                }
                DoubleBinaryOperator radicand = parseScriptArgument();
                if (degree == null) {
                    return (x, y) -> Math.sqrt(radicand.applyAsDouble(x, y));
                }
                DoubleBinaryOperator n = degree;
                return (x, y) -> nthRoot(radicand.applyAsDouble(x, y), n.applyAsDouble(x, y));
            }
            default:
                if (FUNCTIONS.contains(name)) {
                    return parseFunction(name);
                }
                throw new LatexParseException("Unsupported LaTeX command '\\" + name + "'.");
        }
    }

    /**
     * Argument of ^, _, \frac or \sqrt: a {braced group}, or a single token as in LaTeX
     * (so x^23 means x^2 * 3 and \frac12 means 1/2). A leading minus is allowed: x^-1.
     */
    private DoubleBinaryOperator parseScriptArgument() throws LatexParseException {
        Token t = peek();
        if (t == null) {
            throw new LatexParseException("The expression ends unexpectedly.");
        }
        if (t.isSymbol("{")) {
            pos++;
            int savedAbs = absDepth;
            absDepth = 0;
            DoubleBinaryOperator inner = parseExpression();
            expectSymbol("}");
            absDepth = savedAbs;
            return inner;
        }
        if (t.isSymbol("-")) {
            pos++;
            DoubleBinaryOperator a = parseScriptArgument();
            return (x, y) -> -a.applyAsDouble(x, y);
        }
        if (t.kind == Kind.NUMBER && t.text.length() > 1 && t.text.charAt(0) != '.') {
            // Only the first digit belongs to the script; the rest stays in the stream.
            tokens.set(pos, new Token(Kind.NUMBER, t.text.substring(1)));
            double digit = t.text.charAt(0) - '0';
            return (x, y) -> digit;
        }
        return parsePrimary();
    }

    private DoubleBinaryOperator parseFunction(String name) throws LatexParseException {
        DoubleBinaryOperator logBase = null;
        if ((name.equals("log")) && peekSymbol("_")) {
            pos++;
            logBase = parseScriptArgument();
        }
        DoubleBinaryOperator power = null;
        if (peekSymbol("^")) {
            pos++;
            power = parseScriptArgument();
        }
        String effective = name;
        if (power != null && TRIG.contains(name) && isConstant(power, -1)) {
            effective = "arc" + name;  // \sin^{-1} x means arcsin x
            power = null;
        }

        DoubleBinaryOperator arg = parseFunctionArgument();
        DoubleBinaryOperator applied = applyFunction(effective, arg, logBase);
        if (power == null) {
            return applied;
        }
        DoubleBinaryOperator p = power;
        return (x, y) -> Math.pow(applied.applyAsDouble(x, y), p.applyAsDouble(x, y));
    }

    /** \sin(x+1) takes the bracket; \sin 2x takes the product 2x but stops before another function. */
    private DoubleBinaryOperator parseFunctionArgument() throws LatexParseException {
        if (peekSymbol("(") || peekSymbol("[") || peekSymbol("{")) {
            return parsePrimary();
        }
        if (peek() == null) {
            throw new LatexParseException("A function is missing its argument.");
        }
        DoubleBinaryOperator arg;
        if (peekSymbol("-")) {
            pos++;
            DoubleBinaryOperator a = parsePower();
            arg = (x, y) -> -a.applyAsDouble(x, y);
        } else {
            arg = parsePower();
        }
        while (startsImplicitFactor(false)) {
            DoubleBinaryOperator a = arg;
            DoubleBinaryOperator b = parsePower();
            arg = (x, y) -> a.applyAsDouble(x, y) * b.applyAsDouble(x, y);
        }
        return arg;
    }

    private static DoubleBinaryOperator applyFunction(String name, DoubleBinaryOperator arg,
                                                     DoubleBinaryOperator logBase) {
        switch (name) {
            case "sin":
                return (x, y) -> Math.sin(arg.applyAsDouble(x, y));
            case "cos":
                return (x, y) -> Math.cos(arg.applyAsDouble(x, y));
            case "tan":
                return (x, y) -> Math.tan(arg.applyAsDouble(x, y));
            case "cot":
                return (x, y) -> 1.0 / Math.tan(arg.applyAsDouble(x, y));
            case "sec":
                return (x, y) -> 1.0 / Math.cos(arg.applyAsDouble(x, y));
            case "csc":
                return (x, y) -> 1.0 / Math.sin(arg.applyAsDouble(x, y));
            case "arcsin":
                return (x, y) -> Math.asin(arg.applyAsDouble(x, y));
            case "arccos":
                return (x, y) -> Math.acos(arg.applyAsDouble(x, y));
            case "arctan":
                return (x, y) -> Math.atan(arg.applyAsDouble(x, y));
            case "sinh":
                return (x, y) -> Math.sinh(arg.applyAsDouble(x, y));
            case "cosh":
                return (x, y) -> Math.cosh(arg.applyAsDouble(x, y));
            case "tanh":
                return (x, y) -> Math.tanh(arg.applyAsDouble(x, y));
            case "ln":
                return (x, y) -> Math.log(arg.applyAsDouble(x, y));
            case "exp":
                return (x, y) -> Math.exp(arg.applyAsDouble(x, y));
            default:  // log, lg
                if (logBase == null) {
                    return (x, y) -> Math.log10(arg.applyAsDouble(x, y));
                }
                return (x, y) -> Math.log(arg.applyAsDouble(x, y)) / Math.log(logBase.applyAsDouble(x, y));
        }
    }

    private static boolean isConstant(DoubleBinaryOperator f, double value) {
        return f.applyAsDouble(0, 0) == value && f.applyAsDouble(1, 1) == value;
    }

    private static double nthRoot(double v, double n) {
        if (v < 0 && n == Math.rint(n) && ((long) n) % 2 != 0) {
            return -Math.pow(-v, 1.0 / n);
        }
        return Math.pow(v, 1.0 / n);
    }

    private static double parseNumber(String text) throws LatexParseException {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new LatexParseException("'" + text + "' is not a valid number.");
        }
    }
}
