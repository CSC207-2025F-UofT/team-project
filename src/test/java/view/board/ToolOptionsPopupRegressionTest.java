package view.board;

import org.junit.jupiter.api.Test;
import view.DrawingCanvas;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.lang.reflect.Method;
import java.awt.image.BufferedImage;
import static org.junit.jupiter.api.Assertions.*;

/** Pen, eraser and selection settings use one rounded, transparent glass popup. */
class ToolOptionsPopupRegressionTest {
    @Test void optionsSurfaceDoesNotExposeOpaqueNativeCorners() {
        ToolOptionsPopup popup = new ToolOptionsPopup(new DrawingCanvas(), c -> {});
        assertFalse(popup.isOpaque());
        assertEquals(0, popup.getBackground().getAlpha());
        assertTrue(popup.isLightWeightPopupEnabled());
    }
    @Test void penSettingsHavePreviewFiveThicknessesAndColorPalette() throws Exception {
        ToolOptionsPopup popup = new ToolOptionsPopup(new DrawingCanvas(), c -> {});
        JPanel content = new JPanel();
        content.setLayout(new javax.swing.BoxLayout(content, javax.swing.BoxLayout.Y_AXIS));
        Method method = ToolOptionsPopup.class.getDeclaredMethod("penOptions", JPanel.class);
        method.setAccessible(true);
        method.invoke(popup, content);
        assertTrue(content.getComponentCount() >= 7);
        int rowCount = 0;
        for (var component : content.getComponents()) {
            if (component instanceof JPanel row && row.getComponentCount() == 5) rowCount++;
        }
        assertEquals(1, rowCount, "five pen thickness options must be available");
    }
}
