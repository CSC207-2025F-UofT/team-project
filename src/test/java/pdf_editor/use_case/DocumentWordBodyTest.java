package pdf_editor.use_case;

import org.junit.jupiter.api.Test;
import pdf_editor.domain.PdfDocument;
import pdf_editor.domain.PdfMaterial;
import pdf_editor.adapter.PdfBodyFlowLayout;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

/** Body, positioning, and Undo behave as one PDF-only document edit. */
class DocumentWordBodyTest {
    private static List<PdfDocument.NoteSpan> paragraph(String s) {
        return List.of(new PdfDocument.NoteSpan(s,false,false,false,16));
    }
    @Test void imageBehindAndInFrontOfBodyAreUndoableWithoutChangingCoordinates() {
        DocumentEditor document = new DocumentEditor();
        PdfMaterial image = new PdfMaterial("Example", "x^2", "",null,0,0) {};
        var p = document.place(0,image,110,145,130,65);
        assertEquals(PdfDocument.ImageLayer.IN_FRONT_OF_TEXT,p.imageLayer());
        assertTrue(document.layoutSelectedImages(0,Set.of(p.id()),
                PdfDocument.ImageLayer.BEHIND_TEXT,PdfDocument.AnchorMode.FIXED_ON_PAGE,Map.of()));
        assertEquals(PdfDocument.ImageLayer.BEHIND_TEXT, document.find(0,p.id()).imageLayer());
        assertTrue(document.undo());
        assertEquals(PdfDocument.ImageLayer.IN_FRONT_OF_TEXT, document.find(0,p.id()).imageLayer());
        assertEquals(145f,document.find(0,p.id()).y());
    }
    @Test void moveWithTextActuallyMovesButFixedPositionDoesNot() {
        DocumentEditor doc=new DocumentEditor();
        doc.updateFlowingBody(0,PdfBodyFlowLayout.paginate(paragraph("Top\nAnchor\n")));
        PdfMaterial image=new PdfMaterial("Image","x","",null,0,0){};
        var anchored=doc.place(0,image,90,160,130,65);
        var fixed=doc.place(0,image,300,160,130,65);
        doc.layoutSelectedImages(0,Set.of(anchored.id()),PdfDocument.ImageLayer.IN_FRONT_OF_TEXT,
                PdfDocument.AnchorMode.MOVE_WITH_TEXT,Map.of(anchored.id(),4));
        var before=doc.snapshot();
        var flow=PdfBodyFlowLayout.paginate(paragraph("New paragraph\nMore lines\nTop\nAnchor\n"));
        doc.updateFlowingBody(0,flow,PdfBodyFlowLayout.reanchor(before,0,flow));
        assertTrue(doc.find(0,anchored.id()).y()>160,"Anchored image should move down with preceding text");
        assertEquals(160f,doc.find(0,fixed.id()).y(),"Fixed image retains page Y");
        assertTrue(doc.undo());
        assertEquals(160f,doc.find(0,anchored.id()).y());
    }
    @Test void bodyPagesAndRichTextAreRevertedInOneUndo() {
        var document=new DocumentEditor();
        var rich=paragraph("Writing in the document\n".repeat(180));
        document.updateFlowingBody(0,PdfBodyFlowLayout.paginate(rich));
        assertTrue(document.pageCount()>1);
        assertEquals(PdfBodyFlowLayout.plainText(rich), PdfBodyFlowLayout.plainText(
                PdfBodyFlowLayout.concat(java.util.stream.IntStream.range(0,document.pageCount())
                        .mapToObj(document::body).toList())));
        assertTrue(document.undo());
        assertEquals(1,document.pageCount());
        assertTrue(document.body(0).isEmpty());
    }
}
