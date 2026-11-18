package view.answer;

import org.junit.jupiter.api.Test;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TexAnswerTest {
    private static final float SIZE = 60f;

    private static CanvasAnswer at(String latex, double x, double y) {
        Rectangle2D b = TexAnswer.bounds(latex, SIZE);
        return CanvasAnswer.typeset("t", latex, false, x, y, b.getWidth(), b.getHeight(), SIZE);
    }

    // box of the lit px after painting a white on black
    private static Rectangle2D inkOf(CanvasAnswer a) {
        BufferedImage img = new BufferedImage(900, 500, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        TexAnswer.paint(g, a, Color.WHITE);
        g.dispose();
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                if ((img.getRGB(x, y) & 0xff) > 64) {
                    minX = Math.min(minX, x);
                    minY = Math.min(minY, y);
                    maxX = Math.max(maxX, x);
                    maxY = Math.max(maxY, y);
                }
            }
        }
        return new Rectangle2D.Double(minX, minY, maxX - minX + 1, maxY - minY + 1);
    }

    @Test
    void fractionStacksUp() {
        double digit = TexAnswer.bounds("3", SIZE).getHeight();
        double frac = TexAnswer.bounds("\\frac{1}{3}", SIZE).getHeight();
        assertTrue(frac > 2 * digit, frac + " vs " + digit);
    }

    @Test
    void inkStaysInTheBox() {
        CanvasAnswer a = at("= \\frac{3}{(x+1)^{2}}", 100, 80);
        Rectangle2D ink = inkOf(a);
        Rectangle2D box = a.getBox();
        assertTrue(ink.getMinX() >= box.getMinX() - 2 && ink.getMinY() >= box.getMinY() - 2, ink + " in " + box);
        assertTrue(ink.getMaxX() <= box.getMaxX() + 2 && ink.getMaxY() <= box.getMaxY() + 2, ink + " in " + box);
        assertEquals(box.getMinY(), ink.getMinY(), 3);  // no padding on top
    }

    @Test
    void zeroIsAsTallAsAsked() {
        float size = TexAnswer.sizeFor(40);
        Rectangle2D b = TexAnswer.bounds("0", size);
        assertEquals(40, inkOf(CanvasAnswer.typeset("0", "0", false, 50, 50, b.getWidth(), b.getHeight(), size))
                .getHeight(), 2);
    }

    @Test
    void fractionBarIsTheMiddle() {
        CanvasAnswer a = at("\\frac{1}{3}", 100, 80);
        double middle = TexAnswer.middle("\\frac{1}{3}", SIZE);
        assertTrue(middle > a.getHeight() * 0.3 && middle < a.getHeight() * 0.7, middle + " of " + a.getHeight());
    }

    @Test
    void eraserNeedsActualInk() {
        CanvasAnswer a = at("1\\qquad\\qquad 1", 100, 100);
        double mid = a.getY() + a.getHeight() / 2;
        assertTrue(TexAnswer.touches(a, a.getX() - 20, mid, a.getX() + 20, mid, 2), "first 1");
        double gap = a.getX() + a.getWidth() / 2;
        assertFalse(TexAnswer.touches(a, gap, a.getY() - 10, gap, a.getY() + a.getHeight() + 10, 2), "gap");
        assertFalse(TexAnswer.touches(a, 0, 0, 50, 50, 2), "far off");
    }

    @Test
    void eraserFollowsAResize() {
        CanvasAnswer small = at("1", 100, 100);
        CanvasAnswer big = AnswerInk.refit(small.scaled(100, 100, 3), 4);
        assertEquals(small.getWidth() * 3, big.getWidth(), 1);
        double y = big.getY() + big.getHeight() * 0.8;  // low on the big 1, under the small one
        assertTrue(TexAnswer.touches(big, big.getX(), y, big.getX() + big.getWidth(), y, 1));
        assertFalse(TexAnswer.touches(small, big.getX(), y, big.getX() + big.getWidth(), y, 1));
    }

    @Test
    void kalamDoesntLeakIntoOtherTex() {
        float before = new TeXFormula("x").createTeXIcon(TeXConstants.STYLE_DISPLAY, SIZE).getTrueIconWidth();
        TexAnswer.bounds("x + y", SIZE);
        float after = new TeXFormula("x").createTeXIcon(TeXConstants.STYLE_DISPLAY, SIZE).getTrueIconWidth();
        assertEquals(before, after, 0.01, "pdf / history tex keeps its own font");
    }

    @Test
    void everythingTheFormatterMakesParses() {
        for (String tex : new String[]{"\\text{true for all }x", "x \\ne  2 \\text{ and } x \\ne  -1",
                "= -\\infty ", "= \\{1, \\frac{1}{3}\\}", "\\frac{1}{3} \\le  x < 2", "x \\ge  0",
                "= 1.5\\times 10^{-13}", "x_{12} = 3", "x = 1 \\text{ or } x = 2\\text{ (and 3 more)}"}) {
            assertDoesNotThrow(() -> TexAnswer.bounds(tex, SIZE), tex);
        }
    }

    @Test
    void movesAndRecolorsKeepTheTex() {
        CanvasAnswer a = at("\\frac{1}{3}", 100, 100);
        assertEquals("\\frac{1}{3}", a.translated(5, 5).withColor(Color.RED).scaled(0, 0, 2).getLatex());
        assertTrue(a.sized(1, 1).isTypeset());
    }
}
