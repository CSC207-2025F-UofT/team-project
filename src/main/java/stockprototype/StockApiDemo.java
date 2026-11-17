package stockprototype;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.NavigableMap;

/** No arguments runs offline. --live SYMBOL makes one real API request. */
public final class StockApiDemo {
    private StockApiDemo() { }

    public static void main(String[] args) {
        try {
            String symbol;
            NavigableMap<LocalDate, BigDecimal> prices;
            if (args.length == 0) {
                symbol = "SAMPLE";
                System.out.println("OFFLINE DEMO: synthetic prices, not actual market data. No API call.");
                try (InputStream input = StockApiDemo.class.getResourceAsStream("/sample-daily.json")) {
                    if (input == null) {
                        throw new IOException("Missing sample-daily.json resource.");
                    }
                    prices = AlphaVantageClient.parseDailyPrices(
                            new String(input.readAllBytes(), StandardCharsets.UTF_8), symbol);
                }
            }
            else if (args.length == 2 && "--live".equals(args[0])) {
                symbol = AlphaVantageClient.normalizeSymbol(args[1]);
                StockDataSource source = new AlphaVantageClient(System.getenv("ALPHA_VANTAGE_API_KEY"));
                prices = source.getDailyClosingPrices(symbol);
                System.out.println("LIVE RESPONSE: raw daily closing prices; not live intraday quotes.");
            }
            else {
                throw new IllegalArgumentException("Usage: StockApiDemo [--live SYMBOL]");
            }
            System.out.println("Symbol: " + symbol);
            System.out.println("Daily prices: " + prices.size());
            System.out.println("Earliest date: " + prices.firstKey());
            System.out.println("Latest date: " + prices.lastKey());
            prices.entrySet().stream().skip(Math.max(0, prices.size() - 5))
                    .forEach(entry -> System.out.println(entry.getKey() + "  " + entry.getValue().toPlainString()));
        }
        catch (IOException | IllegalArgumentException exception) {
            System.err.println(exception.getMessage());
            System.exit(1);
        }
    }
}
