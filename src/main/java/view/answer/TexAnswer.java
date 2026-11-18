package view.answer;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

// typeset answers: jlatexmath w/ kalam letters, fractions stacked, powers raised
final class TexAnswer {
    private static final float REF = 100f;  // size the eraser mask is drawn at
    private static final double AXIS = 0.25;  // tex math axis (fraction bar) above baseline, x size
    private static final int CACHE_SIZE = 64;
    private static final Map<String, TeXFormula> PARSED = cache();
    private static final Map<String, TeXIcon> ICONS = cache();
    private static final Map<String, Mask> MASKS = cache();
    private static String kalam;  // registered font name, "" if it didnt load
    private static double digitRatio;  // "0" height per point

    private TexAnswer() {
    }

    // tex size whose "0" is charHeight tall
    static synchronized float sizeFor(double charHeight) {
        if (digitRatio == 0) {
            digitRatio = icon("0", REF).getTrueIconHeight() / REF;
        }
        return (float) (charHeight / digitRatio);
    }

    // box at (0,0)
    static Rectangle2D bounds(String latex, float size) {
        TeXIcon icon = icon(latex, size);
        return new Rectangle2D.Double(0, 0, icon.getTrueIconWidth(), icon.getTrueIconHeight());
    }

    // box top to the fraction bar line, what lines up w/ the formula's middle
    static double middle(String latex, float size) {
        TeXIcon icon = icon(latex, size);
        return icon.getTrueIconHeight() - icon.getTrueIconDepth() - AXIS * size;
    }

    static void paint(Graphics2D world, CanvasAnswer a, Color color) {
        TeXIcon icon = icon(a.getLatex(), a.getFontSize());
        Graphics2D g = (Graphics2D) world.create();
        g.translate(a.getX(), a.getY());  // paintIcon only takes ints
        synchronized (TexAnswer.class) {
            icon.setForeground(color);
            icon.paintIcon(null, g, 0, 0);
        }
        g.dispose();
    }

    // eraser segment w/ this radius over actual ink, not just the box
    static boolean touches(CanvasAnswer a, double x1, double y1, double x2, double y2, double radius) {
        Rectangle2D box = a.getBox();
        box.setRect(box.getX() - radius, box.getY() - radius, box.getWidth() + 2 * radius,
                box.getHeight() + 2 * radius);
        if (!box.intersectsLine(x1, y1, x2, y2)) {
            return false;
        }
        Mask m = mask(a.getLatex());
        double k = REF / a.getFontSize();  // world -> mask px
        double ax = (x1 - a.getX()) * k;
        double ay = (y1 - a.getY()) * k;
        double bx = (x2 - a.getX()) * k;
        double by = (y2 - a.getY()) * k;
        double r = radius * k;
        int left = Math.max(0, (int) Math.floor(Math.min(ax, bx) - r));
        int right = Math.min(m.w - 1, (int) Math.ceil(Math.max(ax, bx) + r));
        int top = Math.max(0, (int) Math.floor(Math.min(ay, by) - r));
        int bottom = Math.min(m.h - 1, (int) Math.ceil(Math.max(ay, by) + r));
        for (int y = top; y <= bottom; y++) {
            for (int x = left; x <= right; x++) {
                if (m.on[y * m.w + x] && Line2D.ptSegDist(ax, ay, bx, by, x + 0.5, y + 0.5) <= r + 0.5) {
                    return true;
                }
            }
        }
        return false;
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

    private static synchronized Mask mask(String latex) {
        Mask m = MASKS.get(latex);
        if (m == null) {
            m = new Mask(icon(latex, REF));
            MASKS.put(latex, m);
        }
        return m;
    }

    private static <V> Map<String, V> cache() {
        return new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
                return size() > CACHE_SIZE;
            }
        };
    }

    // which px of the answer at REF size have ink
    private static final class Mask {
        final boolean[] on;
        final int w;
        final int h;

        Mask(TeXIcon icon) {
            w = Math.max(1, (int) Math.ceil(icon.getTrueIconWidth()));
            h = Math.max(1, (int) Math.ceil(icon.getTrueIconHeight()));
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            icon.setForeground(Color.WHITE);
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
            on = new boolean[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    on[y * w + x] = img.getRaster().getSample(x, y, 0) > 64;
                }
            }
        }
    }
}
