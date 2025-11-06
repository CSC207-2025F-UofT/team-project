package dataaccessobjects;

import dataaccessinterface.WeatherTileApiFetcher;
import java.io.IOException;
import entity.WeatherTile;
import entity.WeatherType;
import okhttp3.OkHttpClient;

public class OkHttpsWeatherTileApiFetcher implements WeatherTileApiFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public BufferedImage getWeatherTileImageData(WeatherTile tile) throws TileNotFoundException {
        String url = "https://weathermaps.weatherapi.com/";
        final Request request = new Request.Builder()
                .url(String.format("%s/tiles/%s/%s/%s/%s/%s.png",
                        url,
                        tile.getWeatherType().name().toLowerCase(),
                        tile.getTimestamp(),
                        tile.getTimestamp(),
                        tile.getCoordinates().getX(),
                        tile.getCoordinates().getY(),
                        tile.getCoordinates().getZoom()))
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
