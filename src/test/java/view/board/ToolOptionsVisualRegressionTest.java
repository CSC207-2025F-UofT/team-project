package view.board;

import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/** Prevents the setting panels rendering as an empty white rectangle on Swing. */
class ToolOptionsVisualRegressionTest {
    private static ToolOptionsPopup build(DrawingCanvas.Tool tool) throws Exception {
        ToolOptionsPopup popup = new ToolOptionsPopup(new DrawingCanvas(), color -> {});
        Method rebuild = ToolOptionsPopup.class.getDeclaredMethod("rebuild", DrawingCanvas.Tool.class);
        rebuild.setAccessible(true);
        rebuild.invoke(popup, tool);
        Dimension size = popup.getPreferredSize();
        popup.setSize(size);
        layoutRecursively(popup);
        return popup;
    }

    private static void layoutRecursively(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) {
            if (child instanceof Container next) layoutRecursively(next);
        }
    }

    private static BufferedImage render(JComponent popup) {
        BufferedImage image = new BufferedImage(popup.getWidth(), popup.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        try { popup.printAll(g); } finally { g.dispose(); }
        return image;
    }

    private static int coloredPixels(BufferedImage image, int x0, int y0, int x1, int y1) {
        int count = 0;
        for (int y = y0; y < Math.min(y1, image.getHeight()); y++) {
            for (int x = x0; x < Math.min(x1, image.getWidth()); x++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >>> 16) & 255, g = (rgb >>> 8) & 255, b = rgb & 255;
                if ((rgb >>> 24) > 200 && Math.max(r, Math.max(g, b))
                        - Math.min(r, Math.min(g, b)) > 55) count++;
            }
        }
        return count;
    }

    @Test void penPreviewHasActualWidthAndVisiblePalette() throws Exception {
        ToolOptionsPopup popup = build(DrawingCanvas.Tool.PEN);
        assertTrue(popup.getWidth() >= 340);
        assertTrue(popup.getHeight() >= 275);
        BufferedImage image = render(popup);
        // The multicolour palette at the bottom must not collapse to zero height.
        assertTrue(coloredPixels(image, 25, popup.getHeight() - 90,
                popup.getWidth() - 25, popup.getHeight() - 14) > 100,
                "Pen color palette must be visibly painted");
        assertTrue(countDarkPixels(image, 40, 30, 330, 100) > 120,
                "Pen preview curve must span the preview card, not collapse to one pixel");
    }

    private static int countDarkPixels(BufferedImage image, int x0, int y0, int x1, int y1) {
        int count = 0;
        for (int y=y0;y<Math.min(y1,image.getHeight());y++) {
            for (int x=x0;x<Math.min(x1,image.getWidth());x++) {
                int rgb=image.getRGB(x,y);
                if ((rgb >>> 24)>200 && ((rgb>>>16)&255)<100
                        && ((rgb>>>8)&255)<100 && (rgb&255)<100) count++;
            }
        }
        return count;
    }

    @Test void eraserAndLassoShowDescriptiveModeCards() throws Exception {
        for (DrawingCanvas.Tool tool : new DrawingCanvas.Tool[]{
                DrawingCanvas.Tool.ERASER, DrawingCanvas.Tool.LASSO,
                DrawingCanvas.Tool.RECTANGLE}) {
            ToolOptionsPopup popup = build(tool);
            assertTrue(popup.getHeight() >= 210);
            assertTrue(countDarkPixels(render(popup), 30, 45, 335, 195) > 100,
                    tool + " must show visible mode names and descriptions");
        }
    }

    @Test void popupIsTransparentOutsideItsRoundedCard() throws Exception {
        ToolOptionsPopup popup = build(DrawingCanvas.Tool.PEN);
        BufferedImage image = render(popup);
        assertFalse(popup.isOpaque());
        assertEquals(0, image.getRGB(0, 0) >>> 24,
                "popup must not paint a square native menu background");
    }
}
