package data_access;

import entity.Player;
import use_case.best_team.BestTeamDataAccessInterface;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BestTeamDataAccessObject implements BestTeamDataAccessInterface{
    private static final String API_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private final OkHttpClient client;

    public BestTeamDataAccessObject(){
        this.client = new OkHttpClient();
    }

    @Override
    public List<Player> getAllPlayers() {
        Request request = new Request.Builder().url(API_URL).get().build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()){
                throw new IOException("Unexpected code " + response);
            }

            String json = response.body().toString();
            // Parsing JSON into Player object to be followed
            // For now, we will just return an empty list

            return parsePlayerFromJson(json);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load player data from API.", e);
        }
    }

    private List<Player> parsePlayerFromJson(String json){
        // Parsing JSON to be followed
        return new ArrayList<>();
    }
}
