package stockprototype;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.NavigableMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class AlphaVantageClientTest {
    private String sample() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/sample-daily.json")) {
            assertNotNull(input);
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void readsExactPricesAndOrdersDatesChronologically() throws IOException {
        NavigableMap<LocalDate, BigDecimal> prices = AlphaVantageClient.parseDailyPrices(sample(), "SAMPLE");
        assertEquals(3, prices.size());
        assertEquals(LocalDate.of(2026, 11, 10), prices.firstKey());
        assertEquals(LocalDate.of(2026, 11, 12), prices.lastKey());
        assertEquals(new BigDecimal("121.00"), prices.lastEntry().getValue());
        assertThrows(UnsupportedOperationException.class, prices::clear);
    }

    @Test
    void sendsCompactDailyRequestAndNormalizesInput() throws IOException {
        AtomicReference<String> query = new AtomicReference<>();
        String body = sample();
        AlphaVantageClient client = new AlphaVantageClient("test-only-key", uri -> {
            assertEquals("https", uri.getScheme());
            assertEquals("www.alphavantage.co", uri.getHost());
            query.set(uri.getQuery());
            return new AlphaVantageClient.Reply(200, body);
        });
        assertEquals(3, client.getDailyClosingPrices(" sample ").size());
        assertTrue(query.get().contains("function=TIME_SERIES_DAILY"));
        assertTrue(query.get().contains("symbol=SAMPLE"));
        assertTrue(query.get().contains("outputsize=compact"));
    }

    @Test
    void rejectsInvalidInputBeforeSendingRequest() {
        AlphaVantageClient client = new AlphaVantageClient("test-only-key", uri -> {
            fail("Invalid input must not trigger a network call.");
            return null;
        });
        for (String symbol : new String[] {null, "", " ", "IBM&apikey=other", "AAPL/MSFT"}) {
            assertThrows(IllegalArgumentException.class, () -> client.getDailyClosingPrices(symbol));
        }
    }

    @Test
    void missingKeyFailsClearly() {
        assertThrows(IllegalArgumentException.class, () -> new AlphaVantageClient(null));
        assertThrows(IllegalArgumentException.class, () -> new AlphaVantageClient(" "));
    }

    @Test
    void rejectsProviderNoticesEvenWithSuccessfulHttpStatus() {
        for (String field : new String[] {"Information", "Note", "Error Message"}) {
            String body = new JSONObject().put(field, "provider message containing test-secret").toString();
            AlphaVantageClient client = new AlphaVantageClient("test-only-key",
                    uri -> new AlphaVantageClient.Reply(200, body));
            IOException error = assertThrows(IOException.class, () -> client.getDailyClosingPrices("IBM"));
            assertFalse(error.getMessage().contains("test-secret"));
        }
    }

    @Test
    void rejectsMalformedMissingEmptyAndMismatchedData() throws IOException {
        for (String body : new String[] {"not json", "{}",
                "{\"Meta Data\":{\"2. Symbol\":\"SAMPLE\"},\"Time Series (Daily)\":{}}",
                sample().replace("SAMPLE", "OTHER"),
                sample().replace("2026-11-12", "2026-99-99"),
                sample().replace("121.00", "NaN"),
                sample().replace("121.00", "0"),
                sample().replace("121.00", "-10")}) {
            assertThrows(IOException.class, () -> AlphaVantageClient.parseDailyPrices(body, "SAMPLE"));
        }
    }

    @Test
    void rejectsHttpFailureWithoutLeakingResponseBody() {
        AlphaVantageClient client = new AlphaVantageClient("test-only-key",
                uri -> new AlphaVantageClient.Reply(429, "private-response"));
        IOException error = assertThrows(IOException.class, () -> client.getDailyClosingPrices("IBM"));
        assertEquals("Stock service returned HTTP 429.", error.getMessage());
    }

    @Test
    void hidesCredentialBearingTransportErrors() {
        AlphaVantageClient client = new AlphaVantageClient("test-only-secret", uri -> {
            throw new IOException("Request failed: " + uri);
        });
        IOException error = assertThrows(IOException.class, () -> client.getDailyClosingPrices("IBM"));
        assertFalse(error.toString().contains("test-only-secret"));
        assertNull(error.getCause());
    }

    @Test
    void preservesThreadInterruption() {
        AlphaVantageClient client = new AlphaVantageClient("test-only-key", uri -> {
            throw new InterruptedException();
        });
        try {
            assertThrows(IOException.class, () -> client.getDailyClosingPrices("IBM"));
            assertTrue(Thread.currentThread().isInterrupted());
        }
        finally {
            Thread.interrupted();
        }
    }
}
