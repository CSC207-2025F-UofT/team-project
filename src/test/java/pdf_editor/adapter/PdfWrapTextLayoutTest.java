package pdf_editor.adapter;

import org.junit.jupiter.api.Test;
import pdf_editor.domain.PdfDocument;
import pdf_editor.domain.PdfMaterial;
import pdf_editor.use_case.DocumentEditor;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/** Word-style rectangular wrapping must change actual glyph positions and PDF page flow. */
class PdfWrapTextLayoutTest {
    private static List<PdfDocument.NoteSpan> story() {
        return List.of(new PdfDocument.NoteSpan("The graph demonstrates a changing function. ".repeat(160),
                false, false, false, 16, "Serif"));
    }
    private static PdfMaterial picture() { return new PdfMaterial("Plot", "x^2", "", null, 0, 0) { }; }

    @Test void textFlowsBesideWrappedPictureAndResumesFullWidthBelowIt() {
        var document = new PdfDocument();
        var image = PdfDocument.Placement.material(picture(),90,120,200,150)
                .withLayout(PdfDocument.ImageLayer.WRAP_TEXT,
                        PdfDocument.AnchorMode.FIXED_ON_PAGE,-1);
        document.add(0,image);
        var page = PdfWrapTextLayout.page(story(),0,document.placements(0));
        assertTrue(page.lines().stream().anyMatch(line -> line.top()>120 && line.top()<260
                && !line.segments().isEmpty() && line.segments().get(0).x()>=299),
                "Text must be repositioned beside the image, not drawn over it");
        assertTrue(page.lines().stream().anyMatch(line -> line.top()>290
                && !line.segments().isEmpty() && line.segments().get(0).x()<90),
                "After the picture, lines should use the full width again");
        BufferedImage render = PdfWrapTextLayout.render(story(),document.placements(0),902,1396);
        assertNotNull(render);
    }

    @Test void allRichTextSurvivesWrappingAndPagination() {
        var document = new PdfDocument();
        document.add(0,PdfDocument.Placement.material(picture(),85,80,320,550)
                .withLayout(PdfDocument.ImageLayer.WRAP_TEXT,
                        PdfDocument.AnchorMode.FIXED_ON_PAGE,-1));
        var input=List.of(new PdfDocument.NoteSpan("Heading\n",true,false,false,26,"Georgia"),
                new PdfDocument.NoteSpan("A complicated description with long lines. ".repeat(180),
                        false,true,true,14,"Arial"));
        var pages = PdfWrapTextLayout.paginate(input,document,0);
        assertTrue(pages.size()>1);
        assertEquals(PdfBodyFlowLayout.plainText(input),
                PdfBodyFlowLayout.plainText(PdfBodyFlowLayout.concat(pages)));
        assertTrue(pages.stream().flatMap(List::stream).anyMatch(s -> s.underline() && s.italic()));
    }

    @Test void wrappingOptionAndReflowAreOneUndoableEdit() {
        var editor = new DocumentEditor();
        var image = editor.place(0,picture(),90,115,210,130);
        editor.updateFlowingBody(0,PdfBodyFlowLayout.paginate(story()));
        int originalPages=editor.pageCount();
        var draft = editor.snapshot();
        draft.replace(0,image.withLayout(PdfDocument.ImageLayer.WRAP_TEXT,
                PdfDocument.AnchorMode.FIXED_ON_PAGE,-1));
        var flowed=PdfWrapTextLayout.paginate(story(),draft,0);
        assertTrue(editor.imageLayoutAndReflow(0, Set.of(image.id()),
                PdfDocument.ImageLayer.WRAP_TEXT,flowed));
        assertEquals(PdfDocument.ImageLayer.WRAP_TEXT,editor.find(0,image.id()).imageLayer());
        assertTrue(editor.undo());
        assertEquals(PdfDocument.ImageLayer.IN_FRONT_OF_TEXT,editor.find(0,image.id()).imageLayer());
        assertEquals(originalPages,editor.pageCount());
    }
}
