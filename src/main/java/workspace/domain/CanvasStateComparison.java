package workspace.domain;

import java.util.Arrays;
import java.util.Objects;

/** Checks persisted canvas content, including image bytes (record arrays use identity by default). */
public final class CanvasStateComparison {
    private CanvasStateComparison() { }

    public static boolean same(CanvasState before, CanvasState after) {
        return sameState(before, after, true);
    }

    /** Content changes affect the Edited badge; navigating the viewport does not. */
    public static boolean sameContent(CanvasState before, CanvasState after) {
        return sameState(before, after, false);
    }

    private static boolean sameState(CanvasState before, CanvasState after, boolean includeViewport) {
        if (before == after) return true;
        if (before == null || after == null) return false;
        if (!before.ink().equals(after.ink()) || !before.formulas().equals(after.formulas())
                || (includeViewport && (Double.compare(before.zoom(), after.zoom()) != 0
                    || Double.compare(before.offsetX(), after.offsetX()) != 0
                    || Double.compare(before.offsetY(), after.offsetY()) != 0))
                || before.assets().size() != after.assets().size()) return false;
        for (int i = 0; i < before.assets().size(); i++) {
            var a = before.assets().get(i);
            var b = after.assets().get(i);
            if (!Objects.equals(a.latex(), b.latex()) || !Objects.equals(a.result(), b.result())
                    || a.graph() != b.graph() || Double.compare(a.x(), b.x()) != 0
                    || Double.compare(a.y(), b.y()) != 0
                    || Double.compare(a.width(), b.width()) != 0
                    || Double.compare(a.height(), b.height()) != 0
                    || !Arrays.equals(a.png(), b.png())) return false;
        }
        return true;
    }
}
