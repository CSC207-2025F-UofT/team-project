package view.board;

import view.DrawingCanvas;
import view.glass.GlassDialogs;
import view.glass.GlassNotices;
import workspace.domain.CanvasState;
import workspace.domain.CanvasStateComparison;
import workspace.domain.SavedCanvas;
import workspace.use_case.CanvasWorkspaceBoundary;

import java.awt.Component;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Swing adapter for named canvases. Uses the workspace boundary for I/O and never reads files directly.
 * Snapshot comparison prevents unnecessary unsaved-changes warnings after an untouched Open.
 */
public final class CanvasWorkspaceDialogs {
    private final CanvasWorkspaceBoundary workspace;
    private final DrawingCanvas canvas;
    private final Component parent;
    private CanvasState lastSavedState;
    private String currentName = "Untitled canvas";

    public CanvasWorkspaceDialogs(CanvasWorkspaceBoundary workspace, DrawingCanvas canvas, Component parent) {
        this.workspace = Objects.requireNonNull(workspace);
        this.canvas = Objects.requireNonNull(canvas);
        this.parent = Objects.requireNonNull(parent);
    }

    /** Save immediately when named; request a name only on the first save. */
    public void save() { saveCurrent(false); }

    /** The caller may bind this to Shift+Ctrl/Cmd+S or a menu action. */
    public void saveAs() { saveCurrent(true); }

    private boolean saveCurrent(boolean saveAs) {
        try {
            String name = currentName;
            if (saveAs || workspace.currentId().isEmpty()) {
                name = GlassDialogs.input(parent, "Save Canvas", "Name", currentName, "Save");
                if (name == null) return false;
                if (name.isBlank()) { GlassDialogs.warning(parent, "Please provide a canvas name."); return false; }
            }
            CanvasState state = canvas.exportCanvasState();
            SavedCanvas saved = workspace.save(state, name, saveAs);
            currentName = saved.name();
            lastSavedState = saved.state();
            GlassNotices.showSuccess(parent, "Saved “" + saved.name() + "”");
            return true;
        } catch (IOException | IllegalArgumentException ex) {
            GlassNotices.showError(parent, "Can't save canvas", ex.getMessage());
            return false;
        }
    }

    /** True for actual persistent changes, including edits to imported images and ink. */
    public boolean hasUnsavedChanges() {
        CanvasState current = canvas.exportCanvasState();
        return lastSavedState == null ? !canvas.isBlank() :
                !CanvasStateComparison.same(lastSavedState, current);
    }

    public void open() {
        try {
            List<SavedCanvas> documents = workspace.all();
            if (documents.isEmpty()) {
                GlassDialogs.info(parent, "No saved canvases yet. Use Save to create one.");
                return;
            }
            SavedCanvasChooser picker = new SavedCanvasChooser(documents);
            if (!GlassDialogs.choose(parent, "Open Canvas", picker, "Open", picker::onDoubleClick)) return;
            SavedCanvas chosen = picker.selected();
            if (chosen == null) return;
            if (hasUnsavedChanges()) {
                GlassDialogs.UnsavedDecision decision = GlassDialogs.unsavedChanges(parent, currentName);
                if (decision == GlassDialogs.UnsavedDecision.CANCEL) return;
                if (decision == GlassDialogs.UnsavedDecision.SAVE && !saveCurrent(false)) return;
            }
            SavedCanvas opened = workspace.open(chosen.id());
            canvas.restoreCanvasState(opened.state());
            currentName = opened.name();
            lastSavedState = canvas.exportCanvasState();
        } catch (IOException | IllegalArgumentException failure) {
            GlassNotices.showError(parent, "Can't open canvas", failure.getMessage());
        }
    }

    public void restoreLatestAtStartup() {
        try {
            var recent = workspace.mostRecent();
            if (recent.isPresent()) {
                SavedCanvas opened = workspace.open(recent.get().id());
                canvas.restoreCanvasState(opened.state());
                lastSavedState = canvas.exportCanvasState();
                currentName = opened.name();
            }
        } catch (IOException | RuntimeException failure) {
            // Bad saved files must not break startup or erase a user's current drawing.
            System.err.println("Unable to restore saved canvas: " + failure.getMessage());
        }
    }
}
