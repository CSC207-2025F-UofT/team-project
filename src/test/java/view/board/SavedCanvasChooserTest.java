package view.board;

import org.junit.jupiter.api.Test;
import workspace.domain.CanvasState;
import workspace.domain.SavedCanvas;

import javax.swing.SwingUtilities;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class SavedCanvasChooserTest {
    static SavedCanvas saved(String name) {
        return new SavedCanvas(UUID.randomUUID().toString(), name, Instant.parse("2026-11-15T18:23:00Z"),
                new CanvasState(List.of(), List.of(), List.of(), 1, 0, 0));
    }

    @Test void searchableListContainsOnlyActualSavesAndRestoresSelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var chinese = saved("线性代数");
            var maths = saved("Calculus Notes");
            var chooser = new SavedCanvasChooser(List.of(chinese, maths));
            assertEquals(2, chooser.resultCount());
            chooser.query("代数");
            assertEquals(1, chooser.resultCount());
            assertEquals(chinese.id(), chooser.selected().id());
            chooser.query("not a saved canvas");
            assertEquals(0, chooser.resultCount());
            assertNull(chooser.selected());
            chooser.query("");
            assertEquals(2, chooser.resultCount());
        });
    }

    @Test void emptyListDoesNotShowFabricatedSampleRows() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var chooser = new SavedCanvasChooser(List.of());
            assertEquals(0, chooser.resultCount());
            assertNull(chooser.selected());
        });
    }
}
