package pdf_editor.adapter;

import pdf_editor.MathLibrary;
import pdf_editor.domain.PdfDocument;
import pdf_editor.domain.PdfMaterial;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;

import pdf_editor.domain.PdfPreviewPage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Collection;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Objects;

/** Interface adapter translating view events into application use-case commands. No Swing dependency. */
public final class PdfEditorController {
    private final MathLibrary library;
    private final DocumentEditor editor;
    private final AutoLayoutInteractor layout;
    private final PdfExportInteractor output;
    private int page;
    private final PdfSelectionModel selection = new PdfSelectionModel();
    private String selectedId() { return selection.primary(); }
    public Set<String> selectedIds() { return selection.ids(); }
    public boolean isSelected(String id) { return selection.ids().contains(id); }
    /** Select an item without discarding a previous multi-selection only when requested. */
    public void toggleSelection(String id) {
        if (editor.find(page, id) != null) selection.toggle(id);
    }
    public void setSelection(Collection<String> ids, boolean additive) {
        LinkedHashSet<String> valid = new LinkedHashSet<>();
        for (PdfDocument.Placement p : placements()) if (ids.contains(p.id())) valid.add(p.id());
        if (additive) selection.add(valid); else selection.replace(valid);
    }
    public void selectAll() {
        selection.replace(placements().stream().map(PdfDocument.Placement::id).toList());
    }
    public PdfDocument.Placement hitAt(float x, float y) {
        List<PdfDocument.Placement> items = placements();
        for (int i = items.size() - 1; i >= 0; i--) {
            PdfDocument.Placement p = items.get(i);
            if (x >= p.x() && x <= p.x() + p.width() && y >= p.y() && y <= p.y() + p.height()) return p;
        }
        return null;
    }

    public PdfEditorController(MathLibrary library, DocumentEditor editor,
                               AutoLayoutInteractor layout, PdfExportInteractor output) {
        this.library = Objects.requireNonNull(library);
        this.editor = Objects.requireNonNull(editor);
        this.layout = Objects.requireNonNull(layout);
        this.output = Objects.requireNonNull(output);
    }
    public MathLibrary library() { return library; }
    public int currentPage() { return page; }
    public int pageCount() { return editor.pageCount(); }
    public List<PdfDocument.Placement> placements() { return editor.placements(page); }
    public PdfDocument.Placement selected() { return editor.find(page, selectedId()); }
    public void select(String id) { selection.select(editor.find(page, id) == null ? null : id); }
    public void clearSelection() { selection.clear(); }
    public void selectAt(float x, float y) {
        PdfDocument.Placement hit = hitAt(x, y);
        selection.select(hit == null ? null : hit.id());
    }
    public void switchPage(int target) {
        if (target < 0 || target >= pageCount()) return;
        page = target; selection.clear();
    }
    public void addPage() { switchPage(editor.addPage()); }
    public boolean deletePage() {
        if (pageCount() <= 1) return false;
        editor.removePage(page);
        switchPage(Math.min(page, pageCount()-1));
        return true;
    }
    public PdfDocument.Placement place(PdfMaterial material, float x, float y, float width, float height) {
        PdfDocument.Placement placed = editor.place(page, material, x, y, width, height);
        select(placed.id()); return placed;
    }
    public PdfDocument.Placement addNote() { return addNoteAt(75, 95); }
    public PdfDocument.Placement addNoteAt(float x, float y) {
        PdfDocument.Placement placed = editor.addNote(page, "", x, y);
        select(placed.id()); return placed;
    }
    public void updateRichNote(List<PdfDocument.NoteSpan> spans) {
        if (selected() != null && selected().isNote()) editor.updateRichNote(page, selectedId(), spans);
    }
    /** Formatting is an application command, never state directly mutated by the view. */
    public boolean toggleSelectedNoteStyle(pdf_editor.use_case.NoteFormatting.Style style) {
        PdfDocument.Placement note = selected();
        if (note == null || !note.isNote()) return false;
        editor.updateRichNote(page, selectedId(), pdf_editor.use_case.NoteFormatting.toggle(
                note.noteSpans(), note.noteText(), note.noteSize(), style));
        return true;
    }
    public boolean resizeSelectedNote(int fontSize) {
        PdfDocument.Placement note = selected();
        if (note == null || !note.isNote()) return false;
        editor.updateRichNote(page, selectedId(), pdf_editor.use_case.NoteFormatting.resize(
                note.noteSpans(), note.noteText(), note.noteSize(), fontSize));
        return true;
    }
    public boolean changeSelectedNoteFont(String family) {
        PdfDocument.Placement note = selected();
        if (note == null || !note.isNote()) return false;
        editor.updateRichNote(page, selectedId(), pdf_editor.use_case.NoteFormatting.family(
                note.noteSpans(), note.noteText(), note.noteSize(), family));
        return true;
    }
    /** Delete the entire selection in one Undo step (including a box/lasso selection). */
    public boolean deleteSelected() {
        int changed = editor.deleteMany(page, selection.ids());
        if (changed > 0) selection.clear();
        return changed > 0;
    }
    public boolean editSelectedFormula(String formula, String result) {
        PdfDocument.Placement item = selected();
        if (item == null || item.isNote() || item.material().isGraph()) return false;
        editor.editFormula(page, selectedId(), formula, result);
        return true;
    }
    /** Preserve Library source identity; all PDF copies revise together as one Undo operation. */
    public int reviseSelectedFormula(String formula, String result) {
        PdfDocument.Placement current = selected();
        if (current == null || current.isNote() || current.material().isGraph()) return 0;
        return editor.reviseFormulaMaterials(current.material().id(), formula, result);
    }
    /** Updating a Library entry refreshes all placements that originated from it. */
    public int refreshMaterial(String materialId, String formula, String result) {
        return editor.reviseFormulaMaterials(materialId, formula, result);
    }

    /** Apply one precise-eraser gesture to PDF ink, without affecting other PDF objects. */
    public void replaceInkImages(java.util.Map<String, java.awt.image.BufferedImage> images) {
        if (images.isEmpty()) return;
        java.util.Map<String, pdf_editor.domain.PdfMaterial> materials = new java.util.LinkedHashMap<>();
        images.forEach((id, image) -> materials.put(id, MathLibrary.region("PDF ink stroke", image)));
        editor.replaceInkMaterials(page, materials);
    }

    public void arrange(int direction) { if (selected() != null) editor.arrange(page, selectedId(), direction); }
    public boolean splitSelected() {
        if (selected() == null) return false;
        PdfDocument.Placement[] split = editor.splitCalculation(page, selectedId());
        if (split == null) return false;
        selection.select(split[0].id()); return true;
    }
    public void beginGroupTransform(java.util.Set<String> ids) {
        editor.beginGroupTransform(page, ids);
    }
    public void transformGroup(java.util.Map<String, PdfDocument.Placement> originals, float dx, float dy) {
        editor.transformGroup(page, originals, dx, dy);
    }

    public void beginTransform() {
        if (selected() != null) editor.beginTransform(page, selectedId());
    }
    public void transform(float x, float y, float width, float height) {
        if (selected() != null) editor.transform(page, selectedId(), x, y, width, height);
    }
    public boolean moveSelectedToPage(int target) {
        if (selection.ids().isEmpty() || target == page) return false;
        final List<String> ids = placements().stream().map(PdfDocument.Placement::id)
                .filter(selection.ids()::contains).toList();
        final int from = page;
        final int[] destination = {target};
        editor.transaction(() -> {
            if (destination[0] == -1) destination[0] = editor.addPage();
            for (String id : ids) editor.moveToPage(from, destination[0], id);
        });
        switchPage(destination[0]);
        selection.replace(ids);
        return true;
    }

    /** Image positions are currently page-fixed; front/behind changes only the text stacking. */
    public boolean layoutSelectedMaterials(boolean inFrontOfText) {
        return editor.layoutMaterialsRelativeToText(page, selection.ids(), inFrontOfText);
    }
    public int autoLayout() {
        if (library.items().isEmpty()) return 0;
        int count = library.items().size();
        switchPage(layout.importMaterials(page, library.items()));
        return count;
    }
    public boolean undo() {
        if (!editor.undo()) return false;
        switchPage(Math.min(page, pageCount()-1)); return true;
    }
    public boolean redo() {
        if (!editor.redo()) return false;
        switchPage(Math.min(page, pageCount()-1)); return true;
    }
    public List<PdfPreviewPage> preview() throws IOException { return output.preview(editor.snapshot()); }
    public void export(Path destination) throws IOException { output.export(editor.snapshot(), destination); }
    public PdfDocument documentSnapshot() { return editor.snapshot(); }
}
