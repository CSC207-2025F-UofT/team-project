package entity.phase;

import entity.LatexParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class DifferentialEquationParserTest {
    private final DifferentialEquationParser parser = new DifferentialEquationParser();

    @ParameterizedTest
    @ValueSource(strings = {"y'=x-y", "y^{\\prime}=x-y", "y^{'}=x-y", "\\frac{dy}{dx}=x-y", "dy/dx=x-y",
        "x-y=y'", "y'(x)=x-y", "y′ = x − y"})
    void everyWayOfWritingAFirstOrderOneIsASlopeField(String latex) throws LatexParseException {
        DifferentialEquation e = parser.parse(latex);
        assertEquals(DifferentialEquation.Kind.SLOPE_FIELD, e.getKind());
        assertEquals("x", e.getHorizontalName());
        assertEquals("y", e.getVerticalName());
        assertEquals(1, e.dh(3, 1), 1e-12);
        assertEquals(2, e.dv(3, 1), 1e-12);
    }

    @Test
    void dotMeansTime() throws LatexParseException {
        DifferentialEquation e = parser.parse("\\dot{x}=x(1-x)");
        assertEquals("t", e.getHorizontalName());
        assertEquals("x", e.getVerticalName());
        assertEquals(-2, e.dv(0, 2), 1e-12);
    }

    @Test
    void theOtherLetterIsTheVariable() throws LatexParseException {
        assertEquals("t", parser.parse("y'=t y").getHorizontalName());
    }

    @Test
    void secondOrderGoesOnTheYYPrimePlane() throws LatexParseException {
        DifferentialEquation e = parser.parse("y''=-y-0.5y'");
        assertEquals(DifferentialEquation.Kind.PHASE_PLANE, e.getKind());
        assertEquals("y", e.getHorizontalName());
        assertEquals("y'", e.getVerticalName());
        assertEquals(3, e.dh(2, 3), 1e-12);            // y' is the speed
        assertEquals(-2 - 1.5, e.dv(2, 3), 1e-12);
    }

    @Test
    void greekAndDotsWork() throws LatexParseException {
        DifferentialEquation pendulum = parser.parse("\\theta''=-\\sin\\theta");
        assertEquals("\\theta", pendulum.getHorizontalName());
        assertEquals(-Math.sin(1), pendulum.dv(1, 0), 1e-12);
        DifferentialEquation damped = parser.parse("\\ddot{x}=-x-\\dot{x}");
        assertEquals(-1 - 2, damped.dv(1, 2), 1e-12);
        assertEquals("θ′", DifferentialEquationParser.display("\\theta'"));
    }

    @Test
    void secondOrderWithTheSecondDerivativeAsAFraction() throws LatexParseException {
        assertEquals(-8, parser.parse("\\frac{d^{2}y}{dx^{2}}=-4y").dv(2, 0), 1e-12);
    }

    @ParameterizedTest
    @ValueSource(strings = {"\\begin{matrix}x'=y\\\\y'=-x\\end{matrix}", "x'=y, y'=-x", "x'=y; y'=-x",
        "\\left\\{\\begin{array}{l}x'=y\\\\y'=-x\\end{array}\\right.", "\\begin{cases}y'=-x\\\\x'=y\\end{cases}",
        "\\frac{dx}{dt}=y \\\\ \\frac{dy}{dt}=-x"})
    void twoLinesAreASystem(String latex) throws LatexParseException {
        DifferentialEquation e = parser.parse(latex);
        assertEquals(DifferentialEquation.Kind.PHASE_PLANE, e.getKind());
        assertEquals("x", e.getHorizontalName());   // y first still puts x across
        assertEquals("y", e.getVerticalName());
        assertEquals(5, e.dh(2, 5), 1e-12);
        assertEquals(-2, e.dv(2, 5), 1e-12);
    }

    @Test
    void sayingWhyItCantBeDrawn() {
        assertMessage("y''=x", "autonomous");
        assertMessage("y'''=y", "first- and second-order");
        assertMessage("x'=y\\\\y'=t", "autonomous");
        assertMessage("x'=y\\\\x'=-x", "different variables");
        assertMessage("y'=x+q+r", "Unknown variable");
        assertMessage("x'=y\\\\y'=x\\\\z'=1", "system of two");
    }

    @ParameterizedTest
    @ValueSource(strings = {"y=x^2", "f'(2)=", "1+1=", "y'", "x^2+y^2=1", "\\frac{d}{dx}(x^2)="})
    void notAnOde(String latex) {
        assertFalse(parser.looksLikeDifferentialEquation(latex));
    }

    @Test
    void looksLikeOneEvenIfItCantBeDrawnSoTheButtonCanSayWhy() {
        assertTrue(parser.looksLikeDifferentialEquation("y'''=y"));
        assertTrue(parser.looksLikeDifferentialEquation("y'=x-y"));
    }

    private void assertMessage(String latex, String part) {
        LatexParseException e = assertThrows(LatexParseException.class, () -> parser.parse(latex));
        assertTrue(e.getMessage().contains(part), e.getMessage());
    }
}
