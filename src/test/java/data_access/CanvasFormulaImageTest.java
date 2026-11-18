package data_access;

import org.junit.jupiter.api.Test;
import view.DrawingCanvas;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Canvas strokes -> toFormulaImage() -> model input, the whole way like recognition does it. */
class CanvasFormulaImageTest {

    private static void stroke(DrawingCanvas canvas, int... xy) {
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, xy[0], xy[1], 1, false, MouseEvent.BUTTON1));
        for (int i = 2; i < xy.length; i += 2) {
            canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, xy[i], xy[i + 1], 1, false, MouseEvent.BUTTON1));
        }
        canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0,
                xy[xy.length - 2], xy[xy.length - 1], 1, false, MouseEvent.BUTTON1));
    }

    private static DrawingCanvas canvas() {
        DrawingCanvas canvas = new DrawingCanvas();
        canvas.setSize(1200, 800);
        return canvas;
    }

    /** First and last rows with ink darker than mid gray. */
    private static int[] inkRows(BufferedImage image) {
        int top = -1;
        int bottom = -1;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) & 0xFF) < 128) {
                    top = top < 0 ? y : top;
                    bottom = y;
                    break;
                }
            }
        }
        return new int[]{top, bottom};
    }

    // a "1+1" about 40 px tall at 100%
    private static void onePlusOne(DrawingCanvas canvas, int x, int y) {
        stroke(canvas, x, y, x, y + 40);
        stroke(canvas, x + 15, y + 20, x + 45, y + 20);
        stroke(canvas, x + 30, y + 5, x + 30, y + 35);
        stroke(canvas, x + 60, y, x + 60, y + 40);
    }

    @Test
    void handwrittenLineFillsTheModelLineHeight() {
        DrawingCanvas canvas = canvas();
        onePlusOne(canvas, 100, 100);
        BufferedImage out = FormulaImagePreprocessor.prepare(canvas.toFormulaImage());
        assertEquals(128, out.getHeight());
        int[] rows = inkRows(out);
        assertTrue(rows[1] - rows[0] + 1 >= 105, "line is " + (rows[1] - rows[0] + 1) + " px");
        assertTrue(rows[0] >= 7 && out.getHeight() - 1 - rows[1] >= 7, "margins kept");
    }

    @Test
    void zoomDoesNotChangeWhatTheModelSees() {
        DrawingCanvas plain = canvas();
        onePlusOne(plain, 100, 100);
        DrawingCanvas zoomed = canvas();
        onePlusOne(zoomed, 100, 100);
        zoomed.zoomAt(0, 0, 3);
        BufferedImage a = FormulaImagePreprocessor.prepare(plain.toFormulaImage());
        BufferedImage b = FormulaImagePreprocessor.prepare(zoomed.toFormulaImage());
        assertEquals(a.getWidth(), b.getWidth());
        for (int y = 0; y < a.getHeight(); y++) {
            for (int x = 0; x < a.getWidth(); x++) {
                assertEquals(a.getRGB(x, y), b.getRGB(x, y));
            }
        }
    }

    @Test
    void twoLinesKeepTheOneLineSize() {
        DrawingCanvas one = canvas();
        onePlusOne(one, 100, 100);
        DrawingCanvas two = canvas();
        onePlusOne(two, 100, 100);
        onePlusOne(two, 100, 200);
        BufferedImage single = FormulaImagePreprocessor.prepare(one.toFormulaImage());
        BufferedImage both = FormulaImagePreprocessor.prepare(two.toFormulaImage());
        assertEquals(single.getWidth(), both.getWidth(), "same scale, so same width");
        assertTrue(both.getHeight() > single.getHeight() + 112, "canvas grew instead: " + both.getHeight());
    }

    @Test
    void loneMinusFromTheCanvasStaysThin() {
        DrawingCanvas canvas = canvas();
        stroke(canvas, 100, 100, 140, 102);
        BufferedImage out = FormulaImagePreprocessor.prepare(canvas.toFormulaImage());
        assertEquals(128, out.getHeight());
        int[] rows = inkRows(out);
        assertTrue(rows[1] - rows[0] + 1 <= 30, "minus is " + (rows[1] - rows[0] + 1) + " px thick");
    }
}
