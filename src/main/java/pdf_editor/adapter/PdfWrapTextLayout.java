package pdf_editor.adapter;

import pdf_editor.domain.PdfDocument;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Print-layout adapter for the Word-style body. A wrap-text placement creates a
 * rectangular exclusion in the body, with a small text-to-image gutter.
 * Both the on-screen page and PDFBox exporter render the exact same line layout.
 * No Swing view or PDFBox classes leak into the document domain.
 */
public final class PdfWrapTextLayout {
    private static final float GUTTER = 9f;
    private static final float INSET_X = 7f;
    private static final float INSET_Y = 6f;
    private static final Color INK = new Color(31, 48, 72);
    private PdfWrapTextLayout() { }

    public record Segment(String text, PdfDocument.NoteSpan style, float x) { }
    public record Line(float top, float baseline, float height, List<Segment> segments) { }
    public record Page(int from, int until, List<Line> lines) { }
    private record Glyph(char character, PdfDocument.NoteSpan style) { }
    private record Slot(float x, float width) { }

    private static List<Glyph> glyphs(List<PdfDocument.NoteSpan> spans) {
        List<Glyph> all = new ArrayList<>();
        for (PdfDocument.NoteSpan span : spans)
            for (char c : span.text().toCharArray()) all.add(new Glyph(c, span));
        return all;
    }

    private static Font font(PdfDocument.NoteSpan span) {
        int style = (span.bold() ? Font.BOLD : 0) | (span.italic() ? Font.ITALIC : 0);
        return new Font(span.fontFamily(), style, span.size());
    }

    private static List<Slot> available(float y, float height, List<PdfDocument.Placement> objects) {
        float left = PdfDocument.BODY_LEFT + INSET_X;
        float right = PdfDocument.BODY_LEFT + PdfDocument.BODY_WIDTH - INSET_X;
        List<float[]> exclusions = new ArrayList<>();
        for (PdfDocument.Placement p : objects) {
            if (p.isNote() || p.imageLayer() != PdfDocument.ImageLayer.WRAP_TEXT) continue;
            if (y + height <= p.y() - GUTTER || y >= p.y() + p.height() + GUTTER) continue;
            float from = Math.max(left, p.x() - GUTTER);
            float to = Math.min(right, p.x() + p.width() + GUTTER);
            if (from < to) exclusions.add(new float[] {from, to});
        }
        exclusions.sort(Comparator.comparingDouble(a -> a[0]));
        List<Slot> slots = new ArrayList<>();
        float start = left;
        for (float[] ex : exclusions) {
            if (ex[0] > start) slots.add(new Slot(start, ex[0]-start));
            start = Math.max(start, ex[1]);
        }
        if (start < right) slots.add(new Slot(start, right-start));
        slots.removeIf(s -> s.width() < 20);
        return slots;
    }

    private static Slot preferredSlot(float y, float height, List<PdfDocument.Placement> placements) {
        return available(y, height, placements).stream().max(Comparator.comparingDouble(Slot::width)).orElse(null);
    }

    /** Place as much text as fits on this A4 page, starting at a story offset. */
    public static Page page(List<PdfDocument.NoteSpan> story, int from,
                            List<PdfDocument.Placement> objects) {
        Objects.requireNonNull(story); Objects.requireNonNull(objects);
        List<Glyph> chars = glyphs(story);
        if (from < 0 || from > chars.size()) throw new IndexOutOfBoundsException(from);
        BufferedImage measure = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = measure.createGraphics();
        List<Line> lines = new ArrayList<>();
        int at = from;
        float top = PdfDocument.BODY_TOP + INSET_Y;
        float bottom = PdfDocument.BODY_TOP + PdfDocument.BODY_HEIGHT - INSET_Y;
        try {
            while (at < chars.size()) {
                FontMetrics initial = graphics.getFontMetrics(font(chars.get(at).style()));
                float height = Math.max(12, initial.getHeight() + 2);
                Slot slot = preferredSlot(top, height, objects);
                if (slot == null) {
                    // An image may span the entire text column; resume below it.
                    top += height;
                    if (top + height > bottom) break;
                    continue;
                }
                if (top + height > bottom) break;
                float used = 0;
                int end = at;
                int lastSpace = -1;
                float ascent = initial.getAscent();
                float tallest = height;
                boolean newline = false;
                while (end < chars.size()) {
                    Glyph glyph = chars.get(end);
                    if (glyph.character() == '\n') {newline = true; break;}
                    FontMetrics fm = graphics.getFontMetrics(font(glyph.style()));
                    float advance = fm.charWidth(glyph.character());
                    if (used + advance > slot.width() && end > at) break;
                    if (used + advance > slot.width()) {end++; used += advance; break;}
                    used += advance;
                    ascent = Math.max(ascent, fm.getAscent());
                    tallest = Math.max(tallest, fm.getHeight() + 2);
                    if (Character.isWhitespace(glyph.character())) lastSpace = end;
                    end++;
                }
                if (!newline && end < chars.size() && lastSpace > at) end = lastSpace + 1;
                if (end == at && !newline) end++;
                if (top + tallest > bottom) break;
                List<Segment> fragments = new ArrayList<>();
                float x = slot.x();
                StringBuilder run = new StringBuilder();
                PdfDocument.NoteSpan style = null;
                float runX = x;
                for (int i = at; i < end; i++) {
                    Glyph glyph = chars.get(i);
                    if (style != glyph.style() && style != null) {
                        fragments.add(new Segment(run.toString(), style, runX));
                        run = new StringBuilder(); runX = x;
                    }
                    style = glyph.style();
                    run.append(glyph.character());
                    x += graphics.getFontMetrics(font(style)).charWidth(glyph.character());
                }
                if (style != null && !run.isEmpty()) fragments.add(new Segment(run.toString(), style, runX));
                lines.add(new Line(top, top + ascent, tallest, List.copyOf(fragments)));
                at = newline ? end + 1 : end;
                top += tallest;
            }
        } finally { graphics.dispose(); }
        return new Page(from, at, List.copyOf(lines));
    }

    /** Reflow the whole story, taking each page's actual wrap-text images into account. */
    public static List<List<PdfDocument.NoteSpan>> paginate(List<PdfDocument.NoteSpan> story,
                                                              PdfDocument snapshot, int firstPage) {
        Objects.requireNonNull(snapshot);
        String text = PdfBodyFlowLayout.plainText(story);
        if (text.isEmpty()) return List.of(List.of());
        List<List<PdfDocument.NoteSpan>> result = new ArrayList<>();
        int offset = 0, page = firstPage;
        while (offset < text.length()) {
            List<PdfDocument.Placement> images = page < snapshot.pageCount()
                    ? snapshot.placements(page) : List.of();
            Page layout = page(story, offset, images);
            if (layout.until() <= offset)
                throw new IllegalStateException("Text cannot fit on page " + (page+1));
            result.add(PdfBodyFlowLayout.slice(story, offset, layout.until()));
            offset = layout.until();
            page++;
        }
        return List.copyOf(result);
    }

    /** Raster shared by PDF preview, export and the Swing page. */
    public static BufferedImage render(List<PdfDocument.NoteSpan> spans,
                                       List<PdfDocument.Placement> placements, int width, int height) {
        width = Math.max(8,width); height = Math.max(8,height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.scale(width/(double)PdfDocument.BODY_WIDTH, height/(double)PdfDocument.BODY_HEIGHT);
            g.translate(-PdfDocument.BODY_LEFT,-PdfDocument.BODY_TOP);
            g.setColor(INK);
            for (Line line : page(spans,0,placements).lines()) {
                for (Segment run : line.segments()) {
                    g.setFont(font(run.style()));
                    g.drawString(run.text(),run.x(),line.baseline());
                    if (run.style().underline()) {
                        FontMetrics fm = g.getFontMetrics();
                        float under = line.baseline() + Math.max(1, fm.getDescent()/2f);
                        g.drawLine(Math.round(run.x()),Math.round(under),
                                Math.round(run.x()+fm.stringWidth(run.text())),Math.round(under));
                    }
                }
            }
        } finally { g.dispose(); }
        return image;
    }
}
