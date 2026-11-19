package view.graph;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;

import javax.swing.JLabel;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;

// the function name in the header, typeset with jlatexmath, plain text if latex won't parse
final class GraphTitle {
    private static final JLabel HOST = new JLabel();
    private static final int CACHE = 16;
    private static final Map<String, TeXIcon> ICONS = new LinkedHashMap<>(CACHE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, TeXIcon> eldest) {
            return size() > CACHE;
        }
    };

    private GraphTitle() {
    }

    // left edge x, centred on centreY, shrinks to fit maxWidth
    static void paint(Graphics2D g, String latex, double x, double centreY, double maxWidth, float size) {
        TeXIcon icon = icon(latex, size);
        if (icon == null) {
            g.setFont(GraphTheme.font(size));
            g.setColor(GraphTheme.TITLE);
            FontMetrics m = g.getFontMetrics();
            g.drawString(latex, (float) x, (float) (centreY + m.getAscent() / 2.0 - 2));
            return;
        }
        Graphics2D t = (Graphics2D) g.create();
        try {
            double fit = Math.min(1, maxWidth / Math.max(1, icon.getIconWidth()));
            t.translate(x, centreY - icon.getIconHeight() * fit / 2);
            t.scale(fit, fit);
            synchronized (HOST) {
                icon.paintIcon(HOST, t, 0, 0);
            }
        } finally {
            t.dispose();
        }
    }

    // natural width before any shrink-to-fit
    static double width(String latex, float size) {
        TeXIcon icon = icon(latex, size);
        if (icon != null) {
            return icon.getIconWidth();
        }
        synchronized (HOST) {
            return HOST.getFontMetrics(GraphTheme.font(size)).stringWidth(latex);
        }
    }

    private static TeXIcon icon(String latex, float size) {
        String key = Math.round(size * 4) + "|" + latex;
        synchronized (ICONS) {
            if (ICONS.containsKey(key)) {
                return ICONS.get(key);
            }
            TeXIcon icon;
            try {
                icon = new TeXFormula(latex).createTeXIcon(TeXConstants.STYLE_DISPLAY, size);
                icon.setForeground(GraphTheme.TITLE);
            } catch (RuntimeException | Error e) {
                icon = null;   // bad latex, header falls back to text
            }
            ICONS.put(key, icon);
            return icon;
        }
    }
}
