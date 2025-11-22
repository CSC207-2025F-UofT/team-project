package entity;

public class FinancialStatement {

    private final String ticker;
    private final String reportingPeriod;
    private final double revenue;
    private final double netIncome;
    private final double totalAssets;
    private final double totalLiabilities;
    private final double operatingCashFlow;

    public FinancialStatement(String ticker, String reportingPeriod, double revenue, double netIncome,
                              double totalAssets, double totalLiabilities, double operatingCashFlow) {
        this.ticker = ticker;
        this.reportingPeriod = reportingPeriod;
        this.revenue = revenue;
        this.netIncome = netIncome;
        this.totalAssets = totalAssets;
        this.totalLiabilities = totalLiabilities;
        this.operatingCashFlow = operatingCashFlow;
    }

    public String getTicker() { return ticker; }
    public String getReportingPeriod() { return reportingPeriod; }
    public double getRevenue() { return revenue; }
    public double getNetIncome() { return netIncome; }
    public double getTotalAssets() { return totalAssets; }
    public double getTotalLiabilities() { return totalLiabilities; }
    public double getOperatingCashFlow() { return operatingCashFlow; }
}