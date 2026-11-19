package org.accessgraph.data_access;

import org.accessgraph.entity.Station;
import org.accessgraph.interface_adapter.MapImageGateway;
import org.accessgraph.use_case.station_map.MapRequest;
import org.accessgraph.use_case.station_map.MapViewport;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.accessgraph.use_case.station_map.MapViewport.TILE_SIZE;

/** Renders the requested OpenStreetMap tiles and draws the pins locally. */
public final class OpenStreetMapGateway implements MapImageGateway {
    private static final String USER_AGENT = "AccessGraph/1.0 (+https://github.com/probably-dean-wu/group-17)";
    private static final String DEFAULT_TILE_URL = "https://tile.openstreetmap.org";
    private static final Color STATION_PIN = new Color(0x06635B);
    private final HttpClient client;
    private final URI tiles;
    private final MapTileCache cache;

    public OpenStreetMapGateway(HttpClient client) {
        this(client, URI.create(System.getProperty("accessgraph.tiles.url", DEFAULT_TILE_URL)),
                Path.of(System.getProperty("user.home"), ".accessgraph", "tiles"));
    }

    OpenStreetMapGateway(HttpClient client, URI tiles, Path cacheDirectory) {
        this.client = client;
        this.tiles = tiles;
        this.cache = new MapTileCache(cacheDirectory.resolve(UUID.nameUUIDFromBytes(
                tiles.toString().getBytes(StandardCharsets.UTF_8)).toString()));
    }

    @Override
    public BufferedImage fetch(MapRequest request) throws IOException, InterruptedException {
        MapViewport view = request.getViewport();
        BufferedImage image = new BufferedImage(view.getWidth(), view.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(new Color(0xE8EEF1));
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
            drawTiles(graphics, view);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Station station : request.getStations()) {
                drawDot(graphics, view.screenPoint(station.getLatitude(), station.getLongitude()), STATION_PIN, 5);
            }
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static void drawDot(Graphics2D graphics, MapViewport.Pixel point, Color color, int radius) {
        int x = (int) Math.round(point.x);
        int y = (int) Math.round(point.y);
        graphics.setColor(Color.WHITE);
        graphics.fillOval(x - radius - 2, y - radius - 2, 2 * radius + 4, 2 * radius + 4);
        graphics.setColor(color);
        graphics.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    private void drawTiles(Graphics2D graphics, MapViewport view) throws IOException, InterruptedException {
        int tileCount = 1 << view.getZoom();
        int left = view.getLeft();
        int top = view.getTop();
        int firstX = Math.floorDiv(left, TILE_SIZE);
        int lastX = Math.floorDiv(left + view.getWidth() - 1, TILE_SIZE);
        int firstY = Math.max(0, Math.floorDiv(top, TILE_SIZE));
        int lastY = Math.min(tileCount - 1, Math.floorDiv(top + view.getHeight() - 1, TILE_SIZE));
        for (int y = firstY; y <= lastY; y++) {
            for (int x = firstX; x <= lastX; x++) {
                if (Thread.interrupted()) { throw new InterruptedException(); }
                graphics.drawImage(tile(view.getZoom(), Math.floorMod(x, tileCount), y),
                        x * TILE_SIZE - left, y * TILE_SIZE - top, null);
            }
        }
    }

    private BufferedImage tile(int zoom, int x, int y) throws IOException, InterruptedException {
        String cacheKey = zoom + "-" + x + "-" + y + ".tile";
        MapTileCache.Entry saved = cache.read(cacheKey);
        BufferedImage savedImage = null;
        if (saved != null) {
            try {
                savedImage = decodeTile(saved.image);
                if (saved.isFresh()) { return savedImage; }
            } catch (IOException ignored) {
                saved = null;
            }
        }
        String base = tiles.toString();
        String separator = base.endsWith("/") ? "" : "/";
        URI uri = URI.create(base + separator + zoom + "/" + x + "/" + y + ".png");
        HttpRequest.Builder request = request(uri).header("Accept", "image/png");
        if (saved != null) {
            if (!saved.etag.isEmpty()) { request.header("If-None-Match", saved.etag); }
            if (!saved.lastModified.isEmpty()) { request.header("If-Modified-Since", saved.lastModified); }
        }
        HttpResponse<byte[]> response = download(request.build());
        if (response.statusCode() == 304 && saved != null) {
            cache.store(cacheKey, response.headers(), saved.image, saved);
            return savedImage;
        }
        requireSuccess(response);
        BufferedImage image = decodeTile(response.body());
        cache.store(cacheKey, response.headers(), response.body(), null);
        return image;
    }

    private static BufferedImage decodeTile(byte[] bytes) throws IOException {
        if (bytes.length == 0 || bytes.length > MapTileCache.MAX_TILE_BYTES) {
            throw new IOException("OpenStreetMap returned a tile with an unexpected size.");
        }
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
        if (image == null || image.getWidth() != TILE_SIZE || image.getHeight() != TILE_SIZE) {
            throw new IOException("OpenStreetMap returned an unreadable map tile.");
        }
        return image;
    }

    private static HttpRequest.Builder request(URI uri) {
        return HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(10)).header("User-Agent", USER_AGENT);
    }

    private HttpResponse<byte[]> download(HttpRequest request) throws IOException, InterruptedException {
        CompletableFuture<HttpResponse<byte[]>> pending = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray());
        try {
            return pending.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException exception) {
            pending.cancel(true);
            throw new IOException("OpenStreetMap timed out. Check your connection and try again.");
        } catch (InterruptedException exception) {
            pending.cancel(true);
            throw exception;
        } catch (ExecutionException exception) {
            throw new IOException("OpenStreetMap could not be reached. Check your connection and try again.");
        }
    }

    private static void requireSuccess(HttpResponse<?> response) throws IOException {
        if (response.statusCode() != 200) {
            throw new IOException("OpenStreetMap returned HTTP " + response.statusCode() + ". Try again later.");
        }
    }
}
