package view.board;

import org.junit.jupiter.api.Test;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/** Hover should retain optical glass, including on a 2x Retina-like Graphics2D scale. */
class GlassOpticsAndHoverPerformanceRegressionTest {
    @Test void hoveringKeepsRefractiveLensWithTransparentOuterMarginAtRetinaScale() throws Exception {
        GlassBar bar = new GlassBar(BoardTheme.font(14), Point::new, g -> {
            g.setColor(new java.awt.Color(60, 90, 125));
            g.fillRect(0, 0, 1200, 100);
        }, () -> 1L);
        bar.action("Pen", Icons::pencil, () -> {}, () -> true);
        bar.action("Eraser", Icons::eraser, () -> {}, () -> true);
        bar.finish();
        bar.setHoverToolLabels(true, () -> {});
        int center = GlassBar.MARGIN + GlassBar.PAD + 24;
        int y = GlassBar.MARGIN + GlassBar.CAP_H / 2;
        bar.dispatchEvent(new MouseEvent(bar, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0,
                center, y, 0, false));
        Method step = GlassBar.class.getDeclaredMethod("step");
        step.setAccessible(true);
        for (int i = 0; i < 7; i++) step.invoke(bar);
        Field lens = GlassBar.class.getDeclaredField("lensAlpha");
        lens.setAccessible(true);
        assertTrue(lens.getFloat(bar) > 0.1f, "Hover magnifying droplet must remain enabled");
        BufferedImage canvas = new BufferedImage(bar.getWidth() * 2, bar.getHeight() * 2,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        g.scale(2, 2);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        bar.paint(g);
        g.dispose();
        assertEquals(0, canvas.getRGB(4, 4) >>> 24, "No black outer toolbar drop shadow");
        assertTrue((canvas.getRGB(2 * center, 2 * y) >>> 24) > 100,
                "Refraction and halo must still paint a glass capsule");
    }
}
