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

    /** Save immediately when named, and never overwrite an unchanged saved canvas. */
    public void save() {
        if (workspace.currentId().isPresent() && !hasUnsavedChanges()) return;
        saveCurrent(false);
    }

    /** A plain, immutable state snapshot for the workspace toolbar (the domain never knows Swing). */
    public SaveMenuState saveMenuState() {
        if (workspace.currentId().isEmpty()) return SaveMenuState.newCanvas();
        return new SaveMenuState(currentName, hasUnsavedChanges()
                ? SaveMenuState.Phase.MODIFIED : SaveMenuState.Phase.SAVED);
    }

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

    private boolean confirmDiscardOrSave() {
        if (!hasUnsavedChanges()) return true;
        GlassDialogs.UnsavedDecision decision = GlassDialogs.unsavedChanges(parent, currentName);
        return decision == GlassDialogs.UnsavedDecision.DONT_SAVE
                || (decision == GlassDialogs.UnsavedDecision.SAVE && saveCurrent(false));
    }

    /** New is intentionally unsaved until the first Save prompts for a name. */
    public void newCanvas() {
        if (!confirmDiscardOrSave()) return;
        try {
            canvas.restoreCanvasState(new CanvasState(List.of(), List.of(), List.of(), 1, 0, 0));
            workspace.newCanvas();
            lastSavedState = null;
            currentName = "Untitled canvas";
        } catch (IOException failure) {
            GlassNotices.showError(parent, "Can't create canvas", failure.getMessage());
        }
    }

    public void open() {
        try {
            SavedCanvasChooser picker = new SavedCanvasChooser(workspace.all());
            picker.onDelete(saved -> {
                boolean confirmed = GlassDialogs.confirm(parent, "Delete Canvas",
                        "Permanently delete “" + saved.name() + "”? This cannot be undone.", "Delete");
                if (!confirmed) return;
                try {
                    boolean deletingCurrent = workspace.currentId().filter(saved.id()::equals).isPresent();
                    workspace.delete(saved.id());
                    picker.removeSaved(saved.id());
                    if (deletingCurrent) { lastSavedState = null; currentName = "Untitled canvas"; }
                    GlassNotices.showSuccess(parent, "Deleted “" + saved.name() + "”");
                } catch (IOException | IllegalArgumentException failure) {
                    GlassNotices.showError(parent, "Can't delete canvas", failure.getMessage());
                }
            });
            if (!GlassDialogs.choose(parent, "", picker, "Open", picker::onDoubleClick)) return;
            if (picker.choice() == SavedCanvasChooser.Choice.NEW) {
                newCanvas();
                return;
            }
            SavedCanvas chosen = picker.selected();
            if (chosen == null) return;
            if (picker.choice() == SavedCanvasChooser.Choice.RENAME) {
                rename(chosen);
                return;
            }
            if (!confirmDiscardOrSave()) return;
            SavedCanvas opened = workspace.open(chosen.id());
            canvas.restoreCanvasState(opened.state());
            currentName = opened.name();
            lastSavedState = canvas.exportCanvasState();
        } catch (IOException | IllegalArgumentException failure) {
            GlassNotices.showError(parent, "Can't open canvas", failure.getMessage());
        }
    }

    /** A rename updates metadata only; it does not discard or overwrite unsaved ink. */
    private void rename(SavedCanvas chosen) {
        String name = GlassDialogs.input(parent, "Rename Canvas", "Name", chosen.name(), "Rename");
        if (name == null) return;
        if (name.isBlank()) {
            GlassDialogs.warning(parent, "Please provide a canvas name.");
            return;
        }
        try {
            SavedCanvas renamed = workspace.rename(chosen.id(), name);
            if (workspace.currentId().filter(chosen.id()::equals).isPresent()) {
                currentName = renamed.name();
            }
            GlassNotices.showSuccess(parent, "Renamed to “" + renamed.name() + "”");
        } catch (IOException | IllegalArgumentException failure) {
            GlassNotices.showError(parent, "Can't rename canvas", failure.getMessage());
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
