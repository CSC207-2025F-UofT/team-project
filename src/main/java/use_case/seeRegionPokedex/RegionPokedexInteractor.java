package use_case.seeRegionPokedex;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import java.io.IOException;

import entity.Pokemon;

public class RegionPokedexInteractor implements RegionPokedexInputBoundary {
    private final Pokemon pokemon;
    private final RegionPokedexOutputBoundary userPresenter;

    public RegionPokedexInteractor(Pokemon pokemon, RegionPokedexOutputBoundary userPresenter) {
        this.pokemon = pokemon;
        this.userPresenter = userPresenter;
    }

    @Override
    public void execute(RegionPokedexInputData regionPokedexInputData) throws IOException {
        String name = regionPokedexInputData.getPokedex().toLowerCase();
        if ("".equals(name)) {
            userPresenter.prepareFailureView(("No pokemon game provided"));
        }
        else {
            OkHttpClient client = new OkHttpClient();

            Request request1 = new Request.Builder()
                    .url("https://pokeapi.co/api/v2/pokedex/" + name)
                    .build();

            try (Response response = client.newCall(request1).execute()) {
                if (!response.isSuccessful()) {
                    userPresenter.prepareFailureView(("Pokedex could not be found"));
                    return;
                }

                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                JSONArray entries = json.getJSONArray("pokemon_entries");

                ArrayList<String> pokemons =  new ArrayList<>();
                for (int i = 0; i < entries.length(); i++) {
                    JSONObject entry = entries.getJSONObject(i);
                    JSONObject species = entry.getJSONObject("pokemon_species");
                    String pokemonName = species.getString("name");
                    pokemons.add(pokemonName);
                }

                RegionPokedexOutputData outputData = new RegionPokedexOutputData(pokemons);

                userPresenter.prepareSuccessView(outputData);

            }




        }


    }
}
