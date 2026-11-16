package workspace;

import org.junit.jupiter.api.Test;
import workspace.domain.CanvasState;
import workspace.domain.CanvasStateComparison;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Regression: record equality is insufficient for embedded PNG arrays. */
class CanvasStateComparisonTest {
    private CanvasState state(byte[] png, double x) {
        return new CanvasState(List.of(), List.of(),
                List.of(new CanvasState.Asset("y=x", "", png, x, 23, 120, 80)), 1, 0, 0);
    }
    @Test void equalImageContentsDoNotCreateFalseDirtyChanges() {
        assertTrue(CanvasStateComparison.same(state(new byte[]{1,2,3},10), state(new byte[]{1,2,3},10)));
    }
    @Test void changedImageBytesOrPlacementMarkWorkspaceAsDirty() {
        var original = state(new byte[]{1,2,3}, 10);
        assertFalse(CanvasStateComparison.same(original, state(new byte[]{1,2,4},10)));
        assertFalse(CanvasStateComparison.same(original, state(new byte[]{1,2,3},11)));
    }
    @Test void changedInkAndViewportAlsoCountAsChanges() {
        var empty = new CanvasState(List.of(), List.of(), List.of(), 1, 0, 0);
        var moved = new CanvasState(List.of(), List.of(), List.of(), 1, 42, 0);
        var drawing = new CanvasState(List.of(new CanvasState.Ink(
                List.of(new CanvasState.Point(7,9,11)), 0xff007aff)), List.of(), List.of(), 1, 0, 0);
        assertFalse(CanvasStateComparison.same(empty, moved));
        assertFalse(CanvasStateComparison.same(empty, drawing));
        assertTrue(CanvasStateComparison.same(empty, empty));
        assertFalse(CanvasStateComparison.same(null, empty));
    }
}
