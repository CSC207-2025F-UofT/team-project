package view.answer;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;

// tex layout for answers: jlatexmath w/ kalam letters; AnswerInk traces it into pen lines
final class TexAnswer {
    private static final double AXIS = 0.25;  // tex math axis (fraction bar) above baseline, x size
    private static final int CACHE_SIZE = 64;
    private static final Map<String, TeXFormula> PARSED = cache();
    private static final Map<String, TeXIcon> ICONS = cache();
    private static String kalam;  // registered font name, "" if it didnt load

    private TexAnswer() {
    }

    // box at (0,0)
    static Rectangle2D bounds(String latex, float size) {
        TeXIcon icon = icon(latex, size);
        return new Rectangle2D.Double(0, 0, icon.getTrueIconWidth(), icon.getTrueIconHeight());
    }

    // box top down to the fraction bar line
    static double axis(String latex, float size) {
        TeXIcon icon = icon(latex, size);
        return icon.getTrueIconHeight() - icon.getTrueIconDepth() - AXIS * size;
    }

    // filled glyphs w/ the box top-left at (0,0)
    static synchronized void paint(Graphics2D g, String latex, float size, Color color) {
        TeXIcon icon = icon(latex, size);
        icon.setForeground(color);
        icon.paintIcon(null, g, 0, 0);
    }

    private static synchronized TeXIcon icon(String latex, float size) {
        String key = size + "|" + latex;
        TeXIcon icon = ICONS.get(key);
        if (icon == null) {
            icon = parsed(latex).createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
            icon.setInsets(new Insets(0, 0, 0, 0), true);  // false would pad + mutate the insets passed in
            ICONS.put(key, icon);
        }
        return icon;
    }

    // font is picked at parse time, so kalam is only on while parsing; pdf + history keep the tex font
    private static TeXFormula parsed(String latex) {
        TeXFormula formula = PARSED.get(latex);
        if (formula != null) {
            return formula;
        }
        synchronized (TeXFormula.class) {
            boolean on = !kalam().isEmpty();
            if (on) {
                TeXFormula.registerExternalFont(Character.UnicodeBlock.BASIC_LATIN, kalam);
            }
            try {
                formula = new TeXFormula(latex);
            } finally {
                if (on) {
                    TeXFormula.externalFontMap.remove(Character.UnicodeBlock.BASIC_LATIN);
                }
            }
        }
        PARSED.put(latex, formula);
        return formula;
    }

    // kalam has to be a known font name for jlatexmath to find it
    private static String kalam() {
        if (kalam == null) {
            Font base = AnswerFont.base();
            boolean loaded = base.getFamily().toLowerCase().contains("kalam");  // else its the ui fallback
            if (loaded) {
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(base);  // false = already known, fine
            }
            kalam = loaded ? base.getFontName() : "";
        }
        return kalam;
    }

    private static <V> Map<String, V> cache() {
        return new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
                return size() > CACHE_SIZE;
            }
        };
    }
}
