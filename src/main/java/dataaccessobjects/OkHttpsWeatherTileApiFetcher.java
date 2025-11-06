package dataaccessobjects;

import dataaccessinterface.WeatherTileApiFetcher;

import java.awt.image.BufferedImage;
import java.io.IOException;
import entity.WeatherTile;
import entity.WeatherType;
import okhttp3.OkHttpClient;

import javax.imageio.ImageIO;

public class OkHttpsWeatherTileApiFetcher implements WeatherTileApiFetcher {
    private final OkHttpClient client = new OkHttpClient();

    public BufferedImage getWeatherTileImageData(WeatherTile tile) throws TileNotFoundException {
        String url = "https://weathermaps.weatherapi.com/";
        final Request request = new Request.Builder()
                .url(String.format("%s/%s/tiles/%s/%s/%s/%s/%s.png",
                        url,
                        tile.getWeatherType().name().toLowerCase(),
                        tile.getTimestamp(),
                        tile.getTimestamp(),
                        tile.getCoordinates().x,
                        tile.getCoordinates().y,
                        tile.getCoordinates().zoom))
                .addHeader("Content-Type", "application/json")
                .build();

        try {
            final Response response = client.newCall(request).execute();
            if (response.code() == 404) {
                throws new IOException();
            }
            try {
                InputStream in = response.body().byteStream();
                BufferedImage image = ImageIO.read(in);
                if (image == null){
                    throws new IOException();
                }
                return image;
            }
            catch (IOException){
                throws new IOException();
            }
        } catch (IOException e) {
            throw new BreedFetcher.BreedNotFoundException("Subbreeds could not be found for " + breed);
        }
        return subbreedList;
    }
}
