package view.answer;

import org.junit.jupiter.api.Test;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TexAnswerTest {
    private static final float SIZE = 60f;

    private static CanvasAnswer at(String latex, double x, double y, double pen) {
        Rectangle2D b = AnswerInk.typesetBounds(latex, SIZE, pen);
        return CanvasAnswer.typeset("t", latex, false, x, y, b.getWidth(), b.getHeight(), SIZE).withPenWidth(pen);
    }

    // widest lit run across the middle row of a "1"'s stem = line thickness
    private static double stemWidth(Shape ink) {
        int k = 8;
        Rectangle2D b = ink.getBounds2D();
        int w = (int) Math.ceil(b.getWidth() * k) + 2;
        BufferedImage row = new BufferedImage(w, 1, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = row.createGraphics();
        g.scale(k, k);
        g.translate(-b.getX(), -b.getCenterY() + 0.5 / k);
        g.setColor(Color.WHITE);
        g.fill(ink);
        g.dispose();
        int best = 0;
        int run = 0;
        for (int x = 0; x < w; x++) {
            run = row.getRaster().getSample(x, 0, 0) > 127 ? run + 1 : 0;
            best = Math.max(best, run);
        }
        return (double) best / k;
    }

    @Test
    void linesAreAsThickAsThePen() {
        assertEquals(4, stemWidth(AnswerInk.outline(at("1", 0, 0, 4), 4)), 1);
        assertEquals(10, stemWidth(AnswerInk.outline(at("1", 0, 0, 10), 10)), 1);
    }

    @Test
    void boxMatchesTheInk() {
        CanvasAnswer a = at("= \\frac{3}{(x+1)^{2}}", 100, 80, 6);
        Rectangle2D ink = AnswerInk.outline(a, 6).getBounds2D();
        assertEquals(a.getX(), ink.getX(), 0.5);
        assertEquals(a.getY(), ink.getY(), 0.5);
        assertEquals(a.getWidth(), ink.getWidth(), 0.5);
        assertEquals(a.getHeight(), ink.getHeight(), 0.5);
    }

    @Test
    void fractionStacksUp() {
        double digit = AnswerInk.typesetBounds("3", SIZE, 4).getHeight();
        double frac = AnswerInk.typesetBounds("\\frac{1}{3}", SIZE, 4).getHeight();
        assertTrue(frac > 2 * digit, frac + " vs " + digit);
    }

    @Test
    void zeroCentreLineIsAsTallAsAsked() {
        Rectangle2D b = AnswerInk.typesetBounds("0", AnswerInk.typesetSizeFor(40), 4);
        assertEquals(44, b.getHeight(), 2);
    }

    @Test
    void fractionBarIsTheMiddle() {
        CanvasAnswer a = at("\\frac{1}{3}", 100, 80, 4);
        double middle = AnswerInk.typesetMiddle("\\frac{1}{3}", SIZE, 4);
        assertTrue(middle > a.getHeight() * 0.35 && middle < a.getHeight() * 0.65, middle + " of " + a.getHeight());
    }

    @Test
    void eraserNeedsActualInk() {
        CanvasAnswer a = at("1\\qquad\\qquad 1", 100, 100, 4);
        double mid = a.getY() + a.getHeight() / 2;
        assertTrue(AnswerInk.touches(a, 4, a.getX() - 20, mid, a.getX() + 20, mid, 2), "first 1");
        double gap = a.getX() + a.getWidth() / 2;
        assertFalse(AnswerInk.touches(a, 4, gap, a.getY() - 10, gap, a.getY() + a.getHeight() + 10, 2), "gap");
    }

    @Test
    void resizeKeepsThePen() {
        CanvasAnswer small = at("1", 100, 100, 6);
        CanvasAnswer big = AnswerInk.refit(small.scaled(100, 100, 3), small.getPenWidth());
        assertEquals(6, big.getPenWidth());
        assertEquals(6, stemWidth(AnswerInk.outline(big, big.getPenWidth())), 1);
    }

    @Test
    void kalamDoesntLeakIntoOtherTex() {
        float before = new TeXFormula("x").createTeXIcon(TeXConstants.STYLE_DISPLAY, SIZE).getTrueIconWidth();
        AnswerInk.typesetBounds("x + y", SIZE, 4);
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
    void movesAndRecolorsKeepTheTexAndPen() {
        CanvasAnswer a = at("\\frac{1}{3}", 100, 100, 7);
        CanvasAnswer moved = a.translated(5, 5).withColor(Color.RED).scaled(0, 0, 2);
        assertEquals("\\frac{1}{3}", moved.getLatex());
        assertEquals(7, moved.getPenWidth());
    }
}
