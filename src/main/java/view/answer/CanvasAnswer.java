package view.answer;

import interface_adapter.TextRun;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.List;

// an answer written on the canvas, world coords; box = ink box of the text
public final class CanvasAnswer {
    public static final double DEFAULT_PEN = 4;  // canvas default pen
    private final String text;
    private final boolean error;
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final float fontSize;
    private final List<TextRun> runs;  // null = plain text
    private final String latex;  // null = pen ink, else typeset
    private final Color color;
    private final double penWidth;  // same as the formula's ink, doesn't scale

    public CanvasAnswer(String text, boolean error, double x, double y, double width, double height, float fontSize) {
        this(text, null, null, error, x, y, width, height, fontSize, Color.BLACK, DEFAULT_PEN);
    }

    // text w/ raised exponent runs, e.g. 3x²
    public CanvasAnswer(List<TextRun> runs, boolean error, double x, double y, double width, double height,
                        float fontSize) {
        this(TextRun.plain(runs), List.copyOf(runs), null, error, x, y, width, height, fontSize, Color.BLACK,
                DEFAULT_PEN);
    }

    // typeset w/ jlatexmath, text is just the readable copy that gets saved
    public static CanvasAnswer typeset(String text, String latex, boolean error, double x, double y, double width,
                                       double height, float fontSize) {
        return new CanvasAnswer(text, null, latex, error, x, y, width, height, fontSize, Color.BLACK, DEFAULT_PEN);
    }

    private CanvasAnswer(String text, List<TextRun> runs, String latex, boolean error, double x, double y,
                         double width, double height, float fontSize, Color color, double penWidth) {
        this.text = text;
        this.runs = runs;
        this.latex = latex;
        this.error = error;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.color = color;
        this.penWidth = penWidth;
    }

    public String getText() {
        return text;
    }

    public List<TextRun> getRuns() {
        return runs;
    }

    public String getLatex() {
        return latex;
    }

    public boolean isTypeset() {
        return latex != null;
    }

    public boolean isError() {
        return error;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public float getFontSize() {
        return fontSize;
    }

    public Color getColor() {
        return color;
    }

    public double getPenWidth() {
        return penWidth;
    }

    public Rectangle2D.Double getBox() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    // box isnt refit, AnswerInk.refit after if the width changed
    public CanvasAnswer withPenWidth(double newPenWidth) {
        return new CanvasAnswer(text, runs, latex, error, x, y, width, height, fontSize, color, newPenWidth);
    }

    public CanvasAnswer withColor(Color newColor) {
        return new CanvasAnswer(text, runs, latex, error, x, y, width, height, fontSize, newColor, penWidth);
    }

    // same spot, new box size
    public CanvasAnswer sized(double newWidth, double newHeight) {
        return new CanvasAnswer(text, runs, latex, error, x, y, newWidth, newHeight, fontSize, color, penWidth);
    }

    public CanvasAnswer translated(double dx, double dy) {
        return new CanvasAnswer(text, runs, latex, error, x + dx, y + dy, width, height, fontSize, color, penWidth);
    }

    // box and font scale together
    public CanvasAnswer scaled(double anchorX, double anchorY, double factor) {
        return new CanvasAnswer(text, runs, latex, error, anchorX + (x - anchorX) * factor, anchorY + (y - anchorY) * factor,
                width * factor, height * factor, (float) (fontSize * factor), color, penWidth);
    }
}
