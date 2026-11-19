package pdf_editor.adapter;

import pdf_editor.domain.PdfDocument;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Presentation adapter: uses Swing's actual styled-line layout to split a continuous
 * body into printable A4 pages. The domain stores only NoteSpan values, never JTextPane.
 * Both PDF preview and export consume these same immutable body pages.
 */
public final class PdfBodyFlowLayout {
    public static final float LEFT = PdfDocument.BODY_LEFT;
    public static final float TOP = PdfDocument.BODY_TOP;
    public static final float WIDTH = PdfDocument.BODY_WIDTH;
    public static final float HEIGHT = PdfDocument.BODY_HEIGHT;
    private PdfBodyFlowLayout() { }

    public static PdfDocument.Placement displayNote(List<PdfDocument.NoteSpan> spans) {
        return PdfDocument.bodyPlacement(spans);
    }

    public static String plainText(List<PdfDocument.NoteSpan> spans) {
        StringBuilder text = new StringBuilder();
        for (PdfDocument.NoteSpan span : spans) text.append(span.text());
        return text.toString();
    }

    public static List<PdfDocument.NoteSpan> concat(List<List<PdfDocument.NoteSpan>> parts) {
        List<PdfDocument.NoteSpan> combined = new ArrayList<>();
        for (List<PdfDocument.NoteSpan> part : parts) combined.addAll(part);
        return combined;
    }

    /** Slice styled runs on character boundaries without discarding formatting. */
    public static List<PdfDocument.NoteSpan> slice(List<PdfDocument.NoteSpan> source, int from, int to) {
        if (from < 0 || to < from) throw new IllegalArgumentException("Invalid range");
        List<PdfDocument.NoteSpan> result = new ArrayList<>();
        int offset = 0;
        for (PdfDocument.NoteSpan span : source) {
            int length = span.text().length();
            int a = Math.max(0, from - offset), b = Math.min(length, to - offset);
            if (b > a) result.add(new PdfDocument.NoteSpan(span.text().substring(a, b),
                    span.bold(), span.italic(), span.underline(), span.size(), span.fontFamily()));
            offset += length;
        }
        if (to > offset) throw new IllegalArgumentException("Range past end of text");
        return List.copyOf(result);
    }

    /** Pages contain no extra hard page-break characters. */
    public static List<List<PdfDocument.NoteSpan>> paginate(List<PdfDocument.NoteSpan> spans) {
        Objects.requireNonNull(spans);
        String source = plainText(spans);
        if (source.isEmpty()) return List.of(List.of());
        JTextPane pane = NoteTextDocumentFactory.createPane(displayNote(spans));
        pane.setSize((int) WIDTH, Math.max(100_000, source.length() * 3));
        pane.getPreferredSize();
        List<List<PdfDocument.NoteSpan>> result = new ArrayList<>();
        int start = 0;
        int lineStart = 0;
        double pageStartY = 0;
        try {
            Rectangle2D first = pane.modelToView2D(0);
            if (first != null) pageStartY = first.getY();
            int guard = 0;
            while (lineStart < source.length() && guard++ < source.length() + 10) {
                Rectangle2D rect = pane.modelToView2D(lineStart);
                if (rect != null && rect.getY() + rect.getHeight() - pageStartY > HEIGHT - 9 && lineStart > start) {
                    result.add(slice(spans, start, lineStart));
                    start = lineStart;
                    pageStartY = rect.getY();
                }
                int lineEnd = Utilities.getRowEnd(pane, lineStart);
                int next = lineEnd >= lineStart ? Math.min(source.length(), lineEnd + 1) : lineStart + 1;
                lineStart = Math.max(lineStart + 1, next);
            }
        } catch (BadLocationException bad) { throw new IllegalStateException("Cannot lay out document text", bad); }
        result.add(slice(spans, start, source.length()));
        return List.copyOf(result);
    }

    /** Update text anchors from a single changed story segment.  Only the
     * presentation adapter knows line heights, never the domain/use case. */
    public static java.util.Map<String, pdf_editor.use_case.DocumentEditor.BodyAnchorMove> reanchor(
            PdfDocument before, int firstPage, List<List<PdfDocument.NoteSpan>> replacement) {
        List<List<PdfDocument.NoteSpan>> previous = new ArrayList<>();
        for (int i = firstPage; i < before.pageCount(); i++) previous.add(before.body(i));
        String oldText = plainText(concat(previous));
        String newText = plainText(concat(replacement));
        int prefix = 0;
        while (prefix < oldText.length() && prefix < newText.length() &&
                oldText.charAt(prefix) == newText.charAt(prefix)) prefix++;
        int suffix = 0;
        while (suffix < oldText.length()-prefix && suffix < newText.length()-prefix &&
                oldText.charAt(oldText.length()-suffix-1) == newText.charAt(newText.length()-suffix-1)) suffix++;
        int oldEnd = oldText.length() - suffix;
        int delta = newText.length() - oldText.length();
        java.util.Map<String, pdf_editor.use_case.DocumentEditor.BodyAnchorMove> result = new java.util.LinkedHashMap<>();
        int cumulative = 0;
        for (int page = firstPage; page < before.pageCount(); page++) {
            List<PdfDocument.NoteSpan> oldPage = before.body(page);
            for (PdfDocument.Placement item : before.placements(page)) {
                if (item.isNote() || item.anchorMode() != PdfDocument.AnchorMode.MOVE_WITH_TEXT) continue;
                int index = cumulative + Math.max(0, item.bodyAnchorOffset());
                int updated = index <= prefix ? index : index >= oldEnd ? index+delta : prefix;
                updated = Math.max(0, Math.min(newText.length(), updated));
                int newPage = firstPage, local = updated;
                for (int part = 0; part < replacement.size()-1; part++) {
                    int len = plainText(replacement.get(part)).length();
                    if (local < len) break;
                    local -= len; newPage++;
                }
                int newPart = Math.max(0, Math.min(replacement.size()-1, newPage-firstPage));
                float anchorY = yAtOffset(replacement.get(newPart), local);
                float oldY = yAtOffset(oldPage, Math.max(0, item.bodyAnchorOffset()));
                float y = anchorY + item.y() - oldY;
                result.put(item.id(), new pdf_editor.use_case.DocumentEditor.BodyAnchorMove(
                        newPage, local, y));
            }
            cumulative += plainText(oldPage).length();
        }
        return result;
    }

    /** Character position nearest the target page-local Y (for anchoring a picture to text). */
    public static int offsetAtY(List<PdfDocument.NoteSpan> body, float pageY) {
        if (plainText(body).isEmpty()) return 0;
        JTextPane pane = NoteTextDocumentFactory.createPane(displayNote(body));
        pane.setSize((int) WIDTH, (int) HEIGHT);
        pane.getPreferredSize();
        return Math.max(0, Math.min(pane.getDocument().getLength(),
                pane.viewToModel2D(new java.awt.Point(10, Math.max(0, Math.round(pageY - TOP))))));
    }

    /** Y of an anchored character relative to the printable page, independently of images. */
    public static float yAtOffset(List<PdfDocument.NoteSpan> body, int offset) {
        if (plainText(body).isEmpty()) return TOP;
        JTextPane pane = NoteTextDocumentFactory.createPane(displayNote(body));
        pane.setSize((int) WIDTH, (int) HEIGHT);
        pane.getPreferredSize();
        try {
            Rectangle2D rect = pane.modelToView2D(Math.max(0,
                    Math.min(pane.getDocument().getLength(), offset)));
            return rect == null ? TOP : TOP + (float) rect.getY();
        } catch (BadLocationException ex) { return TOP; }
    }
}
