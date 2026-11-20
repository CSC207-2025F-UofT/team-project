package use_case.portfolio;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PortfolioInputDataTest {
    @Test
    public void tradeRequestStoresAndNormalizesTradeDetails() {
        PortfolioInputData input = new PortfolioInputData(
                " testuser ",
                " aapl ",
                5,
                PortfolioInputData.Operation.BUY);

        assertEquals("testuser", input.getUsername());
        assertEquals("AAPL", input.getTicker());
        assertEquals(5, input.getQuantity());
        assertEquals(PortfolioInputData.Operation.BUY, input.getOperation());
    }

    @Test
    public void viewRequestDoesNotRequireTradeDetails() {
        PortfolioInputData input = new PortfolioInputData(
                "testuser", PortfolioInputData.Operation.VIEW);

        assertNull(input.getTicker());
        assertEquals(0, input.getQuantity());
    }

    @Test
    public void tradeRequestRejectsInvalidQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioInputData(
                        "testuser",
                        "AAPL",
                        0,
                        PortfolioInputData.Operation.SELL));
    }

    @Test
    public void viewRequestRejectsTradeDetails() {
        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioInputData(
                        "testuser",
                        "AAPL",
                        1,
                        PortfolioInputData.Operation.VIEW));
    }
}
