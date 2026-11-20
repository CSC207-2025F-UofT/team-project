package org.accessgraph.data_access;

import org.accessgraph.interface_adapter.TileGateway;

import javax.imageio.ImageIO;
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

import static org.accessgraph.use_case.station_map.MapViewport.MAX_ZOOM;
import static org.accessgraph.use_case.station_map.MapViewport.TILE_SIZE;

/** Downloads OpenStreetMap tiles and keeps them in a disk cache that honours the provider's HTTP headers. */
public final class OpenStreetMapGateway implements TileGateway {
    private static final String USER_AGENT = "AccessGraph/1.0 (+https://github.com/probably-dean-wu/group-17)";
    private static final String DEFAULT_TILE_URL = "https://tile.openstreetmap.org";
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
    public BufferedImage tile(int zoom, int x, int y) throws IOException, InterruptedException {
        if (zoom < 0 || zoom > MAX_ZOOM || x < 0 || y < 0 || x >= 1 << zoom || y >= 1 << zoom) {
            throw new IllegalArgumentException("Tile outside the map.");
        }
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
        HttpRequest.Builder request = HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(10))
                .header("User-Agent", USER_AGENT).header("Accept", "image/png");
        if (saved != null) {
            if (!saved.etag.isEmpty()) { request.header("If-None-Match", saved.etag); }
            if (!saved.lastModified.isEmpty()) { request.header("If-Modified-Since", saved.lastModified); }
        }
        try {
            HttpResponse<byte[]> response = download(request.build());
            if (response.statusCode() == 304 && saved != null) {
                cache.store(cacheKey, response.headers(), saved.image, saved);
                return savedImage;
            }
            if (response.statusCode() != 200) {
                throw new IOException("OpenStreetMap returned HTTP " + response.statusCode() + ". Try again later.");
            }
            BufferedImage image = decodeTile(response.body());
            cache.store(cacheKey, response.headers(), response.body(), null);
            return image;
        } catch (IOException exception) {
            // An expired tile is better than a blank square while offline or when the server is busy.
            if (savedImage != null) { return savedImage; }
            throw exception;
        }
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
            throw new IOException("OpenStreetMap could not be reached. Check your connection and try again.",
                    exception.getCause());
        }
    }
}
