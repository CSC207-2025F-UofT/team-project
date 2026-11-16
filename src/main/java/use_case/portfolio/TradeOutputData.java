package use_case.portfolio;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Output data returned after a buy or sell operation.
 */
public final class TradeOutputData {
    private final boolean success;
    private final String message;
    private final BigDecimal availableCash;

    public TradeOutputData(boolean success, String message, BigDecimal availableCash) {
        this.success = success;
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.availableCash = Objects.requireNonNull(
                availableCash, "availableCash must not be null");
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public BigDecimal getAvailableCash() {
        return availableCash;
    }
}
