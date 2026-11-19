package view.canvas;

import view.graph.PlotCard;
import view.stack.Fonts;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.geom.RoundRectangle2D;

// x / y range boxes. They sit in the graph card's header beside the function, sized to it;
// on a card too narrow for that they hang under the card on their own little panel instead.
final class GraphRangeBar extends JPanel {
    private static final Color BORDER = new Color(203, 213, 225);
    private static final Color FIELD_BORDER = new Color(214, 216, 224);
    private static final Color ACCENT = new Color(0, 122, 255);
    private static final Color LABEL = new Color(51, 65, 85);
    private static final Color PLACEHOLDER = new Color(148, 163, 184);
    private static final Color TEXT = new Color(15, 23, 42);
    // below this header scale (canvas zoomed far out) the boxes' text gets too small to type in
    private static final float MIN_HEADER_SCALE = 0.8f;

    private final RangeField xRange = new RangeField("-10, 10");
    private final RangeField yRange = new RangeField("auto");
    private final JLabel xLabel = axis("x");
    private final JLabel yLabel = axis("y");
    private float scale = -1;
    private boolean inHeader;

    GraphRangeBar(Runnable onEnter) {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setOpaque(false);
        xRange.setToolTipText("x range, e.g. -5, 5. Empty: -10 to 10.");
        yRange.setToolTipText("y range, e.g. -2, 8. Empty or auto: chosen from the function.");
        xRange.addActionListener(e -> onEnter.run());
        yRange.addActionListener(e -> onEnter.run());
        addMouseListener(new MouseAdapter() { });  // clicks stay off the canvas
        setScale(1f);
        setVisible(false);
    }

    // match the card header: the card scales with canvas zoom, so do the boxes
    void setScale(float ui) {
        if (ui == scale) {
            return;
        }
        scale = ui;
        removeAll();
        xLabel.setFont(Fonts.ui(Font.ITALIC, 13.5f * ui));
        yLabel.setFont(Fonts.ui(Font.ITALIC, 13.5f * ui));
        xRange.scaleTo(ui);
        yRange.scaleTo(ui);
        int gap = Math.max(2, Math.round(4 * ui));
        int pad = inHeader ? 0 : Math.max(3, Math.round(6 * ui));
        setBorder(BorderFactory.createEmptyBorder(inHeader ? 0 : pad, pad, inHeader ? 0 : pad, pad));
        add(xLabel);
        add(Box.createHorizontalStrut(gap));
        add(xRange);
        add(Box.createHorizontalStrut(gap * 2));
        add(yLabel);
        add(Box.createHorizontalStrut(gap));
        add(yRange);
        revalidate();
    }

    // in the header the card paints the backdrop; under the card the bar brings its own
    void setInHeader(boolean inHeader) {
        if (this.inHeader == inHeader) {
            return;
        }
        this.inHeader = inHeader;
        float again = scale;
        scale = -1;
        setScale(again);
        repaint();
    }

    /**
     * Fits the boxes into the card's header right after the function, at the header's scale.
     * Returns their spot in card coordinates, or null when they don't fit: the bar then goes back to
     * full size on its own panel, for the caller to hang under the card.
     */
    Rectangle placeIn(PlotCard card) {
        float ui = card.headerScale();
        Rectangle slot = null;
        if (ui >= MIN_HEADER_SCALE) {
            setScale(ui);
            slot = card.reserveHeader(headerSize());
        } else {
            card.reserveHeader(null);
        }
        if (slot == null) {
            setScale(1f);
        }
        setInHeader(slot != null);
        return slot;
    }

    // size of just the boxes and labels, as they sit in the header (no panel padding)
    Dimension headerSize() {
        Dimension d = getPreferredSize();
        java.awt.Insets i = getInsets();
        return new Dimension(d.width - i.left - i.right, d.height - i.top - i.bottom);
    }

    String xText() {
        return xRange.getText();
    }

    String yText() {
        return yRange.getText();
    }

    void setRangeText(String x, String y) {
        xRange.setText(x);
        yRange.setText(y);
    }

    // after a pan / zoom, show the window you're looking at (y stays as typed for 3d)
    void showRange(double xMin, double xMax, double yMin, double yMax, boolean showY) {
        if (xRange.hasFocus() || yRange.hasFocus()) {
            return;   // don't stomp on typing
        }
        put(xRange, pair(xMin, xMax));
        if (showY) {
            put(yRange, pair(yMin, yMax));
        }
    }

    // only touch the box if it changed, start of the text in view so the minus isn't scrolled off
    private static void put(JTextField box, String text) {
        if (!text.equals(box.getText())) {
            box.setText(text);
            box.setCaretPosition(0);
        }
    }

    private static String pair(double lo, double hi) {
        return interface_adapter.graph.GraphNumbers.format(lo) + ", "
                + interface_adapter.graph.GraphNumbers.format(hi);
    }

    private static JLabel axis(String name) {
        JLabel label = new JLabel(name);
        label.setForeground(LABEL);
        return label;
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (inHeader) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(255, 255, 255, 235));
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
        g2.setColor(BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
        g2.dispose();
    }

    // a small pill-shaped box that fits inside the card header
    private static final class RangeField extends JTextField {
        private static final int COLUMNS = 7;
        private final String placeholder;
        private float ui = 1f;
        private boolean hovering;

        RangeField(String placeholder) {
            super(COLUMNS);
            this.placeholder = placeholder;
            setOpaque(false);
            setForeground(TEXT);
            setCaretColor(ACCENT);
            setSelectionColor(new Color(0, 122, 255, 60));
            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    repaint();
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    hovering = true;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    hovering = false;
                    repaint();
                }
            });
        }

        void scaleTo(float ui) {
            this.ui = ui;
            setFont(Fonts.ui(Font.PLAIN, 12f * ui));
            int side = Math.max(4, Math.round(8 * ui));
            setBorder(BorderFactory.createEmptyBorder(0, side, 0, side));
            invalidate();
        }

        @Override
        public Dimension getPreferredSize() {
            FontMetrics m = getFontMetrics(getFont());
            int width = m.stringWidth("-10.5, 10.5") + getInsets().left + getInsets().right;
            int height = Math.max(12, Math.round(24 * ui));
            return new Dimension(width, height);
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            boolean focused = isFocusOwner();
            double h = getHeight() - 1;
            RoundRectangle2D pill = new RoundRectangle2D.Double(0.5, 0.5, getWidth() - 1, h, h, h);
            g2.setColor(Color.WHITE);
            g2.fill(pill);
            g2.setColor(focused ? ACCENT : hovering ? new Color(180, 186, 198) : FIELD_BORDER);
            g2.setStroke(new java.awt.BasicStroke(focused ? 1.5f : 1f));
            g2.draw(pill);
            if (getText().isEmpty() && !focused && placeholder != null) {
                g2.setFont(getFont());
                g2.setColor(PLACEHOLDER);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(placeholder, getInsets().left, (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
            g2.dispose();
            super.paintComponent(g);  // text, caret and selection on top
        }
    }
}
