package view.canvas;

import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SharedCanvasTest {
    private final DrawingCanvas canvas = new DrawingCanvas();
    private final SharedCanvas shared = new SharedCanvas(canvas);

    /** Draws a straight stroke the way the mouse would. */
    private void drawLine(int x1, int y1, int x2, int y2) {
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, x1, y1, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, x2, y2, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0, x2, y2, 1, false, MouseEvent.BUTTON1));
    }

    private static boolean isWhite(BufferedImage image, int x, int y) {
        return (image.getRGB(x, y) & 0xFFFFFF) == 0xFFFFFF;
    }

    @Test
    void wideFormulaImageIsCroppedToTheInkWithAWhiteMargin() {
        drawLine(100, 50, 300, 60);

        BufferedImage image = shared.captureFormulaImage();

        // (200 + pen 4) x (10 + 4) world at 4x = 816 x 56, + 8 px margin a side, padded to 16s
        assertEquals(832, image.getWidth());
        assertEquals(80, image.getHeight());
        assertTrue(isWhite(image, 0, 0));
        assertTrue(isWhite(image, image.getWidth() - 1, image.getHeight() - 1));
        assertEquals(Color.BLACK.getRGB(), image.getRGB(8 + 102 * 4, 12 + 7 * 4), "middle of the line is ink");
    }

    @Test
    void tallInkIsOnlyPaddedNotWidenedOrStretched() {
        drawLine(50, 0, 50, 300);   // one tall, narrow stroke

        BufferedImage image = shared.captureFormulaImage();

        assertEquals(304 * 4 + 16, image.getHeight());
        assertEquals(32, image.getWidth(), "16 px of pen + margins, no forced landscape");
        assertEquals(Color.BLACK.getRGB(), image.getRGB(16, image.getHeight() / 2), "the ink is centred");
        assertTrue(isWhite(image, 0, image.getHeight() / 2));
        assertTrue(isWhite(image, image.getWidth() - 1, image.getHeight() / 2));
    }

    @Test
    void sizesArePaddedUpToSixteensWithAtLeastEightPixelsOfWhite() {
        drawLine(100, 50, 140, 80);   // 44 x 34 with the pen -> 176 x 136 px of ink
        BufferedImage image = shared.captureFormulaImage();
        assertEquals(192, image.getWidth());
        assertEquals(160, image.getHeight());
        int top = image.getHeight();
        int left = image.getWidth();
        int bottom = -1;
        int right = -1;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (!isWhite(image, x, y)) {
                    top = Math.min(top, y);
                    left = Math.min(left, x);
                    bottom = Math.max(bottom, y);
                    right = Math.max(right, x);
                }
            }
        }
        assertTrue(top >= 8 && left >= 8, "margin top/left " + top + "/" + left);
        assertTrue(image.getHeight() - 1 - bottom >= 8 && image.getWidth() - 1 - right >= 8, "margin bottom/right");
    }

    @Test
    void formulaImageIsGrayAndTheSameAtAnyZoom() {
        canvas.setInkColor(Color.RED);
        drawLine(100, 50, 140, 80);
        BufferedImage before = shared.captureFormulaImage();
        canvas.zoomAt(0, 0, 2.5);
        BufferedImage after = shared.captureFormulaImage();
        assertEquals(before.getWidth(), after.getWidth());
        assertEquals(before.getHeight(), after.getHeight());
        for (int y = 0; y < before.getHeight(); y++) {
            for (int x = 0; x < before.getWidth(); x++) {
                int rgb = before.getRGB(x, y);
                assertEquals(rgb, after.getRGB(x, y));
                assertEquals((rgb >> 16) & 0xFF, rgb & 0xFF, "gray, pen colour dropped");
            }
        }
    }

    /** Draws a lasso loop around the rectangle (left, top)-(right, bottom). */
    private void lassoAround(int left, int top, int right, int bottom) {
        canvas.setTool(DrawingCanvas.Tool.LASSO);
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, left, top, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, right, top, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, right, bottom, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, left, bottom, 1, false, MouseEvent.BUTTON1));
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0, left, bottom, 1, false, MouseEvent.BUTTON1));
    }

    @Test
    void withALassoSelectionOnlyThatFormulaIsCaptured() {
        drawLine(0, 0, 30, 0);        // formula A
        drawLine(300, 300, 400, 300); // formula B
        assertEquals(1632, shared.captureFormulaImage().getWidth(), "no selection: both formulas");

        lassoAround(-20, -20, 60, 20);

        BufferedImage image = shared.captureFormulaImage();
        assertEquals(32, image.getHeight(), "only A's height");
        assertEquals(160, image.getWidth(), "only A's width");
        assertEquals(new java.awt.Rectangle(0, 0, 30, 0), shared.getFormulaBounds());
    }

    @Test
    void withoutASelectionTheFormulaIsAllTheInk() {
        drawLine(0, 0, 30, 0);
        drawLine(300, 300, 400, 300);
        assertEquals(new java.awt.Rectangle(0, 0, 400, 300), shared.getFormulaBounds());
    }

    @Test
    void captureWorksOffTheEventThread() throws Exception {
        drawLine(10, 10, 20, 20);
        AtomicReference<BufferedImage> image = new AtomicReference<>();
        Thread worker = new Thread(() -> image.set(shared.captureFormulaImage()));
        worker.start();
        worker.join(5000);
        assertEquals(80, image.get().getWidth());
    }

    @Test
    void emptyCanvasCannotBeCaptured() {
        assertThrows(IllegalStateException.class, shared::captureFormulaImage);
    }

    @Test
    void recognitionSendsTheCapturedImageToTheRecognizer() throws Exception {
        drawLine(0, 0, 30, 0);
        AtomicReference<BufferedImage> received = new AtomicReference<>();
        shared.setFormulaRecognizer(image -> {
            received.set(image);
            return "y=x";
        });

        assertEquals("y=x", shared.recognizeFormula());
        assertEquals(160, received.get().getWidth());
    }

    @Test
    void recognitionWithoutRecognizerSaysItIsNotConnected() {
        drawLine(0, 0, 30, 0);
        assertFalse(shared.isRecognizerConnected());
        IllegalStateException e = assertThrows(IllegalStateException.class, shared::recognizeFormula);
        assertTrue(e.getMessage().contains("not connected"));
    }

    @Test
    void canvasMovesBetweenPagesAndKeepsItsInk() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            CanvasPage graph = new CanvasPage(shared) { };
            CanvasPage pdf = new CanvasPage(shared) { };
            CanvasPage answer = new AnswerCanvasPage(shared);

            shared.attachTo(graph);
            assertSame(graph.getCanvasSlot(), canvas.getParent());
            drawLine(5, 5, 50, 50);

            shared.attachTo(answer);
            assertSame(answer.getCanvasSlot(), canvas.getParent());
            assertEquals(0, graph.getCanvasSlot().getComponentCount(), "only one page holds the canvas");
            assertEquals(1, canvas.getStrokes().size(), "ink survives switching pages");

            shared.attachTo(pdf);
            shared.attachTo(pdf);  // attaching twice is harmless
            assertSame(pdf.getCanvasSlot(), canvas.getParent());
            assertEquals(0, answer.getCanvasSlot().getComponentCount());
        });
    }
}
