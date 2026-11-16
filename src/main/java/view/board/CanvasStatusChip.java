package view.board;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/** Compact visual save-state badge. No workspace/domain dependencies or file operations. */
final class CanvasStatusChip extends JComponent {
    private static final Color INK = new Color(32, 37, 47);
    private static final Color MUTED = new Color(112, 117, 128);
    private SaveMenuState state = SaveMenuState.newCanvas();

    CanvasStatusChip() { setOpaque(false); }

    void setState(SaveMenuState next) {
        state = java.util.Objects.requireNonNull(next);
        revalidate();
        repaint();
    }

    SaveMenuState state() { return state; }

    @Override public Dimension getPreferredSize() {
        var nameFont = BoardTheme.font(13.5f);
        var statusFont = BoardTheme.font(11.5f);
        FontMetrics name = getFontMetrics(nameFont);
        FontMetrics status = getFontMetrics(statusFont);
        String label = state.phase() == SaveMenuState.Phase.NEW ? "Not saved"
                : state.phase() == SaveMenuState.Phase.MODIFIED ? "Edited" : "Saved";
        int width = name.stringWidth(fittedName()) + status.stringWidth(label) + 46;
        return new Dimension(Math.min(230, Math.max(120, width)), 44);
    }

    private String fittedName() {
        String name = state.name();
        return name.length() <= 18 ? name : name.substring(0, 17) + "…";
    }

    @Override protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        RoundRectangle2D shape = new RoundRectangle2D.Double(1, 1, w - 2, h - 3, h - 3, h - 3);
        g.setColor(new Color(255, 255, 255, 238));g.fill(shape);
        g.setColor(new Color(215, 219, 228, 185));g.draw(shape);
        g.setFont(BoardTheme.font(13.5f));g.setColor(INK);
        FontMetrics nf = g.getFontMetrics();
        int baseline = (h + nf.getAscent() - nf.getDescent()) / 2;
        String name = fittedName();
        g.drawString(name, 15, baseline);
        int x = 15 + nf.stringWidth(name) + 12;
        if (state.phase() == SaveMenuState.Phase.MODIFIED) {
            g.setColor(new Color(255, 149, 0));g.fillOval(x, h / 2 - 3, 6, 6);
            x += 10;
        } else if (state.phase() == SaveMenuState.Phase.SAVED) {
            g.setColor(new Color(39, 150, 80));
            g.drawLine(x, h / 2, x + 3, h / 2 + 3);
            g.drawLine(x + 3, h / 2 + 3, x + 7, h / 2 - 4);
            x += 12;
        }
        g.setFont(BoardTheme.font(11.5f));g.setColor(MUTED);
        g.drawString(state.phase() == SaveMenuState.Phase.NEW ? "Not saved"
                : state.phase() == SaveMenuState.Phase.MODIFIED ? "Edited" : "Saved", x, baseline);
        g.dispose();
    }
}
