package pdf_editor.adapter;

import history.domain.HistoryEntry;
import interface_adapter.history.HistoryItem;
import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class HistoryLibraryImporterTest {
    @Test void calculationImportsStructuredFormulaAndAnswerRatherThanScreenshot() {
        MathLibrary library = new MathLibrary();
        HistoryItem item = new HistoryItem(new HistoryEntry("calc1", HistoryEntry.Kind.CALCULATION,
                "Calculation", "2+3", "5", null, 0, 0, Instant.EPOCH));
        var material = HistoryLibraryImporter.importRecord(item, library);
        assertEquals("2+3", material.formula());
        assertEquals("5", material.result());
        assertFalse(material.isGraph());
        assertEquals(1, library.items().size());
    }

    @Test void graphKeepsOriginalImageResolutionAndPixels() throws Exception {
        BufferedImage image = new BufferedImage(340, 190, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(12, 15, 0xff224466);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertTrue(ImageIO.write(image, "png", out));
        var item = new HistoryItem(new HistoryEntry("graph1", HistoryEntry.Kind.GRAPH,
                "Graph", "y=x", "", out.toByteArray(), image.getWidth(), image.getHeight(), Instant.EPOCH));
        var material = HistoryLibraryImporter.importRecord(item, new MathLibrary());
        assertTrue(material.isGraph());
        assertEquals(340, material.imageWidth());
        assertEquals(190, material.imageHeight());
        BufferedImage restored = ImageIO.read(new java.io.ByteArrayInputStream(material.imagePng()));
        assertEquals(0xff224466, restored.getRGB(12, 15));
    }
}
