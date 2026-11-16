package entity;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class PortfolioTest {
    @Test
    public void buySellAndValuationUpdatePortfolio() {
        Portfolio portfolio = new Portfolio();

        portfolio.buy("AAPL", 10, new BigDecimal("100"));
        portfolio.buy("AAPL", 10, new BigDecimal("120"));
        portfolio.updateCurrentPrice("AAPL", new BigDecimal("130"));
        portfolio.sell("AAPL", 5, new BigDecimal("130"));

        Holding holding = portfolio.getHoldings().get("AAPL");
        assertEquals(15, holding.getQuantity());
        assertEquals(new BigDecimal("110.0000000000"),
                holding.getAveragePurchasePrice());
        assertEquals(new BigDecimal("8450.00"), portfolio.getAvailableCash());
        assertEquals(new BigDecimal("10400.00"), portfolio.getPortfolioValue());
        assertEquals(new BigDecimal("400.00"), portfolio.getProfitLoss());
        assertEquals(3, portfolio.getTransactions().size());
    }

    @Test
    public void getHoldingsReturnsDefensiveCopies() {
        Portfolio portfolio = new Portfolio();
        portfolio.buy("AAPL", 10, new BigDecimal("100"));

        portfolio.getHoldings().get("AAPL").removeShares(5);

        assertEquals(10, portfolio.getHoldings().get("AAPL").getQuantity());
    }

    @Test
    public void buyAndSellRejectZeroPrice() {
        Portfolio portfolio = new Portfolio();

        assertThrows(IllegalArgumentException.class,
                () -> portfolio.buy("AAPL", 1, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class,
                () -> portfolio.sell("AAPL", 1, BigDecimal.ZERO));
    }
}
