package use_case;
import entity.TimeInterval;
public interface PriceInputBoundary {
    void loadPriceHistory(String ticker, TimeInterval interval);
}
