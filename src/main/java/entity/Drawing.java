package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Drawing extends Layer {
    private final int drawingId;
    private final List<Stroke> strokes = new ArrayList<>();

    public Drawing(int drawingId, String name) {
        super(name);
        this.drawingId = drawingId;
    }

    public int getDrawingId() {
        return drawingId;
    }

    public List<Stroke> getStrokes() {
        return Collections.unmodifiableList(strokes);
    }

    public void addStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("stroke cannot be null");
        }
        strokes.add(stroke);
    }

    public boolean removeStroke(Stroke stroke) {
        return strokes.remove(stroke);
    }

    public void clear() {
        strokes.clear();
    }

    public boolean isEmpty() {
        return strokes.isEmpty();
    }
}
