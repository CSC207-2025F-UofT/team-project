package view.board;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import interface_adapter.history.HistoryViewModel;
import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import pdf_editor.adapter.PdfEditorController;
import pdf_editor.use_case.AutoLayoutInteractor;
import pdf_editor.use_case.DocumentEditor;
import pdf_editor.use_case.PdfExportInteractor;
import view.DrawingCanvas;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryPdfDropAndPopupRegressionTest {
    private static HistoryItem calculation() {
        return new HistoryItem(new HistoryEntry("calc-1", HistoryEntry.Kind.CALCULATION,
                "Calculation", "2+3", "5", null, 0, 0, Instant.EPOCH));
    }

    private static PdfPanel pdf(MathLibrary library, DocumentEditor document) {
        return new PdfPanel(new PdfEditorController(library, document,
                new AutoLayoutInteractor(document),
                new PdfExportInteractor((doc, target) -> { }, (doc, dpi) -> List.of())));
    }

    @Test void droppedHistoryCalculationIsPlacedAndAutomaticallyImportedIntoLibrary() {
        MathLibrary library = new MathLibrary();
        DocumentEditor document = new DocumentEditor();
        PdfPanel pdf = pdf(library, document);
        assertTrue(pdf.dropHistoryItem(calculation(), new Point(400, 240)));
        assertEquals(1, library.items().size(), "PDF drops must also appear in the Library");
        var item = library.items().get(0);
        assertEquals("2+3", item.formula());
        assertEquals("5", item.result());
        assertEquals(1, document.placements(0).size());
        assertEquals(item.id(), document.placements(0).get(0).material().id());
    }

    @Test void droppedHistoryGraphUsesOriginalPngInBothPlaces() throws Exception {
        BufferedImage image = new BufferedImage(320, 180, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(5, 7, 0xff129abc);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        assertTrue(ImageIO.write(image, "png", bytes));
        HistoryItem graph = new HistoryItem(new HistoryEntry("graph-1", HistoryEntry.Kind.GRAPH,
                "Graph", "y=x^2", "", bytes.toByteArray(), 320, 180, Instant.EPOCH));
        MathLibrary library = new MathLibrary();
        DocumentEditor document = new DocumentEditor();
        PdfPanel pdf = pdf(library, document);
        assertTrue(pdf.dropHistoryItem(graph, new Point(400, 240)));
        assertEquals(1, library.items().size());
        var item = library.items().get(0);
        assertTrue(item.isGraph());
        assertEquals(320, item.imageWidth());
        assertEquals(180, item.imageHeight());
        assertEquals(0xff129abc, ImageIO.read(new java.io.ByteArrayInputStream(item.imagePng())).getRGB(5, 7));
        assertEquals(1, document.placements(0).size());
        assertEquals(item.id(), document.placements(0).get(0).material().id(),
                "PDF must use the same imported Library material, not a second screenshot copy");
    }

    @Test void pdfDropHitTestingUsesVisibleBoardRegionNotInnerPaperShowingState() {
        DrawingCanvas canvas = new DrawingCanvas();
        JPanel page = new JPanel();
        PdfPanel pdf = pdf(new MathLibrary(), new DocumentEditor());
        BoardWindow board = new BoardWindow(page, canvas,
                host -> new HistoryPanel(new HistoryViewModel(), host), pdf);
        board.setSize(1200, 720);
        board.setHistoryOpen(true);
        board.setPdfOpen(true);
        board.doLayout();
        var rect = pdf.getBounds();
        assertTrue(rect.width > 0);
        assertTrue(board.isPdfDropTarget(new Point(rect.x + rect.width / 2, rect.y + rect.height / 2)));
        assertFalse(board.isPdfDropTarget(new Point(10, 200)));
    }

    @Test void popupShadowIsTransparentAndSpacingUsesVisibleToolbarCapsule() throws Exception {
        assertTrue(ToolOptionsPopup.TOOLBAR_GAP < 9,
                "Popover should be much closer to the visible capsule");
        ToolOptionsPopup popup = new ToolOptionsPopup(new DrawingCanvas(), c -> { });
        Method rebuild = ToolOptionsPopup.class.getDeclaredMethod("rebuild", DrawingCanvas.Tool.class);
        rebuild.setAccessible(true);
        rebuild.invoke(popup, DrawingCanvas.Tool.ERASER);
        JComponent surface = (JComponent) popup.getComponent(0);
        var size = surface.getPreferredSize();
        surface.setSize(size);
        BufferedImage buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = buffer.createGraphics();
        surface.paint(g);
        g.dispose();
        // Below the rounded card, away from the small centered triangle:
        // no dark painted shadow / opaque native rectangle should remain.
        int x = size.width / 2 + 60;
        int y = size.height - 14;
        assertEquals(0, buffer.getRGB(x, y) >>> 24,
                "Bottom margin must be transparent rather than a dark drop shadow");
    }
}
