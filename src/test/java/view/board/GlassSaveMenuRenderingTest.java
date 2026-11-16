package view.board;

import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertTrue;

/** Regression for the collapsed, unreadable Save menu seen in the one-board UI. */
class GlassSaveMenuRenderingTest {
    @Test
    void bothSaveChoicesHaveReadableWidthAfterLayout() {
        GlassSaveMenu menu = new GlassSaveMenu(() -> { }, () -> { });
        JPanel rows = (JPanel) menu.getComponent(0);
        JButton save = (JButton) rows.getComponent(0);
        JButton saveAs = (JButton) rows.getComponent(1);

        // JButton inherits ImageObserver.WIDTH = 1. Unqualified WIDTH in the old MenuAction
        // resolved to that constant, yielding a preferred button width of -21 pixels.
        assertTrue(save.getPreferredSize().width >= 160);
        assertTrue(saveAs.getPreferredSize().width >= 160);
        Dimension size = menu.getPreferredSize();
        assertTrue(size.width >= 180, "The popup must not collapse into a 20px-wide strip");
        assertTrue(size.height >= 70);

        menu.setSize(size);
        menu.doLayout();
        rows.doLayout();
        assertTrue(save.getWidth() >= 160);
        assertTrue(saveAs.getWidth() >= 160);
        assertTrue(saveAs.getY() > save.getY());
    }

    @Test
    void twoLabelsAreActuallyPaintedAndNotInvisible() {
        GlassSaveMenu menu = new GlassSaveMenu(() -> { }, () -> { });
        Dimension size = menu.getPreferredSize();
        menu.setSize(size);
        menu.doLayout();
        ((JPanel) menu.getComponent(0)).doLayout();

        BufferedImage image = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        menu.paint(g);
        g.dispose();

        int darkTextPixels = 0;
        for (int y = 10; y < size.height - 10; y++) {
            for (int x = 20; x < Math.min(142, size.width - 10); x++) {
                int color = image.getRGB(x, y);
                int alpha = (color >>> 24) & 255;
                int red = (color >>> 16) & 255;
                int green = (color >>> 8) & 255;
                int blue = color & 255;
                if (alpha >= 150 && red < 100 && green < 100 && blue < 110) darkTextPixels++;
            }
        }
        assertTrue(darkTextPixels > 25, "Both menu labels must render as visible dark text");
    }
}
