package interface_adapter.evaluate;

import entity.SyntaxProblem;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InvalidTextTest {

    @Test
    void everyProblemHasWords() {
        List<String> d = List.of("x", "1", "y");
        for (SyntaxProblem p : SyntaxProblem.values()) {
            String text = InvalidText.of(p.name(), d);
            assertFalse(text.endsWith("Can't read it"), p.name());
            assertFalse(text.contains("\\"), p.name());
        }
    }

    @Test
    void noLatexLeaksOut() {
        assertEquals("Unexpected \"·\"", InvalidText.of("UNEXPECTED", List.of("\\cdot")));
        assertEquals("\"foo\" not supported", InvalidText.of("NOT_SUPPORTED", List.of("\\foo")));
        assertEquals("Missing \"}\"", InvalidText.of("EXPECTED", List.of("}")));
        assertEquals("Stray \".\"", InvalidText.of("BAD_NUMBER", List.of(".")));
        assertEquals("f needs 1 input", InvalidText.of("WRONG_INPUTS", List.of("f", "1")));
    }

    @Test
    void readableNames() {
        assertEquals("x₁₂", ReadableLatex.of("x_{12}"));
        assertEquals("x₁", ReadableLatex.of("x_1"));
        assertEquals("x̄", ReadableLatex.of("\\bar{x}"));
        assertEquals("⌋", ReadableLatex.of("\\rfloor"));
        assertEquals("{", ReadableLatex.of("{"));
    }
}
