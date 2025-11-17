package use_case.clicking;

import com.google.gson.Gson;
import entity.MediaDetailsResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ClickingDataAccessTMDb implements ClickingDataAccessInterface {

    private final String API_KEY = "aaebf7ad961711073ad8cc634faaa700";
    private final Gson gson = new Gson();

    @Override
    public MediaDetailsResponse fetchDetailsById(int id) {
        try {
            String endpoint = "https://api.themoviedb.org/3/movie/" + id
                    + "?api_key=" + API_KEY + "&language=en-US";

            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            MediaDetailsResponse response = gson.fromJson(reader, MediaDetailsResponse.class);
            reader.close();

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
