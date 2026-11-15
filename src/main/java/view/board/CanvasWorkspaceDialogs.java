package view.board;

import view.DrawingCanvas;
import workspace.domain.SavedCanvas;
import view.glass.GlassDialogs;
import workspace.use_case.CanvasWorkspaceBoundary;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/** Swing-only dialog adapter. File I/O and workspace naming rules remain in the use case. */
public final class CanvasWorkspaceDialogs {
    private final CanvasWorkspaceBoundary workspace;
    private final DrawingCanvas canvas;
    private final Component parent;
    public CanvasWorkspaceDialogs(CanvasWorkspaceBoundary workspace, DrawingCanvas canvas, Component parent) {
        this.workspace = Objects.requireNonNull(workspace);
        this.canvas = Objects.requireNonNull(canvas);
        this.parent = Objects.requireNonNull(parent);
    }
    public void save() {
        try {
            boolean existing = workspace.currentId().isPresent();
            String currentName = workspace.all().stream()
                    .filter(item -> workspace.currentId().orElse("").equals(item.id()))
                    .map(SavedCanvas::name).findFirst().orElse("Untitled canvas");
            boolean saveAs = !existing;
            if (existing) {
                int choice = GlassDialogs.options(parent, "Save canvas",
                        "Save the current editable canvas?", "Save", "Save As…", "Cancel");
                if (choice == 0) saveAs = false;
                else if (choice == 1) saveAs = true;
                else return;
            }
            String name = currentName;
            if (saveAs) {
                name = GlassDialogs.input(parent, "Save canvas as", "Canvas name:", currentName, "Save");
                if (name == null) return;
                if (name.isBlank()) { message("Please provide a canvas name."); return; }
            }
            SavedCanvas saved = workspace.save(canvas.exportCanvasState(), name, saveAs);
            message("Saved: " + saved.name());
        } catch (IOException | IllegalArgumentException ex) { GlassDialogs.error(parent, "Unable to save canvas: " + ex.getMessage()); }
    }
    public void open() {
        try {
            List<SavedCanvas> documents = workspace.all();
            if (documents.isEmpty()) { message("No saved canvases yet. Use Save to create one."); return; }
            DefaultListModel<SavedCanvas> model = new DefaultListModel<>();
            documents.forEach(model::addElement);
            JList<SavedCanvas> list = new JList<>(model);
            list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            list.setSelectedIndex(0);
            DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
            list.setCellRenderer((items, item, index, selected, focused) -> {
                JLabel label = new JLabel(item.name() + "    ·    " + date.format(item.modifiedAt()));
                label.setFont(BoardTheme.font(13));
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(9,12,9,12));
                label.setBackground(selected ? new Color(228,238,255) : Color.WHITE);
                return label;
            });
            JScrollPane scroll = new JScrollPane(list);
            scroll.setPreferredSize(new Dimension(380, 235));
            if (!GlassDialogs.choose(parent, "Open saved canvas", scroll, "Open")
                    || list.getSelectedValue() == null) return;
            if (!canvas.isBlank() && !GlassDialogs.confirm(parent, "Open canvas",
                    "Opening another canvas replaces the current unsaved drawing. Continue?",
                    "Open")) return;
            canvas.restoreCanvasState(workspace.open(list.getSelectedValue().id()).state());
        } catch (IOException e) { GlassDialogs.error(parent, "Unable to open canvas: " + e.getMessage()); }
    }
    public void restoreLatestAtStartup() {
        try {
            var recent = workspace.mostRecent();
            if (recent.isPresent()) canvas.restoreCanvasState(workspace.open(recent.get().id()).state());
        } catch (IOException | RuntimeException failure) {
            // Never overwrite an existing on-screen drawing or crash the application for a bad file.
            System.err.println("Unable to restore saved canvas: " + failure.getMessage());
        }
    }
    private void message(String text) { GlassDialogs.info(parent, text); }
}
