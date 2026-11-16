package view.board;

import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/** Regression: menu entries stay readable and never reserve system shortcut/icon gutters. */
class GlassSaveMenuRenderingTest {
    private static GlassSaveMenu menu(SaveMenuState.Phase phase) {
        return new GlassSaveMenu(new SaveMenuState("123", phase), () -> { }, () -> { });
    }

    @Test void dirtyCanvasOffersTwoDistinctActions() {
        var popup = menu(SaveMenuState.Phase.MODIFIED);
        var rows = (JPanel) popup.getComponent(0);
        assertEquals(2, rows.getComponentCount());
        assertEquals("save-current-canvas", rows.getComponent(0).getName());
        assertEquals("save-as-new-canvas", rows.getComponent(1).getName());
    }

    @Test void savedCanvasShowsOnlySaveAsAndNewCanvasShowsOnlySave() {
        var saved = menu(SaveMenuState.Phase.SAVED);
        var fresh = menu(SaveMenuState.Phase.NEW);
        assertEquals(1, ((JPanel) saved.getComponent(0)).getComponentCount());
        assertEquals("save-as-new-canvas", ((JPanel) saved.getComponent(0)).getComponent(0).getName());
        assertEquals(1, ((JPanel) fresh.getComponent(0)).getComponentCount());
        assertEquals("save-current-canvas", ((JPanel) fresh.getComponent(0)).getComponent(0).getName());
    }

    @Test void menuWidthFitsItsLabelsWithoutEmptyShortcutColumn() {
        for (var phase : SaveMenuState.Phase.values()) {
            var popup = menu(phase);
            var rows = (JPanel) popup.getComponent(0);
            Dimension size = popup.getPreferredSize();
            assertTrue(size.width >= 180, phase.name());
            assertTrue(size.width < 335, "no width left for platform-specific key labels");
            popup.setSize(size);
            popup.doLayout();
            rows.doLayout();
            for (var row : rows.getComponents()) {
                assertTrue(row.getWidth() >= 160);
                assertTrue(row.getHeight() >= 50);
                var action = (GlassSaveMenu.MenuAction) row;
                assertTrue(action.getDescription().length() > 4);
            }
        }
    }

    @Test void currentSaveAndSaveAsInvokeDifferentActions() {
        var currentCalls = new java.util.concurrent.atomic.AtomicInteger();
        var newCalls = new java.util.concurrent.atomic.AtomicInteger();
        var menu = new GlassSaveMenu(new SaveMenuState("123", SaveMenuState.Phase.MODIFIED),
                currentCalls::incrementAndGet, newCalls::incrementAndGet);
        var rows = (JPanel) menu.getComponent(0);
        ((JButton) rows.getComponent(0)).doClick();
        assertEquals(1, currentCalls.get());
        assertEquals(0, newCalls.get());
        ((JButton) rows.getComponent(1)).doClick();
        assertEquals(1, currentCalls.get());
        assertEquals(1, newCalls.get());
    }

    @Test void longUnicodeNamesTruncateOnlyInMenuNotInStorageState() {
        String name = "期末复习材料和微积分课堂笔记第三单元公式的完整版本2026";
        var state = new SaveMenuState(name, SaveMenuState.Phase.MODIFIED);
        assertEquals(name, state.name());
        assertTrue(state.compactName().endsWith("…"));
        var menu = new GlassSaveMenu(state, () -> { }, () -> { });
        assertTrue(menu.getPreferredSize().width < 470);
    }

    @Test void labelsAreActuallyPaintedAndNotCollapsed() {
        var popup = menu(SaveMenuState.Phase.MODIFIED);
        Dimension size = popup.getPreferredSize();
        popup.setSize(size);
        popup.doLayout();
        ((JPanel) popup.getComponent(0)).doLayout();
        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        popup.paint(g);
        g.dispose();
        int dark = 0;
        for (int y = 10; y < size.height - 8; y++) {
            for (int x = 16; x < size.width - 16; x++) {
                int rgb = image.getRGB(x, y);
                if ((rgb >>> 24) >= 180 && ((rgb >> 16) & 255) < 120 && ((rgb >> 8) & 255) < 130) dark++;
            }
        }
        assertTrue(dark > 70, "Both menu entries must draw visible text");
    }
}
