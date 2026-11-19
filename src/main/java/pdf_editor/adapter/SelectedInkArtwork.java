package pdf_editor.adapter;

import entity.Stroke;
import view.DrawingCanvas;
import view.answer.AnswerInk;
import view.answer.CanvasAnswer;
import view.answer.Formula;
import workspace.domain.CanvasState;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Export adapter for selected Canvas ink.
 *
 * CanvasState is the public persistence snapshot: unlike a recognition screenshot,
 * each Ink record preserves its original colour and pen width. Use the same stroke
 * order as DrawingCanvas.getStrokes() to match selections without touching Canvas
 * drawing, formula grouping or its internal width map.
 */
public final class SelectedInkArtwork {
    private static final int MARGIN = 18;
    private static final int RESOLUTION = 3;

    private SelectedInkArtwork() { }

    /** Export a transparent PNG-ready raster; include the visible answer for Library copies only. */
    public static BufferedImage render(DrawingCanvas canvas, boolean includeAnswer) {
        Objects.requireNonNull(canvas, "canvas");
        List<Stroke> selected = canvas.selectedInkSnapshot();
        if (selected.isEmpty()) throw new IllegalStateException("Select ink with Box or Lasso first.");

        CanvasState snapshot = canvas.exportCanvasState();
        List<Stroke> source = canvas.getStrokes();
        if (snapshot.ink().size() != source.size())
            throw new IllegalStateException("Canvas ink changed during export");

        // Identity matters: multiple strokes can have identical point coordinates.
        Set<Stroke> chosen = Collections.newSetFromMap(new IdentityHashMap<>());
        chosen.addAll(selected);
        List<CanvasState.Ink> ink = new ArrayList<>();
        for (int i = 0; i < source.size(); i++) {
            if (chosen.contains(source.get(i))) ink.add(snapshot.ink().get(i));
        }

        List<CanvasAnswer> answers = new ArrayList<>();
        if (includeAnswer) {
            for (Formula formula : canvas.getFormulas()) {
                CanvasAnswer answer = formula.getAnswer();
                if (answer != null && chosen.containsAll(formula.getStrokes())) answers.add(answer);
            }
        }
        return rasterize(ink, answers);
    }

    /** Package-visible pure rendering operation for deterministic width and alpha tests. */
    static BufferedImage rasterize(List<CanvasState.Ink> strokes, List<CanvasAnswer> answers) {
        Objects.requireNonNull(strokes, "strokes");
        Objects.requireNonNull(answers, "answers");
        if (strokes.isEmpty()) throw new IllegalArgumentException("No ink to export");

        double left = Double.POSITIVE_INFINITY, top = Double.POSITIVE_INFINITY;
        double right = Double.NEGATIVE_INFINITY, bottom = Double.NEGATIVE_INFINITY;
        for (CanvasState.Ink stroke : strokes) {
            for (CanvasState.Point point : stroke.points()) {
                left = Math.min(left, point.x());
                top = Math.min(top, point.y());
                right = Math.max(right, point.x());
                bottom = Math.max(bottom, point.y());
            }
        }
        for (CanvasAnswer answer : answers) {
            Rectangle2D box = answer.getBox();
            left = Math.min(left, box.getMinX()); top = Math.min(top, box.getMinY());
            right = Math.max(right, box.getMaxX()); bottom = Math.max(bottom, box.getMaxY());
        }
        int width = Math.max(1, (int) Math.ceil(right - left + 2 * MARGIN) * RESOLUTION);
        int height = Math.max(1, (int) Math.ceil(bottom - top + 2 * MARGIN) * RESOLUTION);
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = result.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            graphics.scale(RESOLUTION, RESOLUTION);
            graphics.translate(MARGIN - left, MARGIN - top);
            for (CanvasState.Ink stroke : strokes) {
                graphics.setColor(new Color(stroke.argb(), true));
                graphics.setStroke(new BasicStroke(stroke.width(), BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                Path2D path = new Path2D.Double();
                boolean first = true;
                for (CanvasState.Point point : stroke.points()) {
                    if (first) { path.moveTo(point.x(), point.y()); first = false; }
                    else path.lineTo(point.x(), point.y());
                }
                if (stroke.points().size() == 1) {
                    // A one-point stroke must remain visible as a round pen dot.
                    CanvasState.Point point = stroke.points().get(0);
                    graphics.drawLine(point.x(), point.y(), point.x(), point.y());
                } else {
                    graphics.draw(path);
                }
            }
            for (CanvasAnswer answer : answers)
                AnswerInk.paint(graphics, answer, answer.getColor(), answer.getPenWidth());
        } finally { graphics.dispose(); }
        return result;
    }
}
