package pdf_editor.view;

import org.junit.jupiter.api.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/** PDF selection should follow the same visual tokens as DrawingCanvas. */
class PdfSelectionStyleTest {
    @Test
    void pdfSelectionUsesCanvasColorsAndTransparency() throws Exception {
        assertEquals(new Color(37, 99, 235), color("SELECTION_BLUE"));
        assertEquals(new Color(37, 99, 235, 60), color("SELECTION_HALO"));
    }

    @Test
    void pdfSelectionUsesCanvasDashedOutline() throws Exception {
        Method method = PdfPageCanvas.class.getDeclaredMethod("selectionOutlineStroke");
        method.setAccessible(true);
        BasicStroke stroke = (BasicStroke) method.invoke(null);
        assertEquals(1.2f, stroke.getLineWidth(), 0.001f);
        assertArrayEquals(new float[]{5f, 4f}, stroke.getDashArray(), 0.001f);
    }

    private static Color color(String name) throws Exception {
        Field field = PdfPageCanvas.class.getDeclaredField(name);
        field.setAccessible(true);
        return (Color) field.get(null);
    }
}
