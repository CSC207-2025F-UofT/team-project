package view.answer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RadicandsTest {

    @Test
    void symbolUnderTheRoot() {
        assertEquals(List.of(new Radicands.Root(4, 6)), Radicands.find("1/(2√x)"));
    }

    @Test
    void wholeGroupUnderTheRoot() {
        assertEquals(List.of(new Radicands.Root(0, 6)), Radicands.find("√(x+1)/2"));
    }

    @Test
    void numberStopsBeforeTheLetter() {
        assertEquals(List.of(new Radicands.Root(0, 2)), Radicands.find("√2x"));
    }

    @Test
    void decimalStaysWhole() {
        assertEquals(List.of(new Radicands.Root(1, 5)), Radicands.find("3√2.5"));
    }

    @Test
    void subscriptStaysWithTheLetter() {
        assertEquals(List.of(new Radicands.Root(0, 3)), Radicands.find("√x₁+1"));
    }

    @Test
    void nestedRoots() {
        assertEquals(List.of(new Radicands.Root(0, 7), new Radicands.Root(4, 6)), Radicands.find("√(1+√x)"));
    }

    @Test
    void loneSignHasNothingUnder() {
        assertEquals(List.of(), Radicands.find("√"));
        assertEquals(List.of(), Radicands.find("√ +1"));
    }

    @Test
    void noRoots() {
        assertEquals(List.of(), Radicands.find("3x+1"));
    }
}
