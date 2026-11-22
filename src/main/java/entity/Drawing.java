package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Drawing extends Layer {
    private final List<Stroke> strokes = new ArrayList<>();

    public Drawing(int layerId, String name) {
        super(layerId, name);
    }

    public int getDrawingId() {
        return getLayerId();
    }

    public List<Stroke> getStrokes() {
        return Collections.unmodifiableList(strokes);
    }

    public void addStroke(Stroke stroke) {
        if (stroke == null) {
            throw new IllegalArgumentException("Stroke cannot be null");
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
