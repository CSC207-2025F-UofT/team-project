package use_case;
import entity.PricePoint;
import entity.TimeInterval;
import java.util.List;
public interface PriceDataAccessInterface {
    List<PricePoint> getPriceHistory(String ticker, TimeInterval interval) throws Exception;
}