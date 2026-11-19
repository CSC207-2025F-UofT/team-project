package interface_adapter.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphNumbersTest {
    @Test
    void stepsAreOneTwoOrFive() {
        assertEquals(1, GraphNumbers.niceStep(0.9), 1e-12);
        assertEquals(2, GraphNumbers.niceStep(1.7), 1e-12);
        assertEquals(5, GraphNumbers.niceStep(3), 1e-12);
        assertEquals(0.05, GraphNumbers.niceStep(0.04), 1e-12);
        assertEquals(100, GraphNumbers.niceStep(60), 1e-12);
    }

    @Test
    void ticksLandOnRoundNumbersInsideTheRange() {
        List<GraphTick> ticks = GraphNumbers.ticks(-10, 10, 8);
        assertEquals("-10", ticks.get(0).getLabel());
        assertEquals("10", ticks.get(ticks.size() - 1).getLabel());
        assertTrue(ticks.stream().anyMatch(t -> t.getLabel().equals("0")));
        List<GraphTick> fine = GraphNumbers.ticks(0, 1, 5);
        assertEquals("0.6", fine.get(3).getLabel(), "no 0.6000000000000001");
    }

    @Test
    void readoutsAreShort() {
        assertEquals("1.5", GraphNumbers.format(1.5));
        assertEquals("3.142", GraphNumbers.format(Math.PI));
        assertEquals("0", GraphNumbers.format(1e-15));
        assertEquals("-2", GraphNumbers.format(-2.0000000001));
        assertEquals("1.5e6", GraphNumbers.format(1.5e6));
        assertEquals("2e-5", GraphNumbers.format(2e-5));
    }
}
