package view.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import view.DrawingCanvas;
import workspace.data_access.FileCanvasRepository;
import workspace.domain.CanvasState;
import workspace.use_case.CanvasWorkspace;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** No real modal windows: verifies snapshot-aware unsaved detection after startup/open. */
class CanvasWorkspaceDialogsTest {
    @TempDir Path temp;
    @Test void unchangedSaveDoesNotRewriteAnExistingCanvas() throws Exception {
        var repository = new FileCanvasRepository(temp);
        var workspace = new CanvasWorkspace(repository);
        var canvas = new DrawingCanvas();
        var snapshot = workspace.save(canvas.exportCanvasState(), "Saved drawing", false);
        var dialogs = new CanvasWorkspaceDialogs(new CanvasWorkspace(repository), canvas, new JPanel());
        dialogs.restoreLatestAtStartup();
        assertEquals(SaveMenuState.Phase.SAVED, dialogs.saveMenuState().phase());
        var before = repository.find(snapshot.id()).orElseThrow();
        dialogs.save();
        var persisted = repository.find(snapshot.id()).orElseThrow();
        assertEquals(before.modifiedAt(), persisted.modifiedAt(),
                "Clicking Saved must not rewrite an unchanged canvas");
    }

    @Test void startupOpenIsCleanAndEditingOrUndoUpdatesDirtyStatus() throws Exception {
        var repo = new FileCanvasRepository(temp);
        var initial = new DrawingCanvas();
        var drawing = new BufferedImage(60, 30, BufferedImage.TYPE_INT_ARGB);
        drawing.setRGB(2, 3, Color.BLUE.getRGB());
        initial.addHistoryAsset(drawing, "y=x", "", new Point(25,35));
        new CanvasWorkspace(repo).save(initial.exportCanvasState(), "课程笔记", false);

        var restored = new DrawingCanvas();
        var dialogs = new CanvasWorkspaceDialogs(new CanvasWorkspace(repo), restored, new JPanel());
        dialogs.restoreLatestAtStartup();
        assertFalse(dialogs.hasUnsavedChanges());
        assertEquals(SaveMenuState.Phase.SAVED, dialogs.saveMenuState().phase());
        assertEquals("课程笔记", dialogs.saveMenuState().name());
        restored.addHistoryAsset(drawing, "x^2", "", new Point(80, 75));
        assertTrue(dialogs.hasUnsavedChanges());
        assertEquals(SaveMenuState.Phase.MODIFIED, dialogs.saveMenuState().phase());
        restored.undo();
        assertFalse(dialogs.hasUnsavedChanges());
        assertEquals(SaveMenuState.Phase.SAVED, dialogs.saveMenuState().phase());
    }
}
