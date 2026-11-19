package view.board;

import entity.EquationDisplay;
import interface_adapter.history.HistoryItem;
import view.answer.AnswerFont;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Rendering policy for History entries. The implication is part of the very
 * same JLaTeXMath expression used by the PDF Library, not a hand-drawn glyph
 * or a font-dependent Unicode arrow. This keeps the two panels consistent.
 */
final class HistoryAnswerArtwork {
    private HistoryAnswerArtwork() { }

    /** Reuse TeX glyph layout across row repaints, width probes and drag snapshots. */
    private static final Map<HistoryItem, Map<Float, TeXIcon>> ANSWER_CACHE =
            Collections.synchronizedMap(new WeakHashMap<>());

    static boolean implication(HistoryItem item) {
        return EquationDisplay.isTransformation(item.getLatex(), item.getAnswer());
    }

    /** Formula and (when applicable) the implication, typeset together. */
    static String formulaLatex(HistoryItem item) {
        String formula = EquationDisplay.formula(item.getLatex(), item.getAnswer());
        return implication(item) ? formula + EquationDisplay.latexSeparator(item.getLatex(), item.getAnswer()) : formula;
    }

    static String answerText(HistoryItem item) {
        String answer = EquationDisplay.answer(item.getAnswer());
        if (answer.isEmpty()) return "";
        return implication(item) ? answer : "= " + answer;
    }

    /** JLaTeXMath renders fractions, roots and exponents; ordinary messages use the text fallback. */
    static TeXIcon typesetAnswer(HistoryItem item, float fontSize) {
        String text = answerText(item);
        if (text.isBlank()) return null;
        // Explanatory prose should not silently lose spaces in TeX math mode.
        if (text.matches(".*[a-zA-Z]{2,}\\s+[a-zA-Z]{2,}.*") && !text.contains("\\")) return null;
        synchronized (ANSWER_CACHE) {
            Map<Float, TeXIcon> sizes = ANSWER_CACHE.computeIfAbsent(item, key -> new HashMap<>());
            TeXIcon existing = sizes.get(fontSize);
            if (existing != null) return existing;
            try {
                TeXIcon icon = new TeXFormula(EquationDisplay.typeset(text))
                        .createTeXIcon(TeXConstants.STYLE_DISPLAY, fontSize);
                sizes.put(fontSize, icon);
                return icon;
            } catch (RuntimeException | Error invalid) {
                return null;
            }
        }
    }

    static double width(HistoryItem item, float fontSize) {
        String text = answerText(item);
        if (text.isEmpty()) return 0;
        TeXIcon math = typesetAnswer(item, fontSize);
        return math == null ? AnswerFont.visualBounds(text, fontSize).getWidth() : math.getIconWidth();
    }

    static double height(HistoryItem item, float fontSize) {
        String text = answerText(item);
        if (text.isEmpty()) return 0;
        TeXIcon math = typesetAnswer(item, fontSize);
        return math == null ? AnswerFont.visualBounds(text, fontSize).getHeight() : math.getIconHeight();
    }

    /** Draw math as math, not a literal \frac string in a normal font. */
    static void paint(Graphics2D g, HistoryItem item, float fontSize, double left, double centerY) {
        String text = answerText(item);
        if (text.isEmpty()) return;
        TeXIcon math = typesetAnswer(item, fontSize);
        if (math != null) {
            math.setForeground(java.awt.Color.BLACK);
            math.paintIcon(null, g, (int) Math.round(left),
                    (int) Math.round(centerY - math.getIconHeight() / 2.0));
        } else {
            Rectangle2D bounds = AnswerFont.visualBounds(text, fontSize);
            AnswerFont.draw(g, text, fontSize, left, centerY - bounds.getHeight() / 2);
        }
    }
}
