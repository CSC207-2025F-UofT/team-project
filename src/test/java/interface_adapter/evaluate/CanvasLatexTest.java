package interface_adapter.evaluate;

import interface_adapter.AnswerKind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CanvasLatexTest {

    @Test
    void fractionsGetStacked() {
        assertEquals("= \\frac{1}{3}", CanvasLatex.ofText(AnswerKind.RIGHT, "= 1/3"));
        assertEquals("-\\frac{5}{3}", CanvasLatex.ofText(AnswerKind.AFTER_EQUALS, "= -5/3"));
    }

    @Test
    void powerOfTenGetsRaised() {
        assertEquals("= 2.432902008\\times 10^{18}", CanvasLatex.ofText(AnswerKind.RIGHT, "= 2.432902008×10^18"));
        assertEquals("= 1.5\\times 10^{-13}", CanvasLatex.ofText(AnswerKind.RIGHT, "= 1.5×10^-13"));
    }

    @Test
    void wordsStayUpright() {
        assertEquals("x = \\frac{1}{3} \\text{ or } x = \\frac{5}{3}",
                CanvasLatex.ofText(AnswerKind.BELOW, "x = 1/3 or x = 5/3"));
        assertEquals("\\text{true}", CanvasLatex.ofText(AnswerKind.RIGHT, "true"));
        assertEquals("= \\text{undefined}", CanvasLatex.ofText(AnswerKind.RIGHT, "= undefined"));
        assertEquals("\\text{true for all }x", CanvasLatex.ofText(AnswerKind.BELOW, "true for all x"));
        assertEquals("\\text{no solution}", CanvasLatex.ofText(AnswerKind.BELOW, "no solution"));
        assertEquals("x = 1 \\text{ or } x = 2\\text{ (and 3 more)}",
                CanvasLatex.ofText(AnswerKind.BELOW, "x = 1 or x = 2 (and 3 more)"));
    }

    @Test
    void symbolsBecomeCommands() {
        assertEquals("x \\ne  2 \\text{ and } x \\ne  -1", CanvasLatex.ofText(AnswerKind.BELOW, "x ≠ 2 and x ≠ -1"));
        assertEquals("\\frac{1}{3} \\le  x < 2", CanvasLatex.ofText(AnswerKind.BELOW, "1/3 ≤ x < 2"));
        assertEquals("x \\ge  0", CanvasLatex.ofText(AnswerKind.BELOW, "x ≥ 0"));
        assertEquals("= -\\infty ", CanvasLatex.ofText(AnswerKind.RIGHT, "= -∞"));
    }

    @Test
    void setsAndSubscripts() {
        assertEquals("= \\{1, \\frac{1}{3}\\}", CanvasLatex.ofText(AnswerKind.RIGHT, "= {1, 1/3}"));
        assertEquals("= (1, 2)", CanvasLatex.ofText(AnswerKind.RIGHT, "= (1, 2)"));
        assertEquals("x_{12} = 3", CanvasLatex.ofText(AnswerKind.BELOW, "x_12 = 3"));
    }

    @Test
    void texOnlyLosesTheEqualsAfterUsersOwn() {
        assertEquals("\\frac{3}{x}", CanvasLatex.ofLatex(AnswerKind.AFTER_EQUALS, "= \\frac{3}{x}"));
        assertEquals("= \\frac{3}{x}", CanvasLatex.ofLatex(AnswerKind.RIGHT, "= \\frac{3}{x}"));
    }
}
