package view.answer;

import interface_adapter.TextRun;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerFontTest {

    @Test
    void loadsKalam() {
        assertEquals("Kalam", AnswerFont.base().getFamily());
    }

    @Test
    void hasTheMathSigns() {
        for (char c : "0123456789=−≠≤≥∞√π±?".toCharArray()) {
            assertTrue(AnswerFont.base().canDisplay(c), "" + c);
        }
    }

    @Test
    void digitHeightMatchesCharHeight() {
        float size = AnswerFont.sizeFor(40);
        assertEquals(40, AnswerFont.visualBounds("0", size).getHeight(), 2);
    }

    @Test
    void drawPutsTheInkTopLeftWhereAsked() {
        BufferedImage image = draw("40", 20, 30);
        int[] box = darkBox(image);
        assertEquals(20, box[0], 2);
        assertEquals(30, box[1], 2);
    }

    @Test
    void drawsLettersKalamLacks() {
        BufferedImage image = draw("θ = 1", 20, 30);
        assertTrue(darkBox(image)[0] >= 0, "something was drawn");
    }

    @Test
    void radicandClearsTheRootSign() {
        float size = AnswerFont.sizeFor(40);
        double apart = AnswerFont.visualBounds("√", size).getWidth() + AnswerFont.visualBounds("x", size).getWidth();
        assertTrue(AnswerFont.visualBounds(runs("√x"), size).getWidth() >= apart);
    }

    @Test
    void barRunsOverTheWholeGroup() {
        float size = AnswerFont.sizeFor(40);
        Rectangle2D all = AnswerFont.visualBounds(runs("√(x+1)"), size);
        double groupTop = AnswerFont.visualBounds("(x+1)", size).getY();
        assertTrue(all.getY() < groupTop - 1, "sign + bar sit above the group");
        double nearEnd = all.getMaxX() - 0.2 * size;
        assertTrue(inkAbove(runs("√(x+1)"), size, nearEnd, groupTop), "bar reaches the )");
    }

    @Test
    void barStopsAfterOneSymbol() {
        float size = AnswerFont.sizeFor(40);
        List<TextRun> runs = runs("√x+y");
        Rectangle2D all = AnswerFont.visualBounds(runs, size);
        double xTop = AnswerFont.visualBounds("x", size).getY();
        double overX = AnswerFont.visualBounds("√", size).getMaxX() + AnswerFont.visualBounds("x", size).getWidth() / 2;
        assertTrue(inkAbove(runs, size, overX, xTop), "bar over x");
        assertFalse(inkAbove(runs, size, all.getMaxX() - 0.1 * size, xTop), "no bar over y");
    }

    @Test
    void barJoinsTheTipWithoutAHole() {
        float size = AnswerFont.sizeFor(40);
        Shape ink = AnswerFont.outline(runs("√x"), size);
        double tip = AnswerFont.visualBounds("√", size).getMaxX();
        double top = ink.getBounds2D().getY();
        assertTrue(ink.contains(tip - 0.03 * size, top + 0.03 * size));
    }

    @Test
    void exponentUnderTheRootStaysUnderTheBar() {
        float size = AnswerFont.sizeFor(40);
        List<TextRun> runs = List.of(new TextRun("√(x", false), new TextRun("2", true), new TextRun("+1)", false));
        Rectangle2D raised = AnswerFont.visualBounds(List.of(new TextRun("x", false), new TextRun("2", true)), size);
        assertTrue(AnswerFont.visualBounds(runs, size).getY() < raised.getY() - 1);
    }

    // any ink in a thin column at x, from way up down to just above top
    private static boolean inkAbove(List<TextRun> runs, float size, double x, double top) {
        Shape ink = AnswerFont.outline(runs, size);
        double from = ink.getBounds2D().getY() - 1;
        return ink.intersects(new Rectangle2D.Double(x - 0.5, from, 1, top - 1 - from));
    }

    private static List<TextRun> runs(String text) {
        return List.of(new TextRun(text, false));
    }

    private static BufferedImage draw(String text, double left, double top) {
        BufferedImage image = new BufferedImage(300, 150, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 300, 150);
        g.setColor(Color.BLACK);
        AnswerFont.draw(g, text, AnswerFont.sizeFor(40), left, top);
        g.dispose();
        return image;
    }

    // minX, minY of dark pixels, or -1s
    private static int[] darkBox(BufferedImage image) {
        int minX = -1;
        int minY = -1;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) & 0xff) < 128) {
                    minX = minX < 0 ? x : Math.min(minX, x);
                    minY = minY < 0 ? y : minY;
                }
            }
        }
        return new int[]{minX, minY};
    }
}
