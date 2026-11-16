package view.answer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperandsTest {

    @Test
    void symbolUnderTheRoot() {
        assertEquals(List.of(new Operands.Span(4, 6)), Operands.after("1/(2√x)", '√'));
    }

    @Test
    void wholeGroupUnderTheRoot() {
        assertEquals(List.of(new Operands.Span(0, 6)), Operands.after("√(x+1)/2", '√'));
    }

    @Test
    void numberStopsBeforeTheLetter() {
        assertEquals(List.of(new Operands.Span(0, 2)), Operands.after("√2x", '√'));
    }

    @Test
    void decimalStaysWhole() {
        assertEquals(List.of(new Operands.Span(1, 5)), Operands.after("3√2.5", '√'));
    }

    @Test
    void subscriptStaysWithTheLetter() {
        assertEquals(List.of(new Operands.Span(0, 3)), Operands.after("√x₁+1", '√'));
    }

    @Test
    void nestedRoots() {
        assertEquals(List.of(new Operands.Span(0, 7), new Operands.Span(4, 6)), Operands.after("√(1+√x)", '√'));
    }

    @Test
    void loneMarkHasNothingAfter() {
        assertEquals(List.of(), Operands.after("√", '√'));
        assertEquals(List.of(), Operands.after("√ +1", '√'));
    }

    @Test
    void noMarks() {
        assertEquals(List.of(), Operands.after("3x+1", '√'));
    }

    @Test
    void exponentAfterACaret() {
        assertEquals(List.of(new Operands.Span(1, 3)), Operands.after("x^2", '^'));
        assertEquals(List.of(new Operands.Span(1, 7)), Operands.after("x^(x+1)", '^'));
    }

    @Test
    void negativeNumberStaysWhole() {
        assertEquals(List.of(new Operands.Span(1, 4)), Operands.after("x^−2", '^'));
    }
}
