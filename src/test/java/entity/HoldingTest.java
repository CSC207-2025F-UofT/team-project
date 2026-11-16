package entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HoldingTest {
    @Test
    public void addSharesCalculatesWeightedAveragePrice() {
        Holding holding = new Holding("AAPL", 10, new BigDecimal("100"));

        holding.addShares(10, new BigDecimal("120"));

        assertEquals(20, holding.getQuantity());
        assertEquals(new BigDecimal("110.0000000000"),
                holding.getAveragePurchasePrice());
    }

    @Test
    public void addSharesRejectsIntegerOverflow() {
        Holding holding = new Holding("AAPL", Integer.MAX_VALUE, BigDecimal.ONE);

        assertThrows(ArithmeticException.class,
                () -> holding.addShares(1, BigDecimal.ONE));
    }

    @Test
    public void currentPriceCanBeZeroButPurchasePriceCannot() {
        Holding holding = new Holding("AAPL", 1, BigDecimal.ONE, BigDecimal.ZERO);

        assertEquals(BigDecimal.ZERO, holding.getMarketValue());
        assertThrows(IllegalArgumentException.class,
                () -> holding.addShares(1, BigDecimal.ZERO));
    }
}
