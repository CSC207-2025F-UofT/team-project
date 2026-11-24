package interface_adapter;

import org.json.JSONObject;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.LocalDate;

/** Original team Bank of Canada rate lookup. */
public class CurrencyAPI {
    private final String url;
    private HttpResponse<String> response;
    private double rate;

    public CurrencyAPI(String currency, LocalDate date) throws IOException, InterruptedException {
        this.url = "https://www.bankofcanada.ca/valet/observations/FX" + currency
            + "CAD/json?start_date=" + date + "&end_date=" + date;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.url)).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        this.response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        this.rate = json.getJSONArray("observations").getJSONObject(0)
            .getJSONObject("FX" + currency + "CAD").getDouble("v");
    }

    public double getResponse() { return this.rate; }
}
