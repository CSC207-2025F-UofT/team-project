package view.board;

import org.junit.jupiter.api.Test;
import javax.swing.JMenu;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class GlassContextMenuTest {
    @Test void popupHasTransparentCornersAndRoundedSurface() {
        GlassContextMenu menu = new GlassContextMenu();
        menu.addAction("Delete selected item", () -> {});
        assertFalse(menu.isOpaque());
        assertEquals(0, menu.getBackground().getAlpha());
        menu.setSize(menu.getPreferredSize());
        menu.doLayout();
        BufferedImage canvas = new BufferedImage(menu.getWidth(), menu.getHeight(), BufferedImage.TYPE_INT_ARGB);
        var graphics = canvas.createGraphics();
        menu.paint(graphics);
        graphics.dispose();
        assertEquals(0, (canvas.getRGB(0, 0) >>> 24) & 255, "rounded corner must be transparent");
    }
    @Test void actionsAndNestedPageSubmenuStillWork() {
        GlassContextMenu menu = new GlassContextMenu();
        AtomicInteger count = new AtomicInteger();
        var button = menu.addAction("Delete selected item", count::incrementAndGet);
        button.doClick();
        assertEquals(1, count.get());
        JMenu pages = new JMenu("Move to page");
        pages.add("Page 1");
        menu.addSubmenu(pages);
        assertEquals(1, pages.getItemCount());
        assertFalse(pages.getPopupMenu().isOpaque());
    }
}
