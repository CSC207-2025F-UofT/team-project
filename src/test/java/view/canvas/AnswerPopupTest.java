package view.canvas;

import org.junit.jupiter.api.Test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnswerPopupTest {
    private final AnswerPopup popup = new AnswerPopup(g -> { });

    @Test
    void formulaIsTypesetWithCalculate() {
        AtomicInteger clicks = new AtomicInteger();
        popup.showFormula("\\frac{1}{2}", clicks::incrementAndGet);
        assertTrue(popup.isTypeset());
        assertTrue(popup.hasCalculate());
        assertEquals("\\frac{1}{2}", popup.getLatex());
        popup.clickCalculate();
        assertEquals(1, clicks.get());
    }

    @Test
    void badLatexFallsBackToText() {
        popup.showFormula("\\left(", () -> { });
        assertFalse(popup.isTypeset());
        assertTrue(popup.hasCalculate());
        assertEquals("\\left(", popup.getLatex());
    }

    @Test
    void popupPlacedBelowInkDoesNotOverlapInk() {
        popup.showFormula("5\\times7", () -> { });
        Rectangle ink = new Rectangle(100, 100, 101, 51);
        Rectangle placed = popup.spotBeside(ink, 900, 560);
        assertFalse(placed.intersects(ink), "Popup bounds must not cover the formula ink");
        assertTrue(new Rectangle(0, 0, 900, 560).contains(placed));
    }

    @Test
    void preferredSizeFitsButtonAndCard() {
        popup.showFormula("1+1", () -> { });
        Dimension small = popup.getPreferredSize();
        popup.showFormula("1+1+1+1+1+1+1+1+1+1+1+1", () -> { });
        Dimension big = popup.getPreferredSize();
        assertTrue(small.width > 0 && small.height > 0);
        assertTrue(big.width > small.width);
    }

    @Test
    void graphOnlyHasNoCalculate() {
        AtomicInteger graphs = new AtomicInteger();
        popup.showFormula("y=3x", null, graphs::incrementAndGet);
        assertFalse(popup.hasCalculate());
        assertTrue(popup.hasGraph());
        popup.clickGraph();
        assertEquals(1, graphs.get());
    }

    @Test
    void bothShowsBoth() {
        AtomicInteger clicks = new AtomicInteger();
        popup.showFormula("y=5", clicks::incrementAndGet, () -> clicks.addAndGet(10));
        assertTrue(popup.hasCalculate());
        assertTrue(popup.hasGraph());
        popup.clickCalculate();
        popup.clickGraph();
        assertEquals(11, clicks.get());
    }

    @Test
    void calculateOnlyHasNoGraph() {
        popup.showFormula("1+1=", () -> { }, null);
        assertTrue(popup.hasCalculate());
        assertFalse(popup.hasGraph());
    }
}
