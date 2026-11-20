package use_case.portfolio;

import entity.Portfolio;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PortfolioInteractorTest {
    private static final class RecordingOutputBoundary implements PortfolioOutputBoundary {
        private TradeOutputData tradeOutput;
        private PortfolioViewOutputData portfolioOutput;
        private TransactionHistoryOutputData historyOutput;

        @Override
        public void prepareBuyView(TradeOutputData outputData) {
            tradeOutput = outputData;
        }

        @Override
        public void prepareSellView(TradeOutputData outputData) {
            tradeOutput = outputData;
        }

        @Override
        public void preparePortfolioView(PortfolioViewOutputData outputData) {
            portfolioOutput = outputData;
        }

        @Override
        public void prepareTransactionHistoryView(
                TransactionHistoryOutputData outputData) {
            historyOutput = outputData;
        }
    }

    @Test
    public void buysAndSellsAtCurrentPrice() throws IOException {
        Portfolio portfolio = new Portfolio();
        RecordingOutputBoundary presenter = new RecordingOutputBoundary();
        PortfolioInteractor interactor = new PortfolioInteractor(
                portfolio,
                ticker -> new BigDecimal("100"),
                presenter);

        interactor.execute(new PortfolioInputData(
                "user", "AAPL", 10, PortfolioInputData.Operation.BUY));

        assertTrue(presenter.tradeOutput.isSuccess());
        assertEquals(new BigDecimal("9000.00"), presenter.tradeOutput.getAvailableCash());

        interactor.execute(new PortfolioInputData(
                "user", "AAPL", 5, PortfolioInputData.Operation.SELL));

        assertTrue(presenter.tradeOutput.isSuccess());
        assertEquals(new BigDecimal("9500.00"), presenter.tradeOutput.getAvailableCash());
        assertEquals(2, portfolio.getTransactions().size());
    }

    @Test
    public void presentsInsufficientCashAsFailedTrade() {
        Portfolio portfolio = new Portfolio();
        RecordingOutputBoundary presenter = new RecordingOutputBoundary();
        PortfolioInteractor interactor = new PortfolioInteractor(
                portfolio,
                ticker -> new BigDecimal("200"),
                presenter);

        interactor.execute(new PortfolioInputData(
                "user", "AAPL", 100, PortfolioInputData.Operation.BUY));

        assertFalse(presenter.tradeOutput.isSuccess());
        assertEquals(new BigDecimal("10000.00"),
                presenter.tradeOutput.getAvailableCash());
        assertEquals("Insufficient available cash", presenter.tradeOutput.getMessage());
    }

    @Test
    public void presentsCurrentPortfolioAndTransactionHistory() {
        Portfolio portfolio = new Portfolio();
        portfolio.buy("AAPL", 10, new BigDecimal("100"));
        RecordingOutputBoundary presenter = new RecordingOutputBoundary();
        PortfolioInteractor interactor = new PortfolioInteractor(
                portfolio,
                ticker -> new BigDecimal("120"),
                presenter);

        interactor.execute(new PortfolioInputData(
                "user", PortfolioInputData.Operation.VIEW));
        interactor.execute(new PortfolioInputData(
                "user", PortfolioInputData.Operation.TRANSACTION_HISTORY));

        assertEquals(new BigDecimal("10200.00"), presenter.portfolioOutput.getPortfolioValue());
        assertEquals(new BigDecimal("200.00"), presenter.portfolioOutput.getProfitLoss());
        assertEquals(1, presenter.portfolioOutput.getHoldings().size());
        assertEquals(new BigDecimal("120"),
                presenter.portfolioOutput.getHoldings().get(0).getCurrentPrice());
        assertNotNull(presenter.historyOutput);
        assertEquals(1, presenter.historyOutput.getTransactions().size());
    }

    @Test
    public void usesSeparatePortfolioForEachUsername() {
        RecordingOutputBoundary presenter = new RecordingOutputBoundary();
        PortfolioInteractor interactor = new PortfolioInteractor(
                ticker -> new BigDecimal("10"),
                presenter);

        interactor.execute(new PortfolioInputData(
                "alice", "AAPL", 1, PortfolioInputData.Operation.BUY));
        interactor.execute(new PortfolioInputData(
                "bob", "AAPL", 1, PortfolioInputData.Operation.SELL));

        assertFalse(presenter.tradeOutput.isSuccess());
        assertEquals(new BigDecimal("10000.00"),
                presenter.tradeOutput.getAvailableCash());
    }
}
