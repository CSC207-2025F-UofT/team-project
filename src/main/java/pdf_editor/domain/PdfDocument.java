package pdf_editor.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/** Domain entity: an ordered set of pages with stable, immutable placement identities. */
public final class PdfDocument {
    public static final float PAGE_WIDTH = 595f;
    public static final float PAGE_HEIGHT = 842f;
    public static final float BODY_LEFT = 72f;
    public static final float BODY_TOP = 72f;
    public static final float BODY_WIDTH = PAGE_WIDTH - 144f;
    public static final float BODY_HEIGHT = PAGE_HEIGHT - 144f;
    /** One immutable view of body runs for both Preview and PDF output. */
    public static Placement bodyPlacement(List<NoteSpan> spans) {
        return Placement.note("", BODY_LEFT, BODY_TOP, BODY_WIDTH, BODY_HEIGHT).withSpans(spans);
    }

    public record NoteSpan(String text, boolean bold, boolean italic, boolean underline, int size, String fontFamily) {
        public NoteSpan(String text, boolean bold, boolean italic, boolean underline, int size) {
            this(text, bold, italic, underline, size, "Times New Roman");
        }
        public NoteSpan {
            Objects.requireNonNull(text, "text");
            Objects.requireNonNull(fontFamily, "fontFamily");
            if (fontFamily.isBlank() || fontFamily.length() > 100) throw new IllegalArgumentException("Invalid font family");
            if (size < 1) throw new IllegalArgumentException("Font size must be positive");
        }
    }

    /** PDF media positioning relative to the independent flowing body text. */
    public enum ImageLayer { IN_FRONT_OF_TEXT, BEHIND_TEXT, WRAP_TEXT }
    public enum AnchorMode { FIXED_ON_PAGE, MOVE_WITH_TEXT }

    public static final class Placement {
        private final String id;
        private final PdfMaterial material;
        private final float x, y, width, height;
        private final String noteText;
        private final List<NoteSpan> noteSpans;
        private final int noteStyle, noteSize;
        private final ImageLayer imageLayer;
        private final AnchorMode anchorMode;
        private final int bodyAnchorOffset;

        private Placement(String id, PdfMaterial material, float x, float y, float width, float height,
                          String noteText, List<NoteSpan> spans, int noteStyle, int noteSize) {
            this(id, material, x, y, width, height, noteText, spans, noteStyle, noteSize,
                    material == null ? ImageLayer.BEHIND_TEXT : ImageLayer.IN_FRONT_OF_TEXT,
                    AnchorMode.FIXED_ON_PAGE, -1);
        }
        private Placement(String id, PdfMaterial material, float x, float y, float width, float height,
                          String noteText, List<NoteSpan> spans, int noteStyle, int noteSize,
                          ImageLayer layer, AnchorMode anchor, int anchorOffset) {
            this.id = Objects.requireNonNull(id, "id");
            if (material == null && noteText == null) throw new IllegalArgumentException("Empty placement");
            if (material != null && noteText != null) throw new IllegalArgumentException("Placement must have one content type");
            if (!Float.isFinite(x) || !Float.isFinite(y) || !Float.isFinite(width) || !Float.isFinite(height)
                    || width <= 0 || height <= 0) throw new IllegalArgumentException("Invalid placement bounds");
            this.material = material;
            this.x = x; this.y = y; this.width = width; this.height = height;
            this.noteText = noteText; this.noteSpans = List.copyOf(spans);
            this.noteStyle = noteStyle; this.noteSize = noteSize;
            this.imageLayer = Objects.requireNonNull(layer);
            this.anchorMode = Objects.requireNonNull(anchor);
            this.bodyAnchorOffset = anchorOffset;
        }
        public static Placement material(PdfMaterial material, float x, float y, float width, float height) {
            return new Placement(UUID.randomUUID().toString(), Objects.requireNonNull(material), x, y, width, height,
                    null, List.of(), 0, 16);
        }
        public static Placement note(String text, float x, float y, float width, float height) {
            return new Placement(UUID.randomUUID().toString(), null, x, y, width, height,
                    Objects.requireNonNull(text, "text"), List.of(), 0, 16);
        }
        public String id() { return id; }
        public PdfMaterial material() { return material; }
        public float x() { return x; }
        public float y() { return y; }
        public float width() { return width; }
        public float height() { return height; }
        public boolean isNote() { return noteText != null; }
        public String noteText() { return noteText; }
        public List<NoteSpan> noteSpans() { return noteSpans; }
        public int noteStyle() { return noteStyle; }
        public int noteSize() { return noteSize; }
        public ImageLayer imageLayer() { return imageLayer; }
        public AnchorMode anchorMode() { return anchorMode; }
        public int bodyAnchorOffset() { return bodyAnchorOffset; }
        public Placement withLayout(ImageLayer layer, AnchorMode mode, int anchorOffset) {
            return new Placement(id, material, x, y, width, height, noteText, noteSpans, noteStyle, noteSize,
                    layer, mode, anchorOffset);
        }
        public Placement withMaterial(PdfMaterial newMaterial) {
            if (isNote()) throw new IllegalStateException("Notes are not formula materials");
            return new Placement(id, Objects.requireNonNull(newMaterial), x, y, width, height,
                    null, List.of(), noteStyle, noteSize, imageLayer, anchorMode, bodyAnchorOffset);
        }
        public Placement withBounds(float newX, float newY, float newWidth, float newHeight) {
            return new Placement(id, material, newX, newY, newWidth, newHeight, noteText, noteSpans, noteStyle, noteSize,
                    imageLayer, anchorMode, bodyAnchorOffset);
        }
        public Placement withNote(String text, int style, int size) {
            if (!isNote()) throw new IllegalStateException("Not a note");
            return new Placement(id, null, x, y, width, height, text, List.of(), style, size, imageLayer, anchorMode, bodyAnchorOffset);
        }
        public Placement withSpans(List<NoteSpan> spans) {
            if (!isNote()) throw new IllegalStateException("Not a note");
            List<NoteSpan> copy = List.copyOf(spans);
            StringBuilder text = new StringBuilder();
            for (NoteSpan span : copy) text.append(span.text());
            return new Placement(id, null, x, y, width, height, text.toString(), copy, noteStyle, noteSize, imageLayer, anchorMode, bodyAnchorOffset);
        }
    }

    private final List<List<Placement>> pages = new ArrayList<>();
    /** Word-like body runs are not selectable floating placements. */
    private final List<List<NoteSpan>> bodies = new ArrayList<>();
    public PdfDocument() { addEmptyPage(); }
    private PdfDocument(boolean empty) { if (!empty) addEmptyPage(); }
    private void addEmptyPage() { pages.add(new ArrayList<>()); bodies.add(List.of()); }
    public List<NoteSpan> body(int page) { return bodies.get(page); }
    public void setBody(int page, List<NoteSpan> spans) { bodies.set(page, List.copyOf(spans)); }
    public int pageCount() { return pages.size(); }
    public List<Placement> placements(int page) { return List.copyOf(pages.get(page)); }
    public List<List<Placement>> pages() {
        List<List<Placement>> out = new ArrayList<>();
        for (List<Placement> p : pages) out.add(List.copyOf(p));
        return List.copyOf(out);
    }
    /** Copy page collections; immutable placements/materials are safe to share. */
    public PdfDocument copy() {
        PdfDocument result = new PdfDocument(true);
        for (List<Placement> page : pages) result.pages.add(new ArrayList<>(page));
        for (List<NoteSpan> body : bodies) result.bodies.add(body);
        return result;
    }
    public int addPage() { addEmptyPage(); return pages.size() - 1; }
    public void removePage(int page) {
        if (pages.size() <= 1) throw new IllegalStateException("At least one page is required");
        pages.remove(page); bodies.remove(page);
    }
    public void add(int page, Placement item) { pages.get(page).add(Objects.requireNonNull(item)); }
    public void insert(int page, int index, Placement item) { pages.get(page).add(index, Objects.requireNonNull(item)); }
    public boolean remove(int page, String id) { return pages.get(page).removeIf(item -> item.id().equals(id)); }
    public Placement find(int page, String id) {
        if (id == null) return null;
        for (Placement p : pages.get(page)) if (p.id().equals(id)) return p;
        return null;
    }
    public int indexOf(int page, String id) {
        List<Placement> items = pages.get(page);
        for (int i=0; i<items.size(); i++) if (items.get(i).id().equals(id)) return i;
        return -1;
    }
    public void replace(int page, Placement placement) {
        int index = indexOf(page, placement.id());
        if (index < 0) throw new IllegalArgumentException("Unknown placement: " + placement.id());
        pages.get(page).set(index, placement);
    }
    public void reorder(int page, String id, int target) {
        int from = indexOf(page, id);
        if (from < 0) throw new IllegalArgumentException("Unknown placement");
        List<Placement> items = pages.get(page);
        Placement p = items.remove(from);
        items.add(Math.max(0, Math.min(items.size(), target)), p);
    }
}
