package use_case.portfolio;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Provides the stock price needed by portfolio operations.
 */
public interface StockPriceDataAccessInterface {
    BigDecimal getCurrentPrice(String ticker) throws IOException;
}
