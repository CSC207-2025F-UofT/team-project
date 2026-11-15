package view.glass;

import org.junit.jupiter.api.Test;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

/** Notifications render without a native window; caller owns title/message strings. */
class GlassNoticesTest {
    @Test void errorNoticeCanBeDismissedEarlyAndCloseButtonWorks() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            var notice = new GlassNotices.NoticeView(GlassNotices.Kind.ERROR,
                    "Can't calculate", "a has no value");
            AtomicBoolean closed = new AtomicBoolean();
            notice.onClose(() -> closed.set(true));
            JButton button = findButton(notice);
            assertNotNull(button);
            notice.setSize(480, 110);
            var image = new BufferedImage(480, 110, BufferedImage.TYPE_INT_ARGB);
            var graphics = image.createGraphics(); notice.paint(graphics); graphics.dispose();
            assertNotEquals(0, image.getRGB(100, 28) >>> 24);
            button.doClick();
            assertTrue(closed.get());
        });
    }

    @Test void successNoticeDoesNotRequireDismissAndAutoHidesAfterTwoSeconds() {
        var notice = new GlassNotices.NoticeView(GlassNotices.Kind.SUCCESS, "", "Saved \"123\"");
        assertNull(findButton(notice));
        assertEquals(2_000, GlassNotices.SUCCESS_DURATION_MS);
        assertEquals("glass-notice-success", notice.getName());
    }

    private JButton findButton(Container parent) {
        for (Component c : parent.getComponents()) {
            if (c instanceof JButton b) return b;
            if (c instanceof Container nested) {
                var found = findButton(nested);
                if (found != null) return found;
            }
        }
        return null;
    }
}
