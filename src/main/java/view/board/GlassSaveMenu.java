package view.board;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.Objects;

/**
 * Small liquid-glass menu for canvas persistence. The caller provides save callbacks and state;
 * the menu neither mutates the canvas nor accesses storage. No shortcut/icon columns are reserved.
 */
final class GlassSaveMenu extends JPopupMenu {
    private static final Color INK = new Color(28, 35, 47);
    private static final Color MUTED = new Color(99, 105, 117);
    private static final int INNER_GAP = 4;
    private final JPanel rows;

    GlassSaveMenu(SaveMenuState state, Runnable save, Runnable saveAs) {
        Objects.requireNonNull(state);
        Objects.requireNonNull(save);
        Objects.requireNonNull(saveAs);
        setUI(new BasicPopupMenuUI());
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setLightWeightPopupEnabled(true);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 8, 5));
        rows = new JPanel(null) {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = (Graphics2D) graphics.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                RoundRectangle2D border = new RoundRectangle2D.Double(.5, .5,
                        getWidth() - 1.0, getHeight() - 1.0, 19, 19);
                g.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 253),
                        0, getHeight(), new Color(246, 248, 253, 248)));
                g.fill(border);
                g.setStroke(new BasicStroke(1f));
                g.setColor(new Color(198, 205, 216, 145));
                g.draw(border);
                g.dispose();
            }
        };
        rows.setOpaque(false);
        if (state.phase() == SaveMenuState.Phase.NEW) {
            addRow("Save New Canvas…", "Give this canvas a name", "save-current-canvas", save);
        } else {
            if (state.canSaveInPlace()) {
                addRow("Save to “" + state.compactName() + "”", "Update the saved version", "save-current-canvas", save);
            }
            addRow("Save as New Canvas…", "Keep the original and save a copy", "save-as-new-canvas", saveAs);
        }
        rows.setPreferredSize(new Dimension(menuInnerWidth(), menuInnerHeight()));
        add(rows);
    }

    private void addRow(String label, String description, String name, Runnable action) {
        rows.add(new MenuAction(label, description, name, () -> {
            setVisible(false);
            action.run();
        }));
    }

    private int menuInnerWidth() {
        int width = 0;
        for (var child : rows.getComponents()) {
            width = Math.max(width, child.getPreferredSize().width);
        }
        return width + 2 * INNER_GAP + 10;
    }

    private int menuInnerHeight() {
        return rows.getComponentCount() * 58 + (rows.getComponentCount() - 1) * 1 + INNER_GAP * 2;
    }

    @Override public void doLayout() {
        super.doLayout();
        layoutRows();
    }

    private void layoutRows() {
        int y = INNER_GAP;
        for (var component : rows.getComponents()) {
            component.setBounds(INNER_GAP, y, rows.getWidth() - INNER_GAP * 2, 58);
            y += 59;
        }
    }

    @Override public Dimension getPreferredSize() {
        Dimension inner = rows.getPreferredSize();
        return new Dimension(inner.width + 10, inner.height + 13);
    }

    /** Custom-drawn JButton avoids the default platform JMenuItem icon/shortcut gutters. */
    static final class MenuAction extends JButton {
        private final String description;
        MenuAction(String title, String description, String name, Runnable action) {
            super(title);
            this.description = description;
            setName(name);
            setFont(BoardTheme.font(13.5f));
            setFocusable(false);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setBorder(BorderFactory.createEmptyBorder());
            setRolloverEnabled(true);
            addActionListener(e -> action.run());
        }
        String getDescription() { return description; }
        @Override public Dimension getPreferredSize() {
            FontMetrics titleMetrics = getFontMetrics(getFont());
            Font small = BoardTheme.font(11.3f);
            FontMetrics detailMetrics = getFontMetrics(small);
            return new Dimension(Math.max(170, Math.max(titleMetrics.stringWidth(getText()),
                    detailMetrics.stringWidth(description)) + 32), 58);
        }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            if (getModel().isPressed() || getModel().isRollover()) {
                g.setColor(getModel().isPressed() ? new Color(213, 229, 251)
                        : new Color(234, 242, 255));
                g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 12, 12);
            }
            g.setFont(getFont());
            g.setColor(INK);
            g.drawString(getText(), 14, 25);
            g.setFont(BoardTheme.font(11.3f));
            g.setColor(MUTED);
            g.drawString(description, 14, 42);
            g.dispose();
        }
    }
}
