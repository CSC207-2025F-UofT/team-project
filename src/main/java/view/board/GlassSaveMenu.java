package view.board;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

/** Compact, icon-gutter-free glass menu for the two canvas save commands. View layer only. */
final class GlassSaveMenu extends JPopupMenu {
    private static final int WIDTH = 198;
    private static final Color INK = new Color(35, 42, 54);

    GlassSaveMenu(Runnable save, Runnable saveAs) {
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 7, 5));
        JPanel rows = new JPanel(new GridLayout(2, 1, 0, 2)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                RoundRectangle2D shell = new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 18, 18);
                g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 253),
                        0, getHeight(), new Color(244, 248, 253, 246)));
                g2.fill(shell);
                g2.setColor(new Color(208, 215, 225, 135));
                g2.setStroke(new BasicStroke(1f));
                g2.draw(shell);
                g2.dispose();
            }
        };
        rows.setOpaque(false);
        rows.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rows.add(new MenuAction("Save current canvas", "save-current-canvas", () -> {
            setVisible(false);
            save.run();
        }));
        rows.add(new MenuAction("Save As…", "save-as-new-canvas", () -> {
            setVisible(false);
            saveAs.run();
        }));
        add(rows);
    }

    /** A JButton rather than a JMenuItem removes the OS Look-and-Feel icon gutter. */
    static final class MenuAction extends JButton {
        MenuAction(String text, String name, Runnable action) {
            super(text);
            setName(name);
            setFont(BoardTheme.font(13f));
            setFocusable(false);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
            setRolloverEnabled(true);
            addActionListener(e -> action.run());
        }
        @Override public Dimension getPreferredSize() { return new Dimension(WIDTH - 22, 34); }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover() || getModel().isPressed()) {
                g.setColor(getModel().isPressed() ? new Color(206, 224, 248) : new Color(233, 241, 253));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 11, 11);
            }
            g.setColor(INK);
            g.setFont(getFont());
            FontMetrics fm = g.getFontMetrics();
            g.drawString(getText(), 11, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            g.dispose();
        }
    }
}
