package view.board;

import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Shared view-only surface for PDF object and material-library context actions. */
public final class GlassContextMenu extends JPopupMenu {
    private static final int RADIUS = 17;
    public GlassContextMenu() {
        setUI(new BasicPopupMenuUI());
        setOpaque(false);
        setLightWeightPopupEnabled(true);
        setBackground(new Color(0, 0, 0, 0));
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
    }

    public JMenuItem addAction(String text, Runnable action) {
        JMenuItem item = new GlassAction(text);
        item.addActionListener(e -> action.run());
        add(item);
        return item;
    }

    /** Style a nested page-destination submenu without changing its commands. */
    public void addSubmenu(JMenu menu) {
        menu.setOpaque(false);
        menu.setFont(BoardTheme.font(12.5f));
        menu.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        JPopupMenu child = menu.getPopupMenu();
        child.setUI(new BasicPopupMenuUI());
        child.setOpaque(false);
        child.setBackground(new Color(0, 0, 0, 0));
        child.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        add(menu);
    }

    @Override protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = Math.max(1, getWidth()-1), h = Math.max(1,getHeight()-1);
        g.setPaint(new GradientPaint(0, 0, new Color(255,255,255,248),
                0, h, new Color(241,245,252,245)));
        g.fillRoundRect(0, 0, w, h, RADIUS, RADIUS);
        g.setColor(new Color(188,199,216,180));
        g.setStroke(new BasicStroke(1f));
        g.drawRoundRect(0, 0, w, h, RADIUS, RADIUS);
        g.dispose();
    }

    private static final class GlassAction extends JMenuItem {
        GlassAction(String title) {
            super(title);
            setFont(BoardTheme.font(12.5f));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 15));
            setPreferredSize(new Dimension(Math.max(190, getFontMetrics(getFont()).stringWidth(title)+34), 35));
        }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (getModel().isArmed() || getModel().isSelected()) {
                g.setColor(new Color(226,238,254));
                g.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 11, 11);
            }
            g.setColor(isEnabled() ? new Color(35,43,56) : new Color(145,151,161));
            g.setFont(getFont());
            FontMetrics fm=g.getFontMetrics();
            g.drawString(getText(), 12, (getHeight()+fm.getAscent()-fm.getDescent())/2);
            g.dispose();
        }
    }
}
