package frameworks_and_drivers.currency;

import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

/** Outer-layer HTTP client for Bank of Canada Valet foreign-exchange observations. */
public final class BankOfCanadaCurrencyClient {
    private final HttpClient httpClient;

    public BankOfCanadaCurrencyClient() {
        this(HttpClient.newHttpClient());
    }

    public BankOfCanadaCurrencyClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /** Returns CAD per one unit of the requested foreign currency. */
    public BigDecimal getCadPerUnit(String currencyCode, LocalDate date)
            throws IOException, InterruptedException {
        if (currencyCode == null || !currencyCode.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a three-letter code.");
        }
        if ("CAD".equals(currencyCode)) return BigDecimal.ONE;

        String series = "FX" + currencyCode + "CAD";
        String url = "https://www.bankofcanada.ca/valet/observations/" + series
            + "/json?start_date=" + date + "&end_date=" + date;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Bank of Canada request failed: HTTP " + response.statusCode());
        }

        JSONObject json = new JSONObject(response.body());
        if (json.getJSONArray("observations").length() == 0) {
            throw new IOException("No exchange-rate observation is available for " + date + ".");
        }
        String value = json.getJSONArray("observations").getJSONObject(0)
            .getJSONObject(series).get("v").toString();
        return new BigDecimal(value);
    }
}
