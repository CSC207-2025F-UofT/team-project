package dataaccessobjects;

import dataaccessinterface.WeatherTileApiFetcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OkHttpsWeatherTileApiFetcher implements WeatherTileApiFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public List<BufferedImage> getSubBreeds(String breed) throws BreedNotFoundException {
        ArrayList<String> subbreedList = new ArrayList<>();
        final Request request = new Request.Builder()
                .url(String.format("%s/%s/list", "https://dog.ceo/api/breed", breed))
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject responseBody = new JSONObject(response.body().string());

            if (responseBody.getString("status").equals("success")) {
                final JSONArray subbreeds = responseBody.getJSONArray("message");
                for (var breedName : subbreeds) {
                    subbreedList.add(breedName.toString());
                }
            }
            else {
                throw new BreedFetcher.BreedNotFoundException("Subbreeds could not be found for " + breed);
            }
        } catch (IOException e) {
            throw new BreedFetcher.BreedNotFoundException("Subbreeds could not be found for " + breed);
        }
        return subbreedList;
    }
}
