package usecase.estimateroi;

public class EstimateROIOutputData {

    private final double estimatedValue;
    private final double profit;
    private final double percentageReturn;
    private final int historyLength;

    public EstimateROIOutputData(double estimatedValue,
                                 double profit,
                                 double percentageReturn,
                                 int historyLength) {
        this.estimatedValue = estimatedValue;
        this.profit = profit;
        this.percentageReturn = percentageReturn;
        this.historyLength = historyLength;
    }

    public double getEstimatedValue() {
        return estimatedValue;
    }

    public double getProfit() {
        return profit;
    }

    public double getPercentageReturn() {
        return percentageReturn;
    }

    public int getHistoryLength() {
        return historyLength;
    }
}