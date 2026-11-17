package stockprototype;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Locale;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

/** Only retrieves and parses data; comparison calculations belong in a use case. */
public final class AlphaVantageClient implements StockDataSource {
    private final String apiKey;
    private final Transport transport;

    public AlphaVantageClient(String apiKey) {
        this(apiKey, new HttpTransport());
    }

    // Injectable transport lets tests verify requests without network access or real keys.
    AlphaVantageClient(String apiKey, Transport transport) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new IllegalArgumentException("Set ALPHA_VANTAGE_API_KEY in your run configuration.");
        }
        this.apiKey = apiKey.trim();
        this.transport = Objects.requireNonNull(transport);
    }

    @Override
    public NavigableMap<LocalDate, BigDecimal> getDailyClosingPrices(String symbol) throws IOException {
        String normalized = normalizeSymbol(symbol);
        String query = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
                + "&symbol=" + encode(normalized) + "&outputsize=compact&datatype=json&apikey=" + encode(apiKey);
        Reply reply;
        try {
            reply = transport.get(URI.create(query));
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("Stock data request was interrupted.");
        }
        catch (IOException exception) {
            // Do not expose a transport exception containing the URL and API key.
            throw new IOException("Could not retrieve stock data. Check the connection and try later.");
        }
        if (reply.status != 200) {
            throw new IOException("Stock service returned HTTP " + reply.status + ".");
        }
        return parseDailyPrices(reply.body, normalized);
    }

    static String normalizeSymbol(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("Enter a stock symbol.");
        }
        String normalized = symbol.trim().toUpperCase(Locale.ROOT);
        if (!normalized.matches("[A-Z0-9][A-Z0-9.\\-]{0,29}")) {
            throw new IllegalArgumentException("Enter a stock symbol using letters, digits, dots or hyphens.");
        }
        return normalized;
    }

    static NavigableMap<LocalDate, BigDecimal> parseDailyPrices(String body, String expectedSymbol)
            throws IOException {
        try {
            JSONObject root = new JSONObject(body);
            if (root.has("Error Message")) {
                throw new IOException("The stock service rejected the request. Check the symbol and API access.");
            }
            if (root.has("Note") || root.has("Information")) {
                throw new IOException("The stock service returned an access or usage-limit notice instead of prices.");
            }
            String actualSymbol = root.getJSONObject("Meta Data").getString("2. Symbol");
            if (!expectedSymbol.equalsIgnoreCase(actualSymbol)) {
                throw new IOException("The returned stock does not match the requested symbol.");
            }
            JSONObject series = root.getJSONObject("Time Series (Daily)");
            if (series.isEmpty()) {
                throw new IOException("No daily prices were returned for this stock.");
            }
            NavigableMap<LocalDate, BigDecimal> prices = new TreeMap<>();
            for (String day : series.keySet()) {
                LocalDate date = LocalDate.parse(day);
                BigDecimal close = new BigDecimal(series.getJSONObject(day).get("4. close").toString());
                if (close.signum() <= 0) {
                    throw new IOException("The response contains a nonpositive closing price.");
                }
                prices.put(date, close);
            }
            return Collections.unmodifiableNavigableMap(prices);
        }
        catch (RuntimeException exception) {
            throw new IOException("The stock service returned malformed or incomplete daily price data.");
        }
    }

    private static String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    interface Transport {
        Reply get(URI uri) throws IOException, InterruptedException;
    }

    static final class Reply {
        private final int status;
        private final String body;

        Reply(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }

    private static final class HttpTransport implements Transport {
        private final HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10)).build();

        @Override
        public Reply get(URI uri) throws IOException, InterruptedException {
            HttpRequest request = HttpRequest.newBuilder(uri)
                    .timeout(Duration.ofSeconds(20)).header("Accept", "application/json").GET().build();
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
            return new Reply(response.statusCode(), response.body());
        }
    }
}
