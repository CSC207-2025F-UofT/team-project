package entity.phase;

import entity.LatexFunctionParser;
import entity.LatexParseException;
import entity.SurfaceFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reads a handwritten ODE into a {@link DifferentialEquation}.
 *
 * <p>Derivatives: {@code y'}, {@code y''}, {@code y'(x)}, {@code \frac{dy}{dx}}, {@code \frac{d^2y}{dx^2}},
 * {@code dy/dx}, {@code \dot{x}}, {@code \ddot{x}}. Shapes:
 * <ul>
 *   <li>{@code y' = f(x, y)} -> slope field in the (x, y) plane</li>
 *   <li>{@code y'' = f(y, y')} -> phase plane (y, y')</li>
 *   <li>{@code x' = f(x, y)}, {@code y' = g(x, y)} (two lines, {@code \\} or {@code ;}) -> phase plane (x, y)</li>
 * </ul>
 */
public class DifferentialEquationParser {
    // stand-ins for names the expression parser can't take (\theta, y'), none of them in sin / exp / sqrt / pi ...
    private static final String FRESH = "uvwbkmjfzyd";
    private static final Set<String> GREEK = new HashSet<>(Arrays.asList(
            "\\alpha", "\\beta", "\\gamma", "\\delta", "\\epsilon", "\\varepsilon", "\\zeta", "\\eta",
            "\\theta", "\\vartheta", "\\kappa", "\\lambda", "\\mu", "\\nu", "\\xi", "\\rho", "\\sigma",
            "\\tau", "\\phi", "\\varphi", "\\chi", "\\psi", "\\omega"));

    private final LatexFunctionParser expressions;

    public DifferentialEquationParser() {
        this(new LatexFunctionParser());
    }

    public DifferentialEquationParser(LatexFunctionParser expressions) {
        this.expressions = expressions;
    }

    // popup check, cheap: every line is "derivative = something", nothing compiled yet
    public boolean looksLikeDifferentialEquation(String latex) {
        try {
            lines(latex);
            return true;
        } catch (LatexParseException e) {
            return false;
        }
    }

    /**
     * @throws LatexParseException if it isn't an ODE this can draw; the message says why
     */
    public DifferentialEquation parse(String latex) throws LatexParseException {
        List<Line> lines = lines(latex);
        synchronized (expressions) {   // that parser keeps its place in fields
            if (lines.size() == 1) {
                Line only = lines.get(0);
                if (only.derivative.order == 1) {
                    return slopeField(latex.trim(), only);
                }
                if (only.derivative.order == 2) {
                    return secondOrder(latex.trim(), only);
                }
                throw new LatexParseException("Only first- and second-order equations can be drawn.");
            }
            if (lines.size() == 2) {
                return system(latex.trim(), lines.get(0), lines.get(1));
            }
            throw new LatexParseException("Only one equation or a system of two can be drawn.");
        }
    }

    // ---------------------------------------------------------------- the three shapes

    // y' = f(x, y): arrows (1, f)
    private DifferentialEquation slopeField(String latex, Line line) throws LatexParseException {
        String dep = line.derivative.dependent;
        String indep = line.derivative.independent;
        if (indep == null) {
            indep = guessIndependent(dep, line.other);
        }
        if (indep.equals(dep)) {
            throw new LatexParseException("Differentiate with respect to another variable, e.g. dy/dx.");
        }
        Map<String, String> names = letters(Arrays.asList(indep, dep), List.of(line.other));
        SurfaceFunction f = field(rewrite(line.other, names, null, null), names.get(indep), names.get(dep),
                indep, dep, null);
        return new DifferentialEquation(latex, DifferentialEquation.Kind.SLOPE_FIELD, indep, dep,
                (h, v) -> 1, f::evaluate);
    }

    // y'' = f(y, y'): plane (y, y'), arrows (y', f)
    private DifferentialEquation secondOrder(String latex, Line line) throws LatexParseException {
        String dep = line.derivative.dependent;
        String indep = line.derivative.independent != null ? line.derivative.independent
                : "x".equals(dep) ? "t" : "x";
        String speed = dep + "'";
        Map<String, String> names = letters(Arrays.asList(dep, speed), List.of(line.other));
        SurfaceFunction f = field(rewrite(line.other, names, dep, names.get(speed)), names.get(dep),
                names.get(speed), dep, speed, indep);
        return new DifferentialEquation(latex, DifferentialEquation.Kind.PHASE_PLANE, dep, speed,
                (h, v) -> v, f::evaluate);
    }

    // x' = f(x, y), y' = g(x, y): arrows (f, g)
    private DifferentialEquation system(String latex, Line a, Line b) throws LatexParseException {
        if (a.derivative.order != 1 || b.derivative.order != 1) {
            throw new LatexParseException("A system needs two first-order equations, e.g. x' = y and y' = -x.");
        }
        if (a.derivative.dependent.equals(b.derivative.dependent)) {
            throw new LatexParseException("The two equations need different variables, e.g. x' = y and y' = -x.");
        }
        String sa = a.derivative.independent;
        String sb = b.derivative.independent;
        if (sa != null && sb != null && !sa.equals(sb)) {
            throw new LatexParseException("Both equations must use the same variable, e.g. dx/dt and dy/dt.");
        }
        String indep = sa != null ? sa : sb != null ? sb : "t";
        if ("y".equals(a.derivative.dependent) && "x".equals(b.derivative.dependent)) {
            Line swap = a;   // x goes across, y goes up
            a = b;
            b = swap;
        }
        String h = a.derivative.dependent;
        String v = b.derivative.dependent;
        Map<String, String> names = letters(Arrays.asList(h, v), List.of(a.other, b.other));
        SurfaceFunction f = field(rewrite(a.other, names, null, null), names.get(h), names.get(v), h, v, indep);
        SurfaceFunction g = field(rewrite(b.other, names, null, null), names.get(h), names.get(v), h, v, indep);
        return new DifferentialEquation(latex, DifferentialEquation.Kind.PHASE_PLANE, h, v,
                f::evaluate, g::evaluate);
    }

    // y' = t y -> t, else x (or t when it's x' = ...)
    private static String guessIndependent(String dep, List<String> expression) {
        Set<String> others = new LinkedHashSet<>();
        for (String t : expression) {
            if (isName(t) && !t.equals(dep) && !t.equals("e")) {
                others.add(t);
            }
        }
        if (others.size() == 1) {
            return others.iterator().next();
        }
        return "x".equals(dep) ? "t" : "x";
    }

    // compile, and say the unknown-letter error in the equation's own words
    private SurfaceFunction field(String expression, String first, String second,
                                  String firstName, String secondName, String banned) throws LatexParseException {
        try {
            return expressions.parseField(expression, first, second);
        } catch (LatexParseException e) {
            String msg = e.getMessage() == null ? "" : e.getMessage();
            if (!msg.startsWith("Unknown variable")) {
                throw e;
            }
            String letter = msg.substring(msg.indexOf('\'') + 1, msg.lastIndexOf('\''));
            if (letter.equals(banned)) {
                throw new LatexParseException("A phase portrait needs an autonomous equation: the right side can't use "
                        + display(banned) + ", only " + display(firstName) + " and " + display(secondName) + ".");
            }
            throw new LatexParseException("Unknown variable '" + letter + "': the right side can only use "
                    + display(firstName) + " and " + display(secondName) + ".");
        }
    }

    // ---------------------------------------------------------------- names -> letters

    // single letters stay, \theta / y' get a letter nobody's using
    private static Map<String, String> letters(List<String> names, List<List<String>> expressions) {
        Set<String> taken = new HashSet<>();
        for (List<String> e : expressions) {
            for (String t : e) {
                if (t.length() == 1 && Character.isLetter(t.charAt(0))) {
                    taken.add(t);
                }
            }
        }
        Map<String, String> map = new LinkedHashMap<>();
        for (String n : names) {
            if (n.length() == 1 && !n.equals("e")) {
                map.put(n, n);
                taken.add(n);
            }
        }
        for (String n : names) {
            if (map.containsKey(n)) {
                continue;
            }
            for (char c : FRESH.toCharArray()) {
                String f = String.valueOf(c);
                if (!taken.contains(f)) {
                    map.put(n, f);
                    taken.add(f);
                    break;
                }
            }
        }
        return map;
    }

    // tokens back to latex w/ the stand-in letters; derivativeOf' -> speedLetter
    private static String rewrite(List<String> expression, Map<String, String> names,
                                  String derivativeOf, String speedLetter) throws LatexParseException {
        StringBuilder out = new StringBuilder();
        boolean afterCommand = false;
        int i = 0;
        while (i < expression.size()) {
            String piece;
            Derivative d = derivativeAt(expression, i);
            if (d != null) {
                if (derivativeOf == null || d.order != 1 || !d.dependent.equals(derivativeOf)) {
                    throw new LatexParseException(derivativeOf == null
                            ? "The right side can't have a derivative in it."
                            : "The right side can only use " + display(derivativeOf) + " and "
                            + display(derivativeOf + "'") + ".");
                }
                piece = speedLetter;
                i = d.end;
            } else {
                String t = expression.get(i);
                piece = names.getOrDefault(t, t);
                i++;
            }
            if (afterCommand) {
                out.append(' ');   // \sin x, not \sinx
            }
            out.append(piece);
            afterCommand = piece.length() > 1 && piece.charAt(0) == '\\' && Character.isLetter(piece.charAt(1));
        }
        return out.toString();
    }

    // ---------------------------------------------------------------- lines + derivatives

    private static final class Derivative {
        final String dependent;
        final int order;
        final String independent;   // null = notation didn't say
        final int end;

        Derivative(String dependent, int order, String independent, int end) {
            this.dependent = dependent;
            this.order = order;
            this.independent = independent;
            this.end = end;
        }
    }

    // "derivative = other"
    private static final class Line {
        final Derivative derivative;
        final List<String> other;

        Line(Derivative derivative, List<String> other) {
            this.derivative = derivative;
            this.other = other;
        }
    }

    private static List<Line> lines(String latex) throws LatexParseException {
        if (latex == null || latex.trim().isEmpty()) {
            throw new LatexParseException("The expression is empty.");
        }
        List<Line> out = new ArrayList<>();
        for (List<String> raw : splitLines(tokenize(latex))) {
            if (!raw.isEmpty()) {
                out.add(line(raw));
            }
        }
        if (out.isEmpty()) {
            throw new LatexParseException("The expression is empty.");
        }
        return out;
    }

    private static Line line(List<String> tokens) throws LatexParseException {
        int eq = -1;
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("=")) {
                if (eq >= 0) {
                    throw new LatexParseException("Each equation needs exactly one '='.");
                }
                eq = i;
            }
        }
        if (eq <= 0 || eq == tokens.size() - 1) {
            throw new LatexParseException("Write the equation in full, e.g. y' = x - y.");
        }
        List<String> left = new ArrayList<>(tokens.subList(0, eq));
        List<String> right = new ArrayList<>(tokens.subList(eq + 1, tokens.size()));
        Derivative l = whole(left);
        Derivative r = whole(right);
        if (l != null && r == null) {
            return new Line(l, right);
        }
        if (r != null && l == null) {
            return new Line(r, left);
        }
        throw new LatexParseException(l == null
                ? "No derivative to draw, e.g. y' = x - y."
                : "Put the derivative on one side, e.g. y' = x - y.");
    }

    // the whole side is one derivative, or null
    private static Derivative whole(List<String> side) {
        Derivative d = derivativeAt(side, 0);
        return d != null && d.end == side.size() && d.order >= 1 ? d : null;
    }

    // a derivative starting at i, or null
    private static Derivative derivativeAt(List<String> t, int i) {
        if (i >= t.size()) {
            return null;
        }
        String first = t.get(i);
        if (isName(first) && at(t, i + 1, "'")) {
            int j = i + 1;
            int order = 0;
            while (at(t, j, "'")) {
                order++;
                j++;
            }
            String indep = null;
            if (at(t, j, "(") && j + 2 < t.size() && isName(t.get(j + 1)) && at(t, j + 2, ")")) {
                indep = t.get(j + 1);   // y'(x)
                j += 3;
            }
            return new Derivative(first, order, indep, j);
        }
        if (first.equals("\\dot") || first.equals("\\ddot")) {
            int order = first.equals("\\dot") ? 1 : 2;
            if (at(t, i + 1, "{") && i + 2 < t.size() && isName(t.get(i + 2)) && at(t, i + 3, "}")) {
                return new Derivative(t.get(i + 2), order, "t", i + 4);
            }
            if (i + 1 < t.size() && isName(t.get(i + 1))) {
                return new Derivative(t.get(i + 1), order, "t", i + 2);
            }
            return null;
        }
        if (first.equals("\\frac")) {
            return fraction(t, i);
        }
        // dy/dx written inline
        if (first.equals("d") && i + 4 < t.size() && isName(t.get(i + 1)) && at(t, i + 2, "/")
                && at(t, i + 3, "d") && isName(t.get(i + 4))) {
            return new Derivative(t.get(i + 1), 1, t.get(i + 4), i + 5);
        }
        return null;
    }

    // \frac{dy}{dx}, \frac{d^2y}{dx^2}, \frac{d}{dx}y
    private static Derivative fraction(List<String> t, int i) {
        int j = i + 1;
        if (!at(t, j, "{") || !at(t, j + 1, "d")) {
            return null;
        }
        int[] top = {1};
        j = power(t, j + 2, top);
        if (j < 0) {
            return null;
        }
        String dep = null;
        if (j < t.size() && isName(t.get(j))) {
            dep = t.get(j);
            j++;
        }
        if (!at(t, j, "}") || !at(t, j + 1, "{") || !at(t, j + 2, "d") || j + 3 >= t.size()
                || !isName(t.get(j + 3))) {
            return null;
        }
        String indep = t.get(j + 3);
        int[] bottom = {1};
        j = power(t, j + 4, bottom);
        if (j < 0 || !at(t, j, "}") || top[0] != bottom[0]) {
            return null;
        }
        j++;
        if (dep == null) {
            if (j >= t.size() || !isName(t.get(j))) {
                return null;
            }
            dep = t.get(j);
            j++;
        }
        return new Derivative(dep, top[0], indep, j);
    }

    // optional ^2 / ^{2}, writes the order, -1 if it's not a plain whole number
    private static int power(List<String> t, int j, int[] order) {
        if (!at(t, j, "^")) {
            return j;
        }
        String n;
        int next;
        if (at(t, j + 1, "{") && at(t, j + 3, "}")) {
            n = t.get(j + 2);
            next = j + 4;
        } else if (j + 1 < t.size()) {
            n = t.get(j + 1);
            next = j + 2;
        } else {
            return -1;
        }
        try {
            order[0] = Integer.parseInt(n);
            return next;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean at(List<String> t, int i, String s) {
        return i >= 0 && i < t.size() && t.get(i).equals(s);
    }

    static boolean isName(String t) {
        return (t.length() == 1 && ((t.charAt(0) >= 'a' && t.charAt(0) <= 'z')
                || (t.charAt(0) >= 'A' && t.charAt(0) <= 'Z'))) || GREEK.contains(t);
    }

    // ---------------------------------------------------------------- tokens

    // \\ or ; between equations, or "x'=y, y'=-x" on one line
    private static List<List<String>> splitLines(List<String> tokens) {
        List<List<String>> out = new ArrayList<>();
        List<String> current = new ArrayList<>();
        int depth = 0;
        for (String t : tokens) {
            if (t.equals("(") || t.equals("[") || t.equals("{")) {
                depth++;
            } else if (t.equals(")") || t.equals("]") || t.equals("}")) {
                depth--;
            }
            if (depth == 0 && (t.equals("\\\\") || t.equals(";"))) {
                out.addAll(commaSplit(current));
                current = new ArrayList<>();
            } else {
                current.add(t);
            }
        }
        out.addAll(commaSplit(current));
        return out;
    }

    private static List<List<String>> commaSplit(List<String> line) {
        int equals = 0;
        for (String t : line) {
            if (t.equals("=")) {
                equals++;
            }
        }
        if (equals < 2) {
            return List.of(line);
        }
        List<List<String>> out = new ArrayList<>();
        List<String> current = new ArrayList<>();
        int depth = 0;
        for (String t : line) {
            if (t.equals("(") || t.equals("[") || t.equals("{")) {
                depth++;
            } else if (t.equals(")") || t.equals("]") || t.equals("}")) {
                depth--;
            }
            if (depth == 0 && t.equals(",")) {
                out.add(current);
                current = new ArrayList<>();
            } else {
                current.add(t);
            }
        }
        out.add(current);
        return out;
    }

    private static List<String> tokenize(String s) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            char c = s.charAt(i);
            if (Character.isWhitespace(c) || c == '$') {
                i++;
            } else if (c == '\\') {
                int j = i + 1;
                while (j < s.length() && Character.isLetter(s.charAt(j))) {
                    j++;
                }
                if (j == i + 1 && j < s.length()) {
                    j++;   // \\ \{ \, ...
                }
                out.add(s.substring(i, j));
                i = j;
            } else if (Character.isDigit(c) || c == '.') {
                int j = i;
                while (j < s.length() && (Character.isDigit(s.charAt(j)) || s.charAt(j) == '.')) {
                    j++;
                }
                out.add(s.substring(i, j));
                i = j;
            } else if (c == '′' || c == '’') {
                out.add("'");
                i++;
            } else if (c == '″' || c == '"') {
                out.add("'");
                out.add("'");
                i++;
            } else if (c == '−') {
                out.add("-");
                i++;
            } else {
                out.add(String.valueOf(c));
                i++;
            }
        }
        return foldPrimes(normalize(out));
    }

    // drop layout (\left, \begin{cases}, &, \{), \prime -> '
    private static List<String> normalize(List<String> in) {
        List<String> out = new ArrayList<>();
        for (int i = 0; i < in.size(); i++) {
            String t = in.get(i);
            switch (t) {
                case "\\left":
                case "\\right":
                    if (at(in, i + 1, ".")) {
                        i++;
                    }
                    break;
                case "\\,":
                case "\\;":
                case "\\!":
                case "\\:":
                case "\\ ":
                case "\\{":
                case "\\}":
                case "&":
                    break;
                case "\\prime":
                    out.add("'");
                    break;
                case "\\begin":
                case "\\end":
                    int end = skipGroup(in, i + 1);
                    String env = end > i + 1 ? String.join("", in.subList(i + 2, end)) : "";
                    i = end;
                    if (t.equals("\\begin") && env.equals("array")) {
                        i = skipGroup(in, i + 1);   // the {ll} column spec
                    }
                    break;
                default:
                    out.add(t);
            }
        }
        return out;
    }

    // index of the } closing a {...} at i, or i - 1 when there's no group
    private static int skipGroup(List<String> in, int i) {
        if (!at(in, i, "{")) {
            return i - 1;
        }
        int depth = 0;
        for (int j = i; j < in.size(); j++) {
            if (in.get(j).equals("{")) {
                depth++;
            } else if (in.get(j).equals("}") && --depth == 0) {
                return j;
            }
        }
        return in.size() - 1;
    }

    // y^{'} / y^{''} / y^' (the recogniser writes these) -> y' y''
    private static List<String> foldPrimes(List<String> in) {
        List<String> out = new ArrayList<>();
        int i = 0;
        while (i < in.size()) {
            if (in.get(i).equals("^") && i + 1 < in.size()) {
                if (in.get(i + 1).equals("'")) {
                    i++;
                    continue;
                }
                int j = i + 2;
                while (j < in.size() && in.get(j).equals("'")) {
                    j++;
                }
                if (in.get(i + 1).equals("{") && j > i + 2 && at(in, j, "}")) {
                    for (int k = i + 2; k < j; k++) {
                        out.add("'");
                    }
                    i = j + 1;
                    continue;
                }
            }
            out.add(in.get(i));
            i++;
        }
        return out;
    }

    // \theta -> θ for labels + messages
    public static String display(String name) {
        Map<String, String> greek = GREEK_GLYPHS;
        StringBuilder out = new StringBuilder();
        String base = name;
        int primes = 0;
        while (base.endsWith("'")) {
            base = base.substring(0, base.length() - 1);
            primes++;
        }
        out.append(greek.getOrDefault(base, base));
        for (int k = 0; k < primes; k++) {
            out.append('′');
        }
        return out.toString();
    }

    private static final Map<String, String> GREEK_GLYPHS = new HashMap<>();

    static {
        String[] names = {"alpha", "beta", "gamma", "delta", "epsilon", "varepsilon", "zeta", "eta", "theta",
            "vartheta", "kappa", "lambda", "mu", "nu", "xi", "rho", "sigma", "tau", "phi", "varphi", "chi",
            "psi", "omega"};
        String glyphs = "αβγδεεζηθϑκλμνξρστφφχψω";
        for (int k = 0; k < names.length; k++) {
            GREEK_GLYPHS.put("\\" + names[k], String.valueOf(glyphs.charAt(k)));
        }
    }
}
