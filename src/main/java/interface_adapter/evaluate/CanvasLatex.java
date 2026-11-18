package interface_adapter.evaluate;

import interface_adapter.AnswerKind;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// answer text -> tex for the canvas: p/q stacked, ×10^n raised, words upright
final class CanvasLatex {
    private static final Pattern PIECE = Pattern.compile(
            "(?<frac>\\d+)/(?<den>\\d+)"
            + "|×10\\^(?<exp>-?\\d+)"
            + "|_(?<sub>\\d{2,})"
            + "|(?<more> \\(and \\d+ more\\))"
            + "|\\b(?<join>or|and)\\b"
            + "|\\b(?<word>true for all |no solution|undefined|true|false)"
            + "|(?<sym>[∞≤≥≠{}])");

    private CanvasLatex() {
    }

    // formatter text (numbers, words, ≤...) -> tex
    static String ofText(AnswerKind kind, String result) {
        Matcher m = PIECE.matcher(ofLatex(kind, result));
        StringBuilder out = new StringBuilder();
        while (m.find()) {
            m.appendReplacement(out, Matcher.quoteReplacement(tex(m)));
        }
        m.appendTail(out);
        return out.toString();
    }

    // already tex, just drop the "= " when it goes after the user's =
    static String ofLatex(AnswerKind kind, String result) {
        return kind == AnswerKind.AFTER_EQUALS && result.startsWith("= ") ? result.substring(2) : result;
    }

    private static String tex(Matcher m) {
        if (m.group("frac") != null) {
            return "\\frac{" + m.group("frac") + "}{" + m.group("den") + "}";
        }
        if (m.group("exp") != null) {
            return "\\times 10^{" + m.group("exp") + "}";
        }
        if (m.group("sub") != null) {
            return "_{" + m.group("sub") + "}";
        }
        if (m.group("more") != null) {
            return "\\text{" + m.group("more") + "}";
        }
        if (m.group("join") != null) {
            return "\\text{ " + m.group("join") + " }";
        }
        if (m.group("word") != null) {
            return "\\text{" + m.group("word") + "}";
        }
        return symbol(m.group("sym"));
    }

    private static String symbol(String s) {
        switch (s) {
            case "∞":
                return "\\infty ";
            case "≤":
                return "\\le ";
            case "≥":
                return "\\ge ";
            case "≠":
                return "\\ne ";
            default:
                return "\\" + s;  // set braces
        }
    }
}
