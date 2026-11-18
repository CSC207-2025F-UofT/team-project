package view.board;

import view.DrawingCanvas;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.function.Consumer;

/** Presentation of tool settings. Ink operations remain owned by DrawingCanvas. */
final class ToolOptionsPopup extends JPopupMenu {
    private static final Color BLUE = new Color(0x007AFF);
    private static final Color INK = new Color(0x26282D);
    private static final Color MUTED = new Color(0x747781);
    private static final Color TRACK = new Color(0xF0F0F4);
    private static final Color SELECTED = new Color(0xEAF2FF);
    private static final int WIDTH = 352;
    private final DrawingCanvas canvas;
    private final Consumer<Color> colorChanged;

    ToolOptionsPopup(DrawingCanvas canvas, Consumer<Color> colorChanged) {
        this.canvas = canvas;
        this.colorChanged = colorChanged;
        setUI(new javax.swing.plaf.basic.BasicPopupMenuUI());
        setBorder(BorderFactory.createEmptyBorder());
        setBackground(new Color(0, 0, 0, 0));
        setOpaque(false);
        setLightWeightPopupEnabled(true);
        setFocusable(false);
    }

    void showFor(DrawingCanvas.Tool tool, Component anchor, int x, int y) {
        rebuild(tool);
        show(anchor, x, -getPreferredSize().height - 9);
    }

    private void rebuild(DrawingCanvas.Tool tool) {
        removeAll();
        RoundedSurface surface = new RoundedSurface();
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(18, 19, 17, 19));
        switch (tool) {
            case PEN -> penOptions(body);
            case ERASER -> eraserOptions(body);
            case LASSO, RECTANGLE -> selectionOptions(body, tool);
        }
        surface.add(body);
        add(surface);
        revalidate();
        repaint();
    }

    private void penOptions(JPanel body) {
        body.add(new StrokePreview(canvas.getPenWidth(), canvas.getInkColor()));
        body.add(space(14));
        body.add(heading("Thickness", formatPt(canvas.getPenWidth())));
        body.add(space(7));
        JPanel sizes = controlTrack();
        for (int width : new int[]{1, 2, 4, 7, 10}) {
            JButton b = dotSize(width, Math.round(canvas.getPenWidth()) == width, false);
            b.setToolTipText(width + " pt");
            b.addActionListener(e -> { canvas.setPenWidth(width); refresh(DrawingCanvas.Tool.PEN); });
            sizes.add(b);
        }
        body.add(sizes);
        body.add(space(17));
        body.add(heading("Color", ""));
        body.add(space(9));
        JPanel colors = new JPanel(new GridLayout(1, 7, 8, 0));
        colors.setOpaque(false);
        colors.setAlignmentX(Component.LEFT_ALIGNMENT);
        colors.setPreferredSize(new Dimension(WIDTH - 38, 39));
        colors.setMaximumSize(new Dimension(WIDTH, 39));
        for (Color c : new Color[]{new Color(0x1C1C1E), new Color(0xFF3B30), BLUE,
                new Color(0x34C759), new Color(0xFF9500), new Color(0xAF52DE)}) {
            JButton b = colorDot(c, sameSwatch(c, canvas.getInkColor()), false);
            b.addActionListener(e -> { colorChanged.accept(c); setVisible(false); });
            colors.add(b);
        }
        JButton wheel = colorDot(null, false, true);
        wheel.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(this, "Choose Pen Color", canvas.getInkColor());
            if (selected != null) { colorChanged.accept(selected); setVisible(false); }
        });
        colors.add(wheel);
        body.add(colors);
    }

    private void eraserOptions(JPanel body) {
        body.add(heading("Eraser", "mode · size"));
        body.add(space(9));
        JPanel modes = twoCards();
        modes.add(modeCard("Stroke", "Removes a whole", "line in one touch",
                canvas.getEraserMode() == DrawingCanvas.EraserMode.STROKE, false,
                () -> { canvas.setEraserMode(DrawingCanvas.EraserMode.STROKE); refresh(DrawingCanvas.Tool.ERASER); }));
        modes.add(modeCard("Precise", "Erases only what", "it passes over",
                canvas.getEraserMode() == DrawingCanvas.EraserMode.PRECISE, false,
                () -> { canvas.setEraserMode(DrawingCanvas.EraserMode.PRECISE); refresh(DrawingCanvas.Tool.ERASER); }));
        body.add(modes);
        body.add(space(18));
        body.add(heading("Size", canvas.getEraserSize() + " pt"));
        body.add(space(8));
        JPanel sizes = controlTrack();
        for (int value : new int[]{4, 8, 16, 24, 32}) {
            JButton b = dotSize(value, canvas.getEraserSize() == value, true);
            b.setToolTipText(value + " pt");
            b.addActionListener(e -> { canvas.setEraserSize(value); refresh(DrawingCanvas.Tool.ERASER); });
            sizes.add(b);
        }
        body.add(sizes);
    }

    private void selectionOptions(JPanel body, DrawingCanvas.Tool tool) {
        body.add(heading("Lasso", "freeform or box"));
        body.add(space(10));
        JPanel choices = twoCards();
        choices.add(modeCard("Freeform", "Draw around what", "you want",
                tool == DrawingCanvas.Tool.LASSO, true,
                () -> { canvas.setTool(DrawingCanvas.Tool.LASSO); refresh(DrawingCanvas.Tool.LASSO); }));
        choices.add(modeCard("Box", "Drag a rectangle", "to select",
                tool == DrawingCanvas.Tool.RECTANGLE, true,
                () -> { canvas.setTool(DrawingCanvas.Tool.RECTANGLE); refresh(DrawingCanvas.Tool.RECTANGLE); }));
        body.add(choices);
    }

    /** Rebuild the visible controls after a setting changes, leaving the settings panel open. */
    private void refresh(DrawingCanvas.Tool tool) {
        rebuild(tool);
    }

    private static JPanel twoCards() {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(WIDTH, 145));
        row.setPreferredSize(new Dimension(WIDTH - 38, 145));
        return row;
    }

    private static JButton modeCard(String title, String description1, String description2,
                                    boolean chosen, boolean selection, Runnable action) {
        JButton button = new JButton() {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = graphics2(graphics);
                g.setColor(chosen ? SELECTED : TRACK);
                g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 17, 17);
                if (chosen) {
                    g.setColor(BLUE);
                    g.setStroke(new BasicStroke(1.8f));
                    g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 17, 17);
                }
                g.setColor(chosen ? BLUE : INK);
                g.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                if (selection) {
                    if ("Box".equals(title)) {
                        float[] dash = {5, 4};
                        g.setStroke(new BasicStroke(2.1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, dash, 0));
                        g.drawRect(20, 19, 24, 24);
                    } else {
                        g.drawOval(19, 20, 27, 15);
                        g.drawLine(28, 34, 31, 43);
                    }
                } else {
                    Path2D p = new Path2D.Double();
                    p.moveTo(19, 31); p.curveTo(29, 17, 37, 42, 52, 29);
                    if ("Precise".equals(title)) {
                        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                                1f, new float[]{5, 5}, 0));
                    }
                    g.draw(p);
                }
                drawText(g, title, 18, 76, 15, Font.BOLD, INK);
                drawText(g, description1, 18, 101, 11.5f, Font.PLAIN, MUTED);
                drawText(g, description2, 18, 117, 11.5f, Font.PLAIN, MUTED);
                g.dispose();
            }
        };
        button.setPreferredSize(new Dimension(145, 145));
        button.setToolTipText(title);
        bare(button);
        button.addActionListener(e -> action.run());
        return button;
    }

    private static JPanel controlTrack() {
        JPanel row = new JPanel(new GridLayout(1, 5, 3, 0)) {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = graphics2(graphics);
                g.setColor(TRACK);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g.dispose();
                super.paintComponent(graphics);
            }
        };
        row.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setPreferredSize(new Dimension(WIDTH - 38, 55));
        row.setMaximumSize(new Dimension(WIDTH, 55));
        return row;
    }

    private static JButton dotSize(int size, boolean selected, boolean outline) {
        JButton button = new JButton() {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = graphics2(graphics);
                if (selected) {
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(1, 1, getWidth()-2, getHeight()-2, 13, 13);
                    g.setColor(new Color(0,0,0,15));
                    g.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 13, 13);
                }
                int diameter = outline ? Math.min(30, 5 + size * 2 / 3) : Math.min(25, 3 + size * 2);
                int cx = getWidth()/2, cy = getHeight()/2;
                g.setColor(outline && selected ? BLUE : INK);
                if (outline) {
                    g.setStroke(new BasicStroke(1.7f));
                    g.drawOval(cx-diameter/2,cy-diameter/2,diameter,diameter);
                } else g.fillOval(cx-diameter/2,cy-diameter/2,diameter,diameter);
                g.dispose();
            }
        };
        bare(button);
        return button;
    }

    private static boolean sameSwatch(Color swatch, Color current) {
        return swatch.equals(current) || (swatch.getRed() < 40 && swatch.getGreen() < 40
                && swatch.getBlue() < 40 && current.getRed() < 40
                && current.getGreen() < 40 && current.getBlue() < 40);
    }

    private static JButton colorDot(Color color, boolean selected, boolean wheel) {
        JButton button = new JButton() {
            @Override protected void paintComponent(Graphics graphics) {
                Graphics2D g = graphics2(graphics);
                int d = Math.min(31, Math.min(getWidth()-8, getHeight()-8));
                int x=(getWidth()-d)/2,y=(getHeight()-d)/2;
                if (wheel) {
                    for (int h=0;h<360;h+=3) {
                        g.setColor(Color.getHSBColor(h/360f,1,1));
                        g.fillArc(x,y,d,d,h,4);
                    }
                    g.setColor(new Color(255,255,255,70));g.fillOval(x+d/4,y+d/4,d/2,d/2);
                } else {
                    g.setColor(color);g.fillOval(x,y,d,d);
                }
                if (selected) {
                    g.setStroke(new BasicStroke(2.3f));g.setColor(BLUE);
                    g.drawOval(x-3,y-3,d+6,d+6);
                }
                g.dispose();
            }
        };
        bare(button);
        return button;
    }

    private static JPanel heading(String left, String right) {
        JPanel row = new JPanel(new java.awt.BorderLayout());
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(WIDTH, 24));
        JLabel label = new JLabel(left);
        label.setForeground(MUTED);label.setFont(BoardTheme.font(12f).deriveFont(Font.BOLD));
        JLabel count = new JLabel(right, SwingConstants.RIGHT);
        count.setForeground(MUTED);count.setFont(BoardTheme.font(12f));
        row.add(label, java.awt.BorderLayout.WEST);
        row.add(count, java.awt.BorderLayout.EAST);
        return row;
    }

    private static Component space(int n) { return Box.createRigidArea(new Dimension(0,n)); }
    private static String formatPt(float pt) { return (int)pt == pt ? (int)pt + " pt" : pt + " pt"; }
    private static void bare(JButton b) {
        b.setContentAreaFilled(false);b.setBorderPainted(false);b.setFocusPainted(false);b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder());
    }
    private static Graphics2D graphics2(Graphics graphics) {
        Graphics2D g=(Graphics2D)graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }
    private static void drawText(Graphics2D g,String text,int x,int y,float size,int style,Color color) {
        g.setFont(BoardTheme.font(size).deriveFont(style));g.setColor(color);g.drawString(text,x,y);
    }

    /** One self-painted rounded card, avoiding the native white menu-item surface. */
    private static final class RoundedSurface extends JPanel {
        RoundedSurface() {
            super(new java.awt.BorderLayout());
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 14, 10));
        }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g=graphics2(graphics);
            int cardBottom = getHeight() - 17;
            int center = getWidth() / 2;
            g.setColor(new Color(26,35,58,22));
            g.fillRoundRect(3,5,getWidth()-6,cardBottom,24,24);
            g.setColor(Color.WHITE);
            g.fillRoundRect(1,1,getWidth()-2,cardBottom-1,23,23);
            Path2D tip = new Path2D.Double();
            tip.moveTo(center - 12, cardBottom - 1);
            tip.lineTo(center, cardBottom + 12);
            tip.lineTo(center + 12, cardBottom - 1);
            tip.closePath();
            g.fill(tip);
            g.setColor(new Color(0xDBDFE7));
            g.setStroke(new BasicStroke(1));
            g.drawRoundRect(1,1,getWidth()-3,cardBottom-2,23,23);
            g.dispose();
        }
    }

    private final class StrokePreview extends JComponent {
        private final float width;
        private final Color ink;
        StrokePreview(float width,Color ink) {
            this.width=width;this.ink=ink;
            setPreferredSize(new Dimension(ToolOptionsPopup.WIDTH-38,63));
            setMaximumSize(new Dimension(ToolOptionsPopup.WIDTH,63));
            setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics graphics) {
            Graphics2D g=graphics2(graphics);
            g.setColor(TRACK);
            g.fillRoundRect(0,0,getWidth(),getHeight(),15,15);
            g.setColor(ink);
            g.setStroke(new BasicStroke(width,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            Path2D curve=new Path2D.Double();
            double w=getWidth();
            curve.moveTo(w*.08,37);
            curve.curveTo(w*.23,5,w*.30,55,w*.46,30);
            curve.curveTo(w*.63,-1,w*.72,55,w*.86,30);
            curve.curveTo(w*.91,24,w*.94,18,w*.95,14);
            g.draw(curve);g.dispose();
        }
    }
}
