package presentation.portfolio;

import use_case.portfolio.HoldingOutputData;
import use_case.portfolio.TransactionOutputData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Stores the presentation state of the simulated portfolio.
 */
public final class PortfolioViewModel {

    private String message = "";
    private boolean success;
    private BigDecimal availableCash = BigDecimal.ZERO;
    private BigDecimal portfolioValue = BigDecimal.ZERO;
    private BigDecimal profitLoss = BigDecimal.ZERO;

    private List<HoldingOutputData> holdings = new ArrayList<>();
    private List<TransactionOutputData> transactions = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public BigDecimal getAvailableCash() {
        return availableCash;
    }

    public BigDecimal getPortfolioValue() {
        return portfolioValue;
    }

    public BigDecimal getProfitLoss() {
        return profitLoss;
    }

    public List<HoldingOutputData> getHoldings() {
        return Collections.unmodifiableList(holdings);
    }

    public List<TransactionOutputData> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public void setTradeResult(String message, boolean success, BigDecimal availableCash) {
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.success = success;
        this.availableCash = Objects.requireNonNull(
                availableCash, "availableCash must not be null");
    }

    public void setPortfolioData(
            BigDecimal availableCash,
            BigDecimal portfolioValue,
            BigDecimal profitLoss,
            List<HoldingOutputData> holdings) {

        this.availableCash = Objects.requireNonNull(
                availableCash, "availableCash must not be null");
        this.portfolioValue = Objects.requireNonNull(
                portfolioValue, "portfolioValue must not be null");
        this.profitLoss = Objects.requireNonNull(
                profitLoss, "profitLoss must not be null");
        Objects.requireNonNull(holdings, "holdings must not be null");
        if (holdings.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("holdings must not contain null");
        }
        this.holdings = new ArrayList<>(holdings);
    }

    public void setTransactions(List<TransactionOutputData> transactions) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        if (transactions.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("transactions must not contain null");
        }
        this.transactions = new ArrayList<>(transactions);
    }
}
