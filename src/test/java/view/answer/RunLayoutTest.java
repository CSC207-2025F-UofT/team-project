package view.answer;

import interface_adapter.TextRun;
import org.junit.jupiter.api.Test;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RunLayoutTest {

    @Test
    void radicandClearsTheRootSign() {
        float size = AnswerFont.sizeFor(40);
        double apart = AnswerFont.visualBounds("√", size).getWidth() + AnswerFont.visualBounds("x", size).getWidth();
        assertTrue(RunLayout.visualBounds(runs("√x"), size).getWidth() >= apart);
    }

    @Test
    void barRunsOverTheWholeGroup() {
        float size = AnswerFont.sizeFor(40);
        Rectangle2D all = RunLayout.visualBounds(runs("√(x+1)"), size);
        double groupTop = AnswerFont.visualBounds("(x+1)", size).getY();
        assertTrue(all.getY() < groupTop - 1, "sign + bar sit above the group");
        double nearEnd = all.getMaxX() - 0.2 * size;
        assertTrue(inkAbove(runs("√(x+1)"), size, nearEnd, groupTop), "bar reaches the )");
    }

    @Test
    void barStopsAfterOneSymbol() {
        float size = AnswerFont.sizeFor(40);
        List<TextRun> runs = runs("√x+y");
        Rectangle2D all = RunLayout.visualBounds(runs, size);
        double xTop = AnswerFont.visualBounds("x", size).getY();
        double overX = AnswerFont.visualBounds("√", size).getMaxX()
                + AnswerFont.visualBounds("x", size).getWidth() / 2;
        assertTrue(inkAbove(runs, size, overX, xTop), "bar over x");
        assertFalse(inkAbove(runs, size, all.getMaxX() - 0.1 * size, xTop), "no bar over y");
    }

    @Test
    void barJoinsTheTipWithoutAHole() {
        float size = AnswerFont.sizeFor(40);
        Shape ink = RunLayout.outline(runs("√x"), size);
        double tip = AnswerFont.visualBounds("√", size).getMaxX();
        double top = ink.getBounds2D().getY();
        assertTrue(ink.contains(tip - 0.03 * size, top + 0.03 * size));
    }

    @Test
    void exponentUnderTheRootStaysUnderTheBar() {
        float size = AnswerFont.sizeFor(40);
        List<TextRun> runs = List.of(new TextRun("√(x", false), new TextRun("2", true), new TextRun("+1)", false));
        Rectangle2D raised = RunLayout.visualBounds(List.of(new TextRun("x", false), new TextRun("2", true)), size);
        assertTrue(RunLayout.visualBounds(runs, size).getY() < raised.getY() - 1);
    }

    @Test
    void caretInAnExponentRaisesAgainInsteadOfShowing() {
        // e^(x^2): runs only go up one level, the writer leaves a ^ for the second
        float size = AnswerFont.sizeFor(40);
        List<TextRun> caret = List.of(new TextRun("e", false), new TextRun("x^2", true));
        List<TextRun> flat = List.of(new TextRun("e", false), new TextRun("x2", true));
        assertEquals(3, RunLayout.outlinesBySize(caret, size).size(), "body, exponent, exponent's exponent");
        assertTrue(RunLayout.visualBounds(caret, size).getWidth() < RunLayout.visualBounds(flat, size).getWidth(),
                "no ^ drawn, 2 smaller");
    }

    @Test
    void caretInsideTheSecondLevelStaysAsIs() {
        float size = AnswerFont.sizeFor(40);
        List<TextRun> deep = List.of(new TextRun("e", false), new TextRun("x^(x^2)", true));
        assertEquals(3, RunLayout.outlinesBySize(deep, size).size(), "no third level, too small to read");
    }

    // any ink in a thin column at x, from way up down to just above top
    private static boolean inkAbove(List<TextRun> runs, float size, double x, double top) {
        Shape ink = RunLayout.outline(runs, size);
        double from = ink.getBounds2D().getY() - 1;
        return ink.intersects(new Rectangle2D.Double(x - 0.5, from, 1, top - 1 - from));
    }

    private static List<TextRun> runs(String text) {
        return List.of(new TextRun(text, false));
    }
}
