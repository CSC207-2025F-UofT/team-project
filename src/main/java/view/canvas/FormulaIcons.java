package view.canvas;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Insets;

// latex -> small inline picture for the notice cards
final class FormulaIcons {
    private static final float SIZE = 16f;
    private static final int TEXT_DROP = 5;  // card text baseline sits this far under the middle
    private static final Color INK = new Color(28, 28, 30);

    private FormulaIcons() {
    }

    // null if it won't typeset, caller falls back to the raw text
    static Icon inline(String latex) {
        if (latex == null || latex.isBlank()) {
            return null;
        }
        try {
            TeXIcon icon = new TeXFormula(latex).createTeXIcon(TeXConstants.STYLE_DISPLAY, SIZE);  // readable ½
            icon.setForeground(INK);
            lineUp(icon);
            return icon;
        } catch (RuntimeException | Error bad) {
            return null;
        }
    }

    // pad top or bottom so the card can just centre it and the baselines still match
    private static void lineUp(TeXIcon icon) {
        int h = icon.getIconHeight();
        int baseline = h - icon.getIconDepth();
        int shift = 2 * TEXT_DROP - 2 * baseline + h;  // top pad minus bottom pad
        icon.setInsets(new Insets(Math.max(0, shift), 0, Math.max(0, -shift), 0));
    }
}
