package pdf_editor.adapter;

import interface_adapter.history.CanvasHistorySelection;
import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.PdfExportInteractor;

import java.awt.image.BufferedImage;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CanvasHistoryImporterTest {
    private static PdfEditorController controller(MathLibrary library, DocumentEditor document) {
        return new PdfEditorController(library, document, new AutoLayoutInteractor(document),
                new PdfExportInteractor((doc, file) -> { }, (doc, dpi) -> List.of()));
    }
    private static BufferedImage image(int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(5, 5, 0xff458acd);
        return image;
    }

    @Test void calculationCopiedFromCanvasPreservesFormulaAndResult() {
        var library = new MathLibrary();
        var doc = new DocumentEditor();
        var selection = new CanvasHistorySelection(List.of(
                new CanvasHistorySelection.Asset(image(220, 64), "\\frac{1}{2}+2", "2.5", 10, 10, 110, 32)));
        assertEquals(1, CanvasHistoryImporter.importAt(selection, controller(library, doc), 50, 80));
        assertEquals(1, library.items().size());
        assertEquals("\\frac{1}{2}+2", library.items().get(0).formula());
        assertEquals("2.5", library.items().get(0).result());
        assertEquals(1, doc.placements(0).size());
        assertEquals(library.items().get(0).id(), doc.placements(0).get(0).material().id());
    }

    @Test void graphCopiedFromCanvasUsesOriginalImageAndPdfLibrary() throws Exception {
        var library = new MathLibrary();
        var doc = new DocumentEditor();
        var selection = new CanvasHistorySelection(List.of(
                new CanvasHistorySelection.Asset(image(500, 200), "", "", 40, 60, 250, 100)));
        CanvasHistoryImporter.importAt(selection, controller(library, doc), 30, 50);
        assertEquals(1, library.items().size());
        var graph = library.items().get(0);
        assertTrue(graph.isGraph());
        assertEquals(500, graph.imageWidth());
        assertEquals(200, graph.imageHeight());
        var copy = javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(graph.imagePng()));
        assertEquals(0xff458acd, copy.getRGB(5, 5));
        assertEquals(graph.id(), doc.placements(0).get(0).material().id());
    }

    @Test void multiSelectionCreatesTwoIndependentPdfPlacements() {
        var library = new MathLibrary();
        var doc = new DocumentEditor();
        var selection = new CanvasHistorySelection(List.of(
                new CanvasHistorySelection.Asset(image(120, 40), "x+1", "2", 20, 30, 120, 40),
                new CanvasHistorySelection.Asset(image(150, 40), "y+1", "3", 220, 30, 150, 40)));
        assertEquals(2, CanvasHistoryImporter.importAt(selection, controller(library, doc), 40, 70));
        assertEquals(2, doc.placements(0).size());
        assertEquals(2, library.items().size());
        assertTrue(doc.placements(0).get(1).x() > doc.placements(0).get(0).x());
    }
}
