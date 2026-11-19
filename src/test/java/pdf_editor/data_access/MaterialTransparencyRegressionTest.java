package pdf_editor.data_access;

import org.junit.jupiter.api.Test;
import pdf_editor.MathLibrary;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

/** A material above the Word body must never erase text with an opaque white tile. */
class MaterialTransparencyRegressionTest {
    @Test void formulasAndCalculatedResultsAreTransparentInTheirMargins() {
        MathLibrary library = new MathLibrary();
        var formula = MaterialRenderer.render(library.addFormula("\\frac{x}{2}"), 400, 100);
        var calculation = MaterialRenderer.render(library.addCalculation("x+1", "2"), 400, 100);
        assertTransparentCorners(formula);
        assertTransparentCorners(calculation);
        assertTrue(anyVisiblePixel(formula));
        assertTrue(anyVisiblePixel(calculation));
    }

    @Test void materialLayerDoesNotCoverUnderlyingBodyTextColor() {
        var overlay = MaterialRenderer.render(new MathLibrary().addFormula("x^2"), 320, 90);
        BufferedImage page = new BufferedImage(320, 90, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = page.createGraphics();
        try {
            g.setColor(new Color(255, 0, 180));
            g.fillRect(0, 0, 320, 90);
            g.drawImage(overlay, 0, 0, null);
        } finally { g.dispose(); }
        assertEquals(new Color(255, 0, 180).getRGB(), page.getRGB(0, 0));
        assertEquals(new Color(255, 0, 180).getRGB(), page.getRGB(319, 89));
    }

    @Test void opaqueWhitePlotBecomesTransparentButPreservesBlueLine() {
        BufferedImage chart = new BufferedImage(60, 30, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = chart.createGraphics();
        try {
            g.setColor(Color.WHITE);g.fillRect(0, 0, 60, 30);
            g.setColor(Color.BLUE);g.fillRect(20, 12, 10, 5);
        } finally { g.dispose(); }
        MathLibrary.Material material = MathLibrary.region("graph", chart);
        BufferedImage preview = MaterialRenderer.render(material, 240, 120);
        assertEquals(0, preview.getRGB(0, 0) >>> 24);
        assertEquals(0, preview.getRGB(239, 119) >>> 24);
        assertEquals(Color.BLUE.getRGB(), preview.getRGB(100, 55));
    }

    private static void assertTransparentCorners(BufferedImage image) {
        assertEquals(0, image.getRGB(0, 0) >>> 24);
        assertEquals(0, image.getRGB(image.getWidth()-1, image.getHeight()-1) >>> 24);
    }
    private static boolean anyVisiblePixel(BufferedImage image) {
        for (int y=0;y<image.getHeight();y++) for(int x=0;x<image.getWidth();x++)
            if ((image.getRGB(x, y)>>>24)>100) return true;
        return false;
    }
}
