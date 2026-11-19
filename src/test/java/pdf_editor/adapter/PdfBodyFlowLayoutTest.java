package pdf_editor.adapter;

import org.junit.jupiter.api.Test;
import pdf_editor.domain.PdfDocument;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/** Character-accurate rich body flow. No drawing canvas or recognition modules involved. */
class PdfBodyFlowLayoutTest {
    @Test void richSpansSurviveMultiPageFlowWithoutTruncatingText() {
        var first = new PdfDocument.NoteSpan("First paragraph.\n",true,false,false,20,"Georgia");
        var rest = new PdfDocument.NoteSpan("Second section of text.\n".repeat(150),
                false,true,true,16,"Arial");
        var input = List.of(first,rest);
        var pages = PdfBodyFlowLayout.paginate(input);
        assertTrue(pages.size() >= 2, "Long text must flow onto another page");
        var merged = PdfBodyFlowLayout.concat(pages);
        assertEquals(PdfBodyFlowLayout.plainText(input), PdfBodyFlowLayout.plainText(merged));
        assertTrue(merged.stream().anyMatch(s -> s.bold() && s.fontFamily().equals("Georgia")));
        assertTrue(merged.stream().anyMatch(s -> s.italic() && s.underline() && s.fontFamily().equals("Arial")));
    }
    @Test void emptyStoryNeverCreatesEmptyExtraPages() {
        assertEquals(List.of(List.of()), PdfBodyFlowLayout.paginate(List.of()));
        assertEquals("", PdfBodyFlowLayout.plainText(PdfBodyFlowLayout.concat(
                PdfBodyFlowLayout.paginate(List.of()))));
    }
    @Test void bodyTextIsNotAnOrdinarySelectableImagePlacement() {
        var doc = new PdfDocument();
        doc.setBody(0, List.of(new PdfDocument.NoteSpan("Heading",true,false,false,22)));
        assertEquals(0, doc.placements(0).size());
        assertEquals("Heading", PdfBodyFlowLayout.plainText(doc.copy().body(0)));
    }
}
