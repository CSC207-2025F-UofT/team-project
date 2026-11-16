package use_case.portfolio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TradeOutputDataTest {
    @Test
    public void successfulTradeStoresResult() {
        TradeOutputData output = new TradeOutputData(
                true, "Purchase completed", new BigDecimal("9000.00"));

        assertTrue(output.isSuccess());
        assertEquals("Purchase completed", output.getMessage());
        assertEquals(new BigDecimal("9000.00"), output.getAvailableCash());
    }

    @Test
    public void failedTradeStoresResult() {
        TradeOutputData output = new TradeOutputData(
                false, "Insufficient cash", new BigDecimal("100.00"));

        assertFalse(output.isSuccess());
        assertEquals("Insufficient cash", output.getMessage());
    }

    @Test
    public void nullOutputFieldsAreRejected() {
        assertThrows(NullPointerException.class,
                () -> new TradeOutputData(true, null, BigDecimal.ONE));
        assertThrows(NullPointerException.class,
                () -> new TradeOutputData(true, "ok", null));
    }
}
