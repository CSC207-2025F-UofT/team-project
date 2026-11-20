package presentation.portfolio;

import use_case.portfolio.PortfolioOutputBoundary;
import use_case.portfolio.TradeOutputData;
import use_case.portfolio.PortfolioViewOutputData;
import use_case.portfolio.TransactionHistoryOutputData;

import java.util.Objects;

/**
 * Converts portfolio use-case output data into presentation state.
 */
public final class PortfolioPresenter implements PortfolioOutputBoundary {

    private final PortfolioViewModel viewModel;

    public PortfolioPresenter(PortfolioViewModel viewModel) {
        this.viewModel = Objects.requireNonNull(
                viewModel, "viewModel must not be null");
    }

    @Override
    public void prepareBuyView(TradeOutputData outputData) {
        Objects.requireNonNull(outputData, "outputData must not be null");
        viewModel.setTradeResult(
                outputData.getMessage(),
                outputData.isSuccess(),
                outputData.getAvailableCash());
    }

    @Override
    public void prepareSellView(TradeOutputData outputData) {
        Objects.requireNonNull(outputData, "outputData must not be null");
        viewModel.setTradeResult(
                outputData.getMessage(),
                outputData.isSuccess(),
                outputData.getAvailableCash());
    }

    @Override
    public void preparePortfolioView(PortfolioViewOutputData outputData) {
        Objects.requireNonNull(outputData, "outputData must not be null");
        viewModel.setPortfolioData(
                outputData.getAvailableCash(),
                outputData.getPortfolioValue(),
                outputData.getProfitLoss(),
                outputData.getHoldings());
    }

    @Override
    public void prepareTransactionHistoryView(
            TransactionHistoryOutputData outputData) {
        Objects.requireNonNull(outputData, "outputData must not be null");
        viewModel.setTransactions(outputData.getTransactions());
    }
}
