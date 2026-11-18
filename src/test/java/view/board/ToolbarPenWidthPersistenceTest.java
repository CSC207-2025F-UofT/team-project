package view.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import view.DrawingCanvas;
import workspace.data_access.FileCanvasRepository;
import workspace.domain.CanvasState;
import workspace.domain.SavedCanvas;

import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/** New toolbar controls must preserve edit history and ink appearance when saved. */
class ToolbarPenWidthPersistenceTest {
    @TempDir Path directory;

    @Test
    void changingPenWidthSurvivesUndoRedoAndFileRoundTrip() throws Exception {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setPenWidth(7);
        long time = System.currentTimeMillis();
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, time, 0,
                100, 100, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, time + 1, 0,
                110, 101, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, time + 2, 0,
                120, 103, 1, false, MouseEvent.BUTTON1));

        assertEquals(7, canvas.exportCanvasState().ink().get(0).width());
        canvas.undo();
        assertTrue(canvas.canRedo());
        canvas.redo();
        assertEquals(7, canvas.exportCanvasState().ink().get(0).width());

        FileCanvasRepository repo = new FileCanvasRepository(directory);
        String id = UUID.randomUUID().toString();
        repo.write(new SavedCanvas(id, "Pen options", Instant.now(), canvas.exportCanvasState()));
        CanvasState restored = repo.find(id).orElseThrow().state();
        assertEquals(7, restored.ink().get(0).width());
        DrawingCanvas reopened = new DrawingCanvas();
        reopened.restoreCanvasState(restored);
        assertEquals(7, reopened.exportCanvasState().ink().get(0).width());
    }
}
