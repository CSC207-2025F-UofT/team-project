package pdf_editor.use_case;

import pdf_editor.domain.PdfDocument;
import pdf_editor.domain.PdfMaterial;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/** Application interactor: edits documents, enforces placement constraints and owns undo/redo. No UI references. */
public final class DocumentEditor {
    private PdfDocument document = new PdfDocument();
    private final Deque<PdfDocument> undo = new ArrayDeque<>();
    private final Deque<PdfDocument> redo = new ArrayDeque<>();
    private boolean inBatch;

    public PdfDocument snapshot() { return document.copy(); }
    public int pageCount() { return document.pageCount(); }
    public List<PdfDocument.Placement> placements(int page) { return document.placements(page); }
    public PdfDocument.Placement find(int page, String id) { return document.find(page, id); }
    private void checkpoint() {
        if (inBatch) return;
        undo.push(document.copy());
        while (undo.size() > 60) undo.removeLast();
        redo.clear();
    }
    /** Atomic multi-placement edit: Auto Layout is a single Undo action, rolled back on failure. */
    public void transaction(Runnable action) {
        Objects.requireNonNull(action, "action");
        if (inBatch) throw new IllegalStateException("Nested transactions are not allowed");
        PdfDocument before = document.copy();
        inBatch = true;
        try {
            action.run();
            undo.push(before);
            while (undo.size() > 60) undo.removeLast();
            redo.clear();
        } catch (RuntimeException | Error ex) {
            document = before;
            throw ex;
        } finally { inBatch = false; }
    }
    public int addPage() { checkpoint(); return document.addPage(); }
    public void removePage(int page) { if (pageCount() <= 1) throw new IllegalStateException("At least one page is required"); checkpoint(); document.removePage(page); }
    public PdfDocument.Placement place(int page, PdfMaterial material, float x, float y, float width, float height) {
        Objects.requireNonNull(material, "material");
        validatePage(page);
        float w = validSize(width, 45f, PdfDocument.PAGE_WIDTH);
        float h = validSize(height, 35f, PdfDocument.PAGE_HEIGHT);
        // Validate and construct before recording undo. A failed placement must not
        // consume a checkpoint or invalidate an existing redo history.
        PdfDocument.Placement p = PdfDocument.Placement.material(material,
                clamp(x, 0, PdfDocument.PAGE_WIDTH - w), clamp(y, 0, PdfDocument.PAGE_HEIGHT - h), w, h);
        checkpoint();
        document.add(page, p);
        return p;
    }
    public PdfDocument.Placement addNote(int page, String text, float x, float y) {
        validatePage(page);
        PdfDocument.Placement p = PdfDocument.Placement.note(text,
                clamp(x, 0, PdfDocument.PAGE_WIDTH - 260), clamp(y, 0, PdfDocument.PAGE_HEIGHT - 95), 260, 95);
        checkpoint();
        // Text is the default background layer: newly inserted notes must not
        // cover existing History materials, including a full-page body text box.
        // Explicit Arrange commands remain free to change this ordering later.
        List<PdfDocument.Placement> existing = document.placements(page);
        int insertAt = 0;
        while (insertAt < existing.size() && existing.get(insertAt).isNote()) insertAt++;
        document.insert(page, insertAt, p);
        return p;
    }
    public PdfDocument.Placement updateNote(int page, String id, String text, int style, int size) {
        PdfDocument.Placement p = findRequired(page, id);
        if (!p.isNote()) throw new IllegalArgumentException("Not a note");
        checkpoint();
        PdfDocument.Placement updated = p.withNote(text, style, size);
        document.replace(page, updated);
        return updated;
    }
    public PdfDocument.Placement updateRichNote(int page, String id, List<PdfDocument.NoteSpan> spans) {
        PdfDocument.Placement p = findRequired(page, id);
        if (!p.isNote()) throw new IllegalArgumentException("Not a note");
        checkpoint();
        PdfDocument.Placement updated = p.withSpans(spans);
        document.replace(page, updated);
        return updated;
    }
    /** Apply a typeset correction atomically so Undo restores the prior formula and result. */
    public PdfDocument.Placement editFormula(int page, String id, String formula, String result) {
        PdfDocument.Placement current = findRequired(page, id);
        if (current.isNote() || current.material().isGraph())
            throw new IllegalArgumentException("Only typeset formula materials can be edited");
        PdfDocument.Placement updated = current.withMaterial(current.material().withText(formula, result));
        checkpoint();
        document.replace(page, updated);
        return updated;
    }
    /** Revise all placements sharing one material ID in one Undo step. Other objects are untouched. */
    public int reviseFormulaMaterials(String materialId, String formula, String result) {
        Objects.requireNonNull(materialId, "materialId");
        Objects.requireNonNull(formula, "formula");
        Objects.requireNonNull(result, "result");
        if (formula.isBlank()) throw new IllegalArgumentException("Formula required");
        int count = 0;
        for (int page = 0; page < pageCount(); page++) {
            for (PdfDocument.Placement item : placements(page)) {
                if (!item.isNote() && !item.material().isGraph() &&
                        item.material().id().equals(materialId)) count++;
            }
        }
        if (count == 0) return 0;
        checkpoint();
        for (int page = 0; page < pageCount(); page++) {
            for (PdfDocument.Placement item : placements(page)) {
                if (!item.isNote() && !item.material().isGraph() &&
                        item.material().id().equals(materialId)) {
                    document.replace(page, item.withMaterial(item.material().revised(formula.trim(), result.trim())));
                }
            }
        }
        return count;
    }

    /** Replace raster ink in place, preserving placement identity and stacking order. One Undo step. */
    public void replaceInkMaterials(int page, java.util.Map<String, PdfMaterial> replacements) {
        validatePage(page);
        if (replacements.isEmpty()) return;
        for (var entry : replacements.entrySet()) {
            PdfDocument.Placement current = findRequired(page, entry.getKey());
            if (current.isNote() || !current.material().isGraph()
                    || !"PDF ink stroke".equals(current.material().title()))
                throw new IllegalArgumentException("Only PDF ink strokes can be erased");
            Objects.requireNonNull(entry.getValue());
        }
        checkpoint();
        for (var entry : replacements.entrySet()) {
            document.replace(page, findRequired(page, entry.getKey()).withMaterial(entry.getValue()));
        }
    }

    public void arrange(int page, String id, int direction) {
        validatePage(page);
        int index = document.indexOf(page, id);
        if (index < 0) return;
        int count = placements(page).size();
        int target = direction == 2 ? count - 1 : direction == -2 ? 0 : index + direction;
        target = Math.max(0, Math.min(count - 1, target));
        if (target == index) return;
        checkpoint(); document.reorder(page, id, target);
    }
    public PdfDocument.Placement[] splitCalculation(int page, String id) {
        PdfDocument.Placement original = find(page, id);
        if (original == null || original.isNote() || original.material().isGraph()
                || !original.material().hasResult()) return null;
        checkpoint();
        int index = document.indexOf(page, id);
        float leftWidth = Math.max(45, original.width() * .52f);
        float rightWidth = Math.max(45, original.width() - leftWidth);
        // Leave the sole equals sign on the result half. Formula has none.
        PdfDocument.Placement formula = PdfDocument.Placement.material(
                PdfMaterial.formulaPart(original.material()), original.x(), original.y(),
                leftWidth, original.height());
        PdfDocument.Placement result = PdfDocument.Placement.material(
                PdfMaterial.resultPart(original.material()), original.x() + leftWidth,
                original.y(), rightWidth, original.height());
        document.remove(page, id);
        document.insert(page, index, formula);
        document.insert(page, index + 1, result);
        return new PdfDocument.Placement[] { formula, result };
    }
    public boolean moveToPage(int from, int to, String id) {
        validatePage(from); validatePage(to);
        PdfDocument.Placement p = document.find(from, id);
        if (from == to || p == null) return false;
        checkpoint();
        document.remove(from, id);
        document.add(to, p.withBounds(
                clamp(p.x(), 0, PdfDocument.PAGE_WIDTH - p.width()),
                clamp(p.y(), 0, PdfDocument.PAGE_HEIGHT - p.height()), p.width(), p.height()));
        return true;
    }
    public boolean delete(int page, String id) {
        if (document.find(page, id) == null) return false;
        checkpoint(); return document.remove(page, id);
    }
    /** An entire multi-selection is one atomic, undoable deletion. */
    public int deleteMany(int page, java.util.Collection<String> ids) {
        validatePage(page);
        Objects.requireNonNull(ids);
        java.util.LinkedHashSet<String> existing = new java.util.LinkedHashSet<>();
        for (PdfDocument.Placement p : placements(page)) if (ids.contains(p.id())) existing.add(p.id());
        if (existing.isEmpty()) return 0;
        checkpoint();
        for (String id : existing) document.remove(page, id);
        return existing.size();
    }

    /** Adjust PDF z-order relative to text without creating a second rendering/layout model.
     * Fixed page coordinates and original relative image order are preserved. */
    public boolean layoutMaterialsRelativeToText(int page, java.util.Collection<String> ids, boolean front) {
        validatePage(page);
        Objects.requireNonNull(ids);
        List<PdfDocument.Placement> original = placements(page);
        List<PdfDocument.Placement> moved = new java.util.ArrayList<>();
        List<PdfDocument.Placement> rest = new java.util.ArrayList<>();
        for (PdfDocument.Placement p : original) {
            if (!p.isNote() && ids.contains(p.id())) moved.add(p);
            else rest.add(p);
        }
        if (moved.isEmpty()) return false;
        int position = front ? rest.size() : 0;
        if (front) {
            for (int i=0; i<rest.size(); i++) if (rest.get(i).isNote()) position = i + 1;
        } else {
            for (int i=0; i<rest.size(); i++) if (rest.get(i).isNote()) { position = i; break; }
        }
        rest.addAll(position, moved);
        if (rest.equals(original)) return false;
        checkpoint();
        for (int i=0; i<rest.size(); i++) document.reorder(page, rest.get(i).id(), i);
        return true;
    }
    /** One checkpoint per drag gesture, not per mouse movement. */
    /** Start a grouped selection move as one Undo operation. */
    public void beginGroupTransform(int page, java.util.Set<String> ids) {
        if (ids.isEmpty()) return;
        for (String id : ids) findRequired(page, id);
        checkpoint();
    }
    /** Live group move uses the checkpoint created at gesture start. */
    public void transformGroup(int page, java.util.Map<String, PdfDocument.Placement> originals,
                               float dx, float dy) {
        validatePage(page);
        for (var entry : originals.entrySet()) {
            PdfDocument.Placement original = entry.getValue();
            PdfDocument.Placement current = findRequired(page, entry.getKey());
            float x = clamp(original.x() + dx, 0, PdfDocument.PAGE_WIDTH - current.width());
            float y = clamp(original.y() + dy, 0, PdfDocument.PAGE_HEIGHT - current.height());
            document.replace(page, current.withBounds(x, y, current.width(), current.height()));
        }
    }

    public void beginTransform(int page, String id) { findRequired(page, id); checkpoint(); }
    public PdfDocument.Placement transform(int page, String id, float x, float y, float width, float height) {
        PdfDocument.Placement p = findRequired(page, id);
        float w = validSize(width, 45, PdfDocument.PAGE_WIDTH);
        float h = validSize(height, 35, PdfDocument.PAGE_HEIGHT);
        PdfDocument.Placement updated = p.withBounds(
                clamp(x, 0, PdfDocument.PAGE_WIDTH - w),
                clamp(y, 0, PdfDocument.PAGE_HEIGHT - h), w, h);
        document.replace(page, updated);
        return updated;
    }
    public boolean undo() {
        if (undo.isEmpty()) return false;
        redo.push(document.copy()); document = undo.pop(); return true;
    }
    public boolean redo() {
        if (redo.isEmpty()) return false;
        undo.push(document.copy()); document = redo.pop(); return true;
    }
    private PdfDocument.Placement findRequired(int page, String id) {
        PdfDocument.Placement p = document.find(page, id);
        if (p == null) throw new IllegalArgumentException("Unknown placement: " + id);
        return p;
    }
    private void validatePage(int page) { document.placements(page); }
    private static float validSize(float v, float min, float max) {
        if (!Float.isFinite(v)) throw new IllegalArgumentException("Size must be finite");
        return clamp(v, min, max);
    }
    private static float clamp(float v, float low, float high) {
        if (!Float.isFinite(v)) throw new IllegalArgumentException("Coordinate must be finite");
        return Math.max(low, Math.min(high, v));
    }
}
