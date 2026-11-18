package usecase.estimateroi;

import java.util.List;

public interface EstimateROIDataAccessInterface {

    List<Double> getHistoricalPrices(String ticker, int historyLength);
}