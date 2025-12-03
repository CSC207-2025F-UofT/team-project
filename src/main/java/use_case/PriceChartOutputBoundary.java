package use_case;
import entity.PricePoint;
import entity.TimeInterval;
import entity.ChartViewModel;
import java.util.List;
public interface PriceChartOutputBoundary {
    void presentPriceHistory(List<PricePoint> priceData, String ticker, TimeInterval interval);
    void presentError(String message);
}