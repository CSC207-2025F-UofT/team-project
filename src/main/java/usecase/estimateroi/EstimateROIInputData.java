package usecase.estimateroi;

public class EstimateROIInputData {

    private final String ticker;
    private final double amount;
    private final int historyLength;

    public EstimateROIInputData(String ticker, double amount, int historyLength) {
        this.ticker = ticker;
        this.amount = amount;
        this.historyLength = historyLength;
    }

    public String getTicker() {
        return ticker;
    }

    public double getAmount() {
        return amount;
    }

    public int getHistoryLength() {
        return historyLength;
    }
}