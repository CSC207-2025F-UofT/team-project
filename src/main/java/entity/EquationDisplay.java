package entity;

import java.util.regex.Pattern;

/** Domain-independent display policy shared by History, Library and PDF rendering. */
public final class EquationDisplay {
    // A plain ( or [ around a plain matrix stays text-sized; \left/\right make JLaTeXMath stretch it.
    private static final Pattern PLAIN_PAREN_MATRIX = Pattern.compile(
            "(?<!\\\\left)\\((\\s*\\\\begin\\{matrix\\}.*?\\\\end\\{matrix\\}\\s*)\\)", Pattern.DOTALL);
    private static final Pattern PLAIN_BRACKET_MATRIX = Pattern.compile(
            "(?<!\\\\left)\\[(\\s*\\\\begin\\{matrix\\}.*?\\\\end\\{matrix\\}\\s*)\\]", Pattern.DOTALL);

    private EquationDisplay() { }

    /** LaTeX to hand to JLaTeXMath: stretches plain brackets that wrap a plain matrix. */
    public static String typeset(String latex) {
        if (latex == null) return null;
        String sized = PLAIN_PAREN_MATRIX.matcher(latex).replaceAll("\\\\left($1\\\\right)");
        return PLAIN_BRACKET_MATRIX.matcher(sized).replaceAll("\\\\left[$1\\\\right]");
    }

    /** A solution relating two equations is a transformation, not a new equality. */
    public static boolean isTransformation(String source, String result) {
        String left = source == null ? "" : source.trim().replaceFirst("\\s*=+\\s*$", "");
        String right = answer(result);
        return left.contains("=") && right.contains("=");
    }

    public static String formula(String source, String result) {
        String text = source == null ? "" : source.trim();
        if (result != null && !result.isBlank()) return text.replaceFirst("\\s*=+\\s*$", "").trim();
        return text;
    }

    public static String answer(String source) {
        return source == null ? "" : source.trim().replaceFirst("^\\s*=+\\s*", "").trim();
    }

    public static String separator(String source, String result) {
        return isTransformation(source, result) ? " ⇒ " : " = ";
    }

    /** JLaTeXMath rendering uses a LaTeX command for the implication symbol. */
    public static String latexSeparator(String source, String result) {
        return isTransformation(source, result) ? "\\Rightarrow" : "=";
    }

    public static String equation(String source, String result) {
        String right = answer(result);
        String left = formula(source, result);
        if (right.isBlank()) return left;
        return left.isBlank() ? "= " + right : left + separator(source, result) + right;
    }
}
