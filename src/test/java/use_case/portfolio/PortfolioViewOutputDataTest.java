package use_case.portfolio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PortfolioViewOutputDataTest {
    @Test
    public void storesPortfolioValuesAndHoldings() {
        HoldingOutputData holding = new HoldingOutputData(
                "AAPL", 10, new BigDecimal("100"),
                new BigDecimal("110"), new BigDecimal("1100"));

        PortfolioViewOutputData output = new PortfolioViewOutputData(
                new BigDecimal("8900"),
                new BigDecimal("10000"),
                BigDecimal.ZERO,
                List.of(holding));

        assertEquals(new BigDecimal("8900"), output.getAvailableCash());
        assertEquals(new BigDecimal("10000"), output.getPortfolioValue());
        assertEquals(BigDecimal.ZERO, output.getProfitLoss());
        assertEquals(List.of(holding), output.getHoldings());
    }

    @Test
    public void copiesAndProtectsHoldingList() {
        List<HoldingOutputData> source = new ArrayList<>();
        source.add(new HoldingOutputData(
                "AAPL", 1, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE));
        PortfolioViewOutputData output = new PortfolioViewOutputData(
                BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ONE, source);

        source.clear();

        assertEquals(1, output.getHoldings().size());
        assertThrows(UnsupportedOperationException.class,
                () -> output.getHoldings().clear());
    }

    @Test
    public void rejectsInvalidPortfolioValues() {
        assertThrows(IllegalArgumentException.class,
                () -> new PortfolioViewOutputData(
                        new BigDecimal("-1"), BigDecimal.ONE, BigDecimal.ZERO, List.of()));
        assertThrows(NullPointerException.class,
                () -> new PortfolioViewOutputData(
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        List.of((HoldingOutputData) null)));
    }
}
