package view.board;

import view.DrawingCanvas;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.function.Consumer;

/** Presentation-only controls for the selected ink tool. All canvas operations are delegated. */
final class ToolOptionsPopup extends JPopupMenu {
    private static final Color PANEL = Color.WHITE;
    private static final Color NEUTRAL = new Color(0xF1F2F6);
    private static final Color BLUE = new Color(0x007AFF);
    private final DrawingCanvas canvas;
    private final Consumer<Color> colorChanged;

    ToolOptionsPopup(DrawingCanvas canvas, Consumer<Color> colorChanged) {
        this.canvas = canvas;
        this.colorChanged = colorChanged;
        setBorder(BorderFactory.createEmptyBorder(13, 14, 19, 14));
        setBackground(PANEL);
        setOpaque(false);
    }

    void showFor(DrawingCanvas.Tool tool, java.awt.Component anchor, int x, int y) {
        removeAll();
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setPreferredSize(new Dimension(306, tool == DrawingCanvas.Tool.PEN ? 245 :
                tool == DrawingCanvas.Tool.ERASER ? 155 : 105));
        switch (tool) {
            case PEN -> penOptions(content);
            case ERASER -> eraserOptions(content);
            case LASSO, RECTANGLE -> selectionOptions(content, tool);
        }
        add(content);
        // Position flush above the floating toolbar regardless of the selected tool's content height.
        show(anchor, x, -getPreferredSize().height - 12);
    }

    private void penOptions(JPanel content) {
        content.add(new StrokePreview());
        content.add(Box.createVerticalStrut(10));
        content.add(label("Thickness"));
        JPanel sizes = line();
        for (int width : new int[]{1, 2, 4, 7, 10}) {
            JButton button = pill("●", width == Math.round(canvas.getPenWidth()));
            button.setFont(BoardTheme.font(8f + width));
            button.setToolTipText(width + " pt");
            button.addActionListener(e -> {
                canvas.setPenWidth(width);
                setVisible(false);
            });
            sizes.add(button);
        }
        content.add(sizes);
        content.add(Box.createVerticalStrut(10));
        content.add(label("Color"));
        JPanel colors = line();
        for (Color c : BoardTheme.SWATCHES) {
            JButton button = colorButton(c);
            button.addActionListener(e -> selectColor(c));
            colors.add(button);
        }
        JButton more = pill("🌈", false);
        more.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Pen Color", canvas.getInkColor());
            if (c != null) selectColor(c);
        });
        colors.add(more);
        content.add(colors);
    }

    private void selectColor(Color color) {
        colorChanged.accept(color);
        setVisible(false);
    }

    private void eraserOptions(JPanel content) {
        content.add(label("Eraser mode"));
        JPanel modes = line();
        for (DrawingCanvas.EraserMode mode : new DrawingCanvas.EraserMode[]{
                DrawingCanvas.EraserMode.STROKE, DrawingCanvas.EraserMode.PRECISE}) {
            JButton b = pill(mode == DrawingCanvas.EraserMode.STROKE ? "Stroke" : "Precise",
                    canvas.getEraserMode() == mode);
            b.addActionListener(e -> {
                canvas.setEraserMode(mode);
                setVisible(false);
            });
            modes.add(b);
        }
        content.add(modes);
        content.add(Box.createVerticalStrut(12));
        content.add(label("Size · " + canvas.getEraserSize() + " pt"));
        JPanel sizes = line();
        for (int size : new int[]{4, 8, 16, 24, 32}) {
            JButton b = pill("○", canvas.getEraserSize() == size);
            b.setFont(BoardTheme.font(8 + size / 3f));
            b.setToolTipText(size + " pt");
            b.addActionListener(e -> { canvas.setEraserSize(size); setVisible(false); });
            sizes.add(b);
        }
        content.add(sizes);
    }

    private void selectionOptions(JPanel content, DrawingCanvas.Tool tool) {
        content.add(label("Selection shape"));
        JPanel modes = line();
        JButton free = pill("Freeform", tool == DrawingCanvas.Tool.LASSO);
        JButton box = pill("Box", tool == DrawingCanvas.Tool.RECTANGLE);
        free.addActionListener(e -> { canvas.setTool(DrawingCanvas.Tool.LASSO); setVisible(false); });
        box.addActionListener(e -> { canvas.setTool(DrawingCanvas.Tool.RECTANGLE); setVisible(false); });
        modes.add(free);
        modes.add(box);
        content.add(modes);
    }

    private static JLabel label(String value) {
        JLabel l = new JLabel(value);
        l.setFont(BoardTheme.font(12f));
        l.setForeground(new Color(0x69717D));
        l.setBorder(BorderFactory.createEmptyBorder(2, 3, 7, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private static JPanel line() {
        JPanel p = new JPanel(new java.awt.GridLayout(1, 0, 5, 0));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(310, 43));
        p.setAlignmentX(LEFT_ALIGNMENT);
        return p;
    }

    private static JButton pill(String text, boolean chosen) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(BoardTheme.font(12f));
        b.setBackground(chosen ? new Color(0xE7F0FF) : NEUTRAL);
        b.setForeground(chosen ? BLUE : new Color(0x252833));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(chosen ? BLUE : NEUTRAL, 1, true),
                BorderFactory.createEmptyBorder(7, 7, 7, 7)));
        b.setOpaque(true);
        return b;
    }

    private static JButton colorButton(Color c) {
        JButton b = pill("●", false);
        b.setForeground(c);
        b.setFont(BoardTheme.font(20f));
        return b;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0xD8DCE5));
        g.fillRoundRect(1, 3, getWidth() - 2, getHeight() - 6, 27, 27);
        g.setColor(PANEL);
        g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 7, 25, 25);
        g.dispose();
    }

    private final class StrokePreview extends JPanel {
        StrokePreview() {
            setOpaque(false);
            setPreferredSize(new Dimension(306, 59));
            setMaximumSize(new Dimension(306, 59));
            setAlignmentX(LEFT_ALIGNMENT);
        }
        @Override protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(NEUTRAL);
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g.setColor(new Color(0x242426));
            g.setStroke(new BasicStroke(canvas.getPenWidth(), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            java.awt.geom.Path2D curve = new java.awt.geom.Path2D.Double();
            curve.moveTo(28, 36);
            curve.curveTo(75, 3, 107, 55, 153, 26);
            curve.curveTo(211, -1, 251, 57, 280, 24);
            g.draw(curve);
            g.dispose();
        }
    }
}
