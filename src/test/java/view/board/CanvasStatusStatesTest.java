package view.board;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

/** Save status is a view-only badge and does not mutate the workspace. */
class CanvasStatusStatesTest {
    @Test void allFourStatesFitTheOriginalCompactToolbarHeight() {
        CanvasStatusChip chip = new CanvasStatusChip();
        for (SaveMenuState.Phase phase : SaveMenuState.Phase.values()) {
            chip.setState(new SaveMenuState("Test", phase));
            assertEquals(44, chip.getPreferredSize().height);
            chip.setSize(chip.getPreferredSize());
            var bitmap = new BufferedImage(chip.getWidth(), chip.getHeight(), BufferedImage.TYPE_INT_ARGB);
            var g = bitmap.createGraphics(); chip.paint(g); g.dispose();
            assertEquals(0, bitmap.getRGB(0,0) >>> 24, "Capsule corners stay transparent on macOS");
            assertTrue((bitmap.getRGB(20,20) >>> 24) > 150);
        }
    }
    @Test void hoveringAndClickingUsesTheProvidedOpenRenameAction() {
        var chip = new CanvasStatusChip();
        AtomicInteger clicks = new AtomicInteger();
        chip.setRenameAction(clicks::incrementAndGet);
        chip.dispatchEvent(new MouseEvent(chip, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 20,20,1,false,MouseEvent.BUTTON1));
        assertEquals(1, clicks.get());
    }
}
