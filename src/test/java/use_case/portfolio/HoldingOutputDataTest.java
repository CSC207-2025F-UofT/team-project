package use_case.portfolio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HoldingOutputDataTest {
    @Test
    public void storesHoldingValues() {
        HoldingOutputData output = new HoldingOutputData(
                "aapl",
                10,
                new BigDecimal("100"),
                new BigDecimal("110"),
                new BigDecimal("1100"));

        assertEquals("AAPL", output.getTicker());
        assertEquals(10, output.getQuantity());
        assertEquals(new BigDecimal("100"), output.getAveragePurchasePrice());
        assertEquals(new BigDecimal("110"), output.getCurrentPrice());
        assertEquals(new BigDecimal("1100"), output.getMarketValue());
    }

    @Test
    public void rejectsInvalidHoldingValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new HoldingOutputData(
                        "AAPL", 0, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE));
        assertThrows(IllegalArgumentException.class,
                () -> new HoldingOutputData(
                        "AAPL", 1, BigDecimal.ONE, BigDecimal.ONE,
                        new BigDecimal("-1")));
    }
}
