package view.glass;

import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/** Offscreen tests for the compact, fully painted success and error cards. */
class GlassNoticesCompactTest {
    @Test
    void successUsesOneLineInsteadOfTallTextArea() {
        var view = new GlassNotices.NoticeView(GlassNotices.Kind.SUCCESS, "", "Saved “123”");
        assertEquals(64, view.getPreferredSize().height);
        assertTrue(view.getPreferredSize().width <= 480);
        assertEquals(0, view.getComponentCount());
        BufferedImage image = paint(view);
        int middle = image.getRGB(image.getWidth() / 2, 27);
        assertTrue((middle & 0x00FFFFFF) != 0); // fully painted; not empty/transparent canvas
        assertTrue((middle >>> 24) >= 245);
    }

    @Test
    void errorUsesTwoLineLayoutAndWorkingCloseButton() {
        var view = new GlassNotices.NoticeView(GlassNotices.Kind.ERROR,
                "Can't calculate", "a has no value — write a = … first");
        assertTrue(view.getPreferredSize().height <= 135);
        assertTrue(view.getPreferredSize().width <= 580);
        assertEquals(1, view.getComponentCount());
        assertTrue(view.getComponent(0) instanceof JButton);
        AtomicBoolean closed = new AtomicBoolean();
        view.onClose(() -> closed.set(true));
        ((JButton) view.getComponent(0)).doClick();
        assertTrue(closed.get());
        BufferedImage image = paint(view);
        assertTrue((image.getRGB(image.getWidth() / 2, 30) >>> 24) >= 245);
    }

    private static BufferedImage paint(GlassNotices.NoticeView view) {
        var size = view.getPreferredSize();
        view.setSize(size);
        view.doLayout();
        var image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        var graphics = image.createGraphics();
        view.paint(graphics);
        graphics.dispose();
        return image;
    }
}
