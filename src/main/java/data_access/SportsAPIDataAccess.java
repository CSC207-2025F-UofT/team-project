package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.FileWriter;
import java.io.IOException;

public class SportsAPIDataAccess {
    private OkHttpClient client = new OkHttpClient();
    private String apiKey = "0c5ba9bf08780b2ed18e605b84f07565";

    public void fetchOdds() {
        String sport = "basketball_nba";
        String url = "https://api.the-odds-api.com/v4/sports/" + sport +
                "/odds?regions=us&markets=h2h&apiKey=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("Request failed: " + response.code());
                return;
            }

            String jsonData = response.body().string();

            try (FileWriter writer = new FileWriter("odds.txt")) {
                writer.write(jsonData);
            }

            System.out.println("Data saved to odds.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
