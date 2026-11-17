package view.board;

import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

/** Save/Saved/Save… changes must be visible without changing the workspace use case. */
class SaveSplitControlTest {
    @Test void saveStateHasDistinctNewDirtyAndCleanSemantics() {
        assertFalse(SaveMenuState.newCanvas().hasSavedFile());
        assertFalse(SaveMenuState.newCanvas().canSaveInPlace());
        assertTrue(new SaveMenuState("123", SaveMenuState.Phase.MODIFIED).canSaveInPlace());
        assertFalse(new SaveMenuState("123", SaveMenuState.Phase.SAVED).canSaveInPlace());
    }

    @Test void changingBarLabelRemeasuresTheVisibleTextInsteadOfCollapsing() {
        var bar = new GlassBar(BoardTheme.font(14), () -> new java.awt.Point(),
                g -> { }, () -> 1L);
        var action = bar.action("Save…", Icons::save, () -> { }, () -> true);
        bar.finish();
        int before = bar.capsuleWidth();
        action.setDisplay("Saved", Icons::check);
        bar.finish();
        assertEquals("Saved", action.getLabel());
        assertTrue(before > 65);
        assertTrue(bar.capsuleWidth() > 65);
        assertEquals(44, new CanvasStatusChip().getPreferredSize().height);
    }

    @Test void downwardChevronHasActualDrawnPixels() {
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        graphics.translate(15, 15);
        graphics.setColor(java.awt.Color.BLACK);
        Icons.chevronDown(graphics);
        graphics.dispose();
        int count = 0;
        for (int y = 0; y < 30; y++) for (int x = 0; x < 30; x++) {
            if ((image.getRGB(x, y) >>> 24) > 50) count++;
        }
        assertTrue(count > 8);
    }
}
