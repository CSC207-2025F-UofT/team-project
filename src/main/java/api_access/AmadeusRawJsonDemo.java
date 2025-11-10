package api_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AmadeusRawJsonDemo {
    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://test.api.amadeus.com/v2/shopping/flight-offers?originLocationCode=SYD&destinationLocationCode=BKK&departureDate=2025-12-02&adults=1&nonStop=true")
                .get()
                .addHeader("Authorization", "Bearer un0QDeDRlCyaUrgeocdJsjpHsuA1")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("HTTP " + response.code());
            System.out.println(response.body() != null ? response.body().string() : "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

