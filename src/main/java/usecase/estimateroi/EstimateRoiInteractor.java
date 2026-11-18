package usecase.estimateroi;

import java.util.List;

public class EstimateRoiInteractor implements EstimateROIInputBoundary {

    private final EstimateROIDataAccessInterface dataAccess;
    private final EstimateROIOutputBoundary presenter;

    public EstimateRoiInteractor(
            EstimateROIDataAccessInterface dataAccess,
            EstimateROIOutputBoundary presenter) {

        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(EstimateROIInputData inputData) {

        double amount = inputData.getAmount();
        String ticker = inputData.getTicker();
        int historyLength = inputData.getHistoryLength();

        if (!Double.isFinite(amount) || amount <= 0) {
            presenter.prepareFailView(
                    "Please enter a positive investment amount.");
            return;
        }

        if (historyLength <= 0) {
            presenter.prepareFailView(
                    "Please select a valid history length.");
            return;
        }

        List<Double> prices;

        try {
            prices = dataAccess.getHistoricalPrices(ticker, historyLength);
        } catch (RuntimeException e) {
            presenter.prepareFailView(
                    "Unable to retrieve stock data. Please try again.");
            return;
        }

        if (prices == null || prices.size() < 2) {
            presenter.prepareFailView(
                    "Not enough historical data available.");
            return;
        }

        double oldPrice = prices.get(0);
        double currentPrice = prices.get(prices.size() - 1);

        if (oldPrice <= 0 || currentPrice <= 0
                || !Double.isFinite(oldPrice)
                || !Double.isFinite(currentPrice)) {
            presenter.prepareFailView("Invalid stock prices.");
            return;
        }

        double annualReturn = Math.pow(
                currentPrice / oldPrice,
                1.0 / historyLength) - 1;

        double estimatedValue = amount * (1 + annualReturn);
        double profit = estimatedValue - amount;
        double percentageReturn = annualReturn * 100;

        EstimateROIOutputData outputData =
                new EstimateROIOutputData(
                        estimatedValue,
                        profit,
                        percentageReturn,
                        historyLength);

        presenter.prepareSuccessView(outputData);
    }
}

/*
This implementation assumes that historical prices are ordered from oldest
to newest, cover the entire requestion period,
and that historyLength is measured in years.
The DAO will need to return an empty list if the
requested period is unavailable.
 */