package view.board;

import org.junit.jupiter.api.Test;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Regression coverage for contiguous Save / chevron hit targets and the shared hover lens. */
class GlassSplitHoverTest {
    @Test
    void twoRoundedHoverTargetsShareAnUnbrokenHitBoundary() {
        var saves = new AtomicInteger();
        var menus = new AtomicInteger();
        var bar = new GlassBar(BoardTheme.font(14), Point::new, g -> { }, () -> 1L);
        var save = bar.action("Save", Icons::save, saves::incrementAndGet, () -> true);
        var arrow = bar.action(null, Icons::chevronDown, menus::incrementAndGet, () -> true);
        save.setSplitGroup(7);
        arrow.setSplitGroup(7);
        bar.finish();
        assertEquals(save.x + save.w, arrow.x, 0.001f, "No separator or dead space");

        int y = GlassBar.MARGIN + GlassBar.PAD + GlassBar.SEG_H / 2;
        click(bar, Math.round(GlassBar.MARGIN + GlassBar.PAD + save.x + save.w - 2), y);
        click(bar, Math.round(GlassBar.MARGIN + GlassBar.PAD + arrow.x + 2), y);
        assertEquals(1, saves.get());
        assertEquals(1, menus.get());

        // Both halves must paint with the lens active, without warping glyphs or throwing.
        hoverAndPaint(bar, Math.round(GlassBar.MARGIN + GlassBar.PAD + save.center()), y);
        hoverAndPaint(bar, Math.round(GlassBar.MARGIN + GlassBar.PAD + arrow.center()), y);
    }

    private static void click(GlassBar bar, int x, int y) {
        long now = System.currentTimeMillis();
        bar.dispatchEvent(new MouseEvent(bar, MouseEvent.MOUSE_PRESSED, now, 0, x, y, 1, false, MouseEvent.BUTTON1));
        bar.dispatchEvent(new MouseEvent(bar, MouseEvent.MOUSE_RELEASED, now, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }

    private static void hoverAndPaint(GlassBar bar, int x, int y) {
        bar.dispatchEvent(new MouseEvent(bar, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0,
                x, y, 0, false));
        var image = new BufferedImage(bar.getWidth(), bar.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        try {
            bar.paint(graphics);
        } finally {
            graphics.dispose();
        }
        assertTrue(image.getWidth() > 0);
    }
}
