package api;

public class ApiTest {
    public static void main(String[] args) {
        try {
            Api api = new Api("3TEXXG3G2UXFI7E2");

            // Test 1: Overview
            System.out.println("=== OVERVIEW ===");
            System.out.println(api.getOverview("IBM"));

            // Test 2: Intraday (5min)
            System.out.println("\n=== INTRADAY ===");
            System.out.println(api.getFuncTimeSeriesIntraday("IBM"));

            // Test 3: Daily Adjusted
            System.out.println("\n=== DAILY ADJUSTED ===");
            System.out.println(api.getFuncTimeSeriesDailyAdjusted("IBM"));

            // Test 4: Weekly Adjusted
            System.out.println("\n=== WEEKLY ADJUSTED ===");
            System.out.println(api.getFuncTimeSeriesWeeklyAdjusted("IBM"));

            // Test 5: Income Statement
            System.out.println("\n=== INCOME STATEMENT ===");
            System.out.println(api.getFuncIncomeStatement("IBM"));

            // Test 6: Balance Sheet
            System.out.println("\n=== BALANCE SHEET ===");
            System.out.println(api.getFuncBalanceSheet("IBM"));

            // Test 7: Cash Flow
            System.out.println("\n=== CASH FLOW ===");
            System.out.println(api.getFuncCashFlow("IBM"));

            // Test 8: News Sentiment
            System.out.println("\n=== NEWS SENTIMENT ===");
            System.out.println(api.getFuncNewsSentiment("IBM"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

