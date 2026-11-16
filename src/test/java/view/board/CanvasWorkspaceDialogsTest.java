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
        restored.addHistoryAsset(drawing, "x^2", "", new Point(80, 75));
        assertTrue(dialogs.hasUnsavedChanges());
        restored.undo();
        assertFalse(dialogs.hasUnsavedChanges());
    }
}
