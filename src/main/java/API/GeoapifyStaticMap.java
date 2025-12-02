package API;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GeoapifyStaticMap implements MoveStaticMapInterface {
    private static final int ZOOM_LEVEL = 17;
    private static final int MAP_WIDTH = 600;
    private static final int MAP_HEIGHT = 400;
    private static final String API_KEY = "56cbecbec7ef4abc89b419650ca68cba"; // since this is public, we need to disable after the demo

    private final OkHttpClient httpClient;

    public GeoapifyStaticMap() {
        this.httpClient = new OkHttpClient();
    }

    @Override
    public ImageIcon getMapImage(double latitude, double longitude) {
        try {
            return fetchMapFromGeoapify(latitude, longitude);
        } catch (IOException e) {
            System.err.println("Failed to fetch map from Geoapify: " + e.getMessage());
            return createPlaceholderImage(latitude, longitude);
        }
    }

    private ImageIcon fetchMapFromGeoapify(double latitude, double longitude) throws IOException {
        String url = buildGeoapifyUrl(latitude, longitude);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response code: " + response.code());
            }

            if (response.body() == null) {
                throw new IOException("Empty response body");
            }

            byte[] imageBytes = response.body().bytes();
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));

            if (bufferedImage == null) {
                throw new IOException("Failed to parse image from response");
            }

            return new ImageIcon(bufferedImage);
        }
    }

    private String buildGeoapifyUrl(double latitude, double longitude) {
        return String.format(
                "https://maps.geoapify.com/v1/staticmap?style=osm-bright&width=%d&height=%d&center=lonlat:%f,%f&zoom=%d&apiKey=%s",
                MAP_WIDTH,
                MAP_HEIGHT,
                longitude,
                latitude,
                ZOOM_LEVEL,
                API_KEY
        );
    }

    private ImageIcon createPlaceholderImage(double lat, double lon) {
        BufferedImage image = new BufferedImage(
                MAP_WIDTH, MAP_HEIGHT, BufferedImage.TYPE_INT_RGB
        );
        Graphics2D g = image.createGraphics();

        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, MAP_WIDTH, MAP_HEIGHT);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Map Unavailable", MAP_WIDTH / 2 - 60, MAP_HEIGHT / 2 - 20);
        g.drawString(String.format("Lat: %.4f, Lon: %.4f", lat, lon), MAP_WIDTH / 2 - 80, MAP_HEIGHT / 2);
        g.drawString("(Network Error)", MAP_WIDTH / 2 - 60, MAP_HEIGHT / 2 + 20);

        g.dispose();

        return new ImageIcon(image);
    }
}
