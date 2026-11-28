package stockprototype;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.NavigableMap;
import java.util.TreeMap;

/** Reads the bundled synthetic price histories for offline comparisons. */
final class SampleStockDataSource implements StockDataSource {
    @Override
    public NavigableMap<LocalDate, BigDecimal> getDailyClosingPrices(String symbol) throws IOException {
        String ticker = StockSymbol.normalize(symbol);
        InputStream stream = getClass().getResourceAsStream("/comparison-sample.csv");
        if (stream == null) {
            throw new IOException("The sample price file is missing.");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header == null) {
                throw new IOException("The sample price file is empty.");
            }
            int column = Arrays.asList(header.split(",")).indexOf(ticker);
            if (column < 1) {
                throw new IOException("Sample data is available for IBM, AAPL and MSFT. Choose two of these symbols.");
            }
            NavigableMap<LocalDate, BigDecimal> history = new TreeMap<>();
            String row;
            while ((row = reader.readLine()) != null) {
                String[] fields = row.split(",");
                history.put(LocalDate.parse(fields[0]), new BigDecimal(fields[column]));
            }
            return Collections.unmodifiableNavigableMap(history);
        }
    }
}
