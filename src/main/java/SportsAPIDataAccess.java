import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SportsAPIDataAccess {
    private static final String key = "0c5ba9bf08780b2ed18e605b84f07565";
    private static final String OUTPUT_FILE = "sportsbets.txt";
    String sport = "basketball_nba";  // Example sport
    String url = "https://api.the-odds-api.com/v4/sports/" + sport +
            "/odds?regions=us&markets=h2h&apiKey=" + key;

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
