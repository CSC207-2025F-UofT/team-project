package stockprototype;

import java.util.Locale;

public final class StockSymbol {
    private StockSymbol() { }

    public static String normalize(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Enter a stock symbol.");
        }
        String normalized = symbol.trim().toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z0-9][A-Z0-9.\\-]{0,29}")) {
            throw new IllegalArgumentException("Enter a stock symbol using letters, digits, dots or hyphens.");
        }
        return normalized;
    }
}
