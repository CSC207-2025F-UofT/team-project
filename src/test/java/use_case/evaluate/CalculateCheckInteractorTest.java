package use_case.evaluate;

import data_access.InMemoryFunctionDataAccessObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculateCheckInteractorTest {
    private final CalculateCheckInputBoundary check =
            new CalculateCheckInteractor(new InMemoryFunctionDataAccessObject());

    @Test
    void sumsAndOneUnknownCount() {
        assertTrue(check.canCalculate("1+1="));
        assertTrue(check.canCalculate("x^{2}=4"));
        assertTrue(check.canCalculate("y=2+3"), "flat line, solves for y");
        assertTrue(check.canCalculate("\\frac{d}{dx}x^{2}="));
    }

    @Test
    void graphOnlyLinesDont() {
        assertFalse(check.canCalculate("y=3x"));
        assertFalse(check.canCalculate("y=x^{2}+1"));
        assertFalse(check.canCalculate("f(x)=x^{2}"), "saves itself, nothing to calculate");
    }

    @Test
    void junkIsJustNo() {
        assertFalse(check.canCalculate(null));
        assertFalse(check.canCalculate(""));
        assertFalse(check.canCalculate("=5"));
        assertFalse(check.canCalculate("\\frac{"));
        assertFalse(check.canCalculate("1=2=3x"));
    }
}
