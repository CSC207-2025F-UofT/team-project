package use_case.evaluate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefinitionCheckInteractorTest {
    private final DefinitionCheckInputBoundary check = new DefinitionCheckInteractor();

    @Test
    void definitionWithABodyCounts() {
        assertTrue(check.isDefinition("f(x)=x^{2}+2x"));
        assertTrue(check.isDefinition("g(x,y)=x+y"));
        assertTrue(check.isDefinition("f(x)=x^{2}+2x="), "trailing = still a definition");
    }

    @Test
    void noBodyYetDoesntCount() {
        assertFalse(check.isDefinition("f(x)="));
        assertFalse(check.isDefinition("f(x)"));
    }

    @Test
    void callsAndSumsArentDefinitions() {
        assertFalse(check.isDefinition("f(2)="));
        assertFalse(check.isDefinition("1+1="));
        assertFalse(check.isDefinition("y=3x"));
        assertFalse(check.isDefinition("x^{2}=4"));
    }

    @Test
    void junkIsJustNo() {
        assertFalse(check.isDefinition(null));
        assertFalse(check.isDefinition(""));
        assertFalse(check.isDefinition("\\frac{"));
    }
}
