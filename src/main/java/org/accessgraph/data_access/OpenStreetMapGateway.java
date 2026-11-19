package org.accessgraph.data_access;

import org.accessgraph.entity.GeoPosition;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapImage;
import org.accessgraph.use_case.station_map.MapImageGateway;
import org.accessgraph.use_case.station_map.MapRequest;
import org.accessgraph.use_case.station_map.MapViewport;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.accessgraph.use_case.station_map.MapViewport.HEIGHT;
import static org.accessgraph.use_case.station_map.MapViewport.TILE_SIZE;
import static org.accessgraph.use_case.station_map.MapViewport.WIDTH;

/** Composites visible OSM tiles and renders TTC and journey pins locally. */
public final class OpenStreetMapGateway implements MapImageGateway {
    private static final String USER_AGENT = "AccessGraph/1.0 (+https://github.com/probably-dean-wu/group-17)";
    private static final String DEFAULT_TILE_URL = "https://tile.openstreetmap.org";
    private static final String DEFAULT_SEARCH_URL = "https://photon.komoot.io/api";
    private static final int PADDING = 36;
    private static final int FOCUS_ZOOM = 15;
    private static final int MAX_ADDRESS_BYTES = 64 * 1024;
    private static final String ATTRIBUTION = "© OpenStreetMap contributors";
    private final HttpClient client;
    private final URI tiles;
    private final URI search;
    private final MapTileCache cache;
    private String lastAddress = "";
    private Point2D.Double lastMatch;

    public OpenStreetMapGateway(HttpClient client) {
        this(client, URI.create(System.getProperty("accessgraph.tiles.url", DEFAULT_TILE_URL)),
                URI.create(System.getProperty("accessgraph.geocoder.url", DEFAULT_SEARCH_URL)),
                Path.of(System.getProperty("user.home"), ".accessgraph", "tiles"));
    }

    OpenStreetMapGateway(HttpClient client, URI tiles, URI search, Path cacheDirectory) {
        this.client = client;
        this.tiles = tiles;
        this.search = search;
        // Keep tiles from different providers in distinct cache directories.
        this.cache = new MapTileCache(cacheDirectory.resolve(UUID.nameUUIDFromBytes(
                tiles.toString().getBytes(StandardCharsets.UTF_8)).toString()));
    }

    @Override
    public MapImage fetch(MapRequest request) throws IOException, InterruptedException {
        Point2D.Double destination = request.getDestination().isEmpty() ? null : geocode(request.getDestination());
        List<Pin> pins = new ArrayList<>();
        Pin focus = null;
        for (Station station : request.getStations()) {
            Pin pin = stationPin(station);
            pins.add(pin);
            if (station == request.getFocus()) { focus = pin; }
        }
        GeoPosition position = request.getPosition();
        if (position != null) {
            pins.add(new Pin(position.getLongitude(), position.getLatitude(), new Color(0x1455A3), "U"));
        }
        if (destination != null) {
            pins.add(new Pin(destination.x, destination.y, new Color(0xAC2737), "D"));
        }
        if (focus != null) {
            pins.remove(focus);
            pins.add(new Pin(focus.longitude, focus.latitude, new Color(0xAC2737), "S"));
        }

        MapViewport viewport = request.getViewport();
        if (viewport == null) { viewport = viewOf(pins, focus); }
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(new Color(0xE8EEF1));
            graphics.fillRect(0, 0, WIDTH, HEIGHT);
            drawTiles(graphics, viewport);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (Pin pin : pins) { drawPin(graphics, viewport, pin); }
            graphics.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
            int creditWidth = graphics.getFontMetrics().stringWidth(ATTRIBUTION) + 12;
            graphics.setColor(Color.WHITE);
            graphics.fillRect(WIDTH - creditWidth, HEIGHT - 23, creditWidth, 23);
            graphics.setColor(Color.BLACK);
            graphics.drawString(ATTRIBUTION, WIDTH - creditWidth + 6, HEIGHT - 7);
        } finally {
            graphics.dispose();
        }
        return new MapImage(image, viewport);
    }

    private static Pin stationPin(Station station) {
        Color color;
        switch (station.getAccessibility()) {
            case CONFIRMED: color = new Color(0x006B68); break;
            case LIMITED: color = new Color(0xA15B00); break;
            default: color = new Color(0x657681);
        }
        return new Pin(station.getLongitude(), station.getLatitude(), color, "");
    }

    private static MapViewport viewOf(List<Pin> pins, Pin focus) {
        if (focus != null) {
            Point2D.Double center = project(focus.longitude, focus.latitude, FOCUS_ZOOM);
            return centeredAt(FOCUS_ZOOM, center.x, center.y);
        }
        // The bounds at zoom zero are scaled up until they fill the viewport with a pin margin.
        double west = Double.POSITIVE_INFINITY;
        double north = Double.POSITIVE_INFINITY;
        double east = Double.NEGATIVE_INFINITY;
        double south = Double.NEGATIVE_INFINITY;
        for (Pin pin : pins) {
            Point2D.Double point = project(pin.longitude, pin.latitude, 0);
            west = Math.min(west, point.x);
            north = Math.min(north, point.y);
            east = Math.max(east, point.x);
            south = Math.max(south, point.y);
        }
        int zoom = FOCUS_ZOOM;
        while (zoom > 0 && (Math.scalb(east - west, zoom) > WIDTH - 2 * PADDING
                || Math.scalb(south - north, zoom) > HEIGHT - 2 * PADDING)) {
            zoom--;
        }
        return centeredAt(zoom, Math.scalb((west + east) / 2, zoom), Math.scalb((north + south) / 2, zoom));
    }

    private static MapViewport centeredAt(int zoom, double x, double y) {
        return new MapViewport(zoom, x - WIDTH / 2.0, y - HEIGHT / 2.0);
    }

    private static Point2D.Double project(double longitude, double latitude, int zoom) {
        int size = TILE_SIZE << zoom;
        double limitedLatitude = Math.max(-85.05112878, Math.min(85.05112878, latitude));
        double sin = Math.sin(Math.toRadians(limitedLatitude));
        double x = (longitude + 180) / 360 * size;
        double y = (0.5 - Math.log((1 + sin) / (1 - sin)) / (4 * Math.PI)) * size;
        return new Point2D.Double(x, y);
    }

    private void drawTiles(Graphics2D graphics, MapViewport view) throws IOException, InterruptedException {
        int tileCount = 1 << view.getZoom();
        int left = view.getLeft();
        int top = view.getTop();
        int firstX = Math.floorDiv(left, TILE_SIZE);
        int lastX = Math.floorDiv(left + WIDTH - 1, TILE_SIZE);
        int firstY = Math.max(0, Math.floorDiv(top, TILE_SIZE));
        int lastY = Math.min(tileCount - 1, Math.floorDiv(top + HEIGHT - 1, TILE_SIZE));
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
        HttpResponse<byte[]> response = download(request.build(), "OpenStreetMap");
        if (response.statusCode() == 304 && saved != null) {
            cache.store(cacheKey, response.headers(), saved.image, saved);
            return savedImage;
        }
        requireSuccess(response, "OpenStreetMap");
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

    private static void drawPin(Graphics2D graphics, MapViewport view, Pin pin) {
        Point2D.Double point = project(pin.longitude, pin.latitude, view.getZoom());
        int worldWidth = TILE_SIZE << view.getZoom();
        int x = (int) Math.round(point.x) - view.getLeft();
        x += Math.round((WIDTH / 2.0 - x) / worldWidth) * worldWidth;
        int y = (int) Math.round(point.y) - view.getTop();
        int radius = pin.label.isEmpty() ? 4 : 10;
        graphics.setColor(Color.WHITE);
        graphics.fillOval(x - radius - 1, y - radius - 1, 2 * radius + 2, 2 * radius + 2);
        graphics.setColor(pin.color);
        graphics.fillOval(x - radius, y - radius, 2 * radius, 2 * radius);
        if (!pin.label.isEmpty()) {
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
            graphics.drawString(pin.label, x - graphics.getFontMetrics().stringWidth(pin.label) / 2, y + 4);
        }
    }

    private Point2D.Double geocode(String address) throws IOException, InterruptedException {
        synchronized (this) {
            if (address.equals(lastAddress)) { return lastMatch; }
        }
        URI uri = URI.create(search + "?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8)
                + "&limit=1&lat=43.65&lon=-79.38");
        HttpResponse<byte[]> response = download(request(uri).header("Accept", "application/json").build(), "Photon");
        requireSuccess(response, "Photon");
        if (response.body().length > MAX_ADDRESS_BYTES) { throw new IOException("Photon returned an oversized response."); }
        try {
            JSONArray features = new JSONObject(new String(response.body(), StandardCharsets.UTF_8))
                    .getJSONArray("features");
            if (features.isEmpty()) { throw new IOException("No destination found. Include the city or postal code and try again."); }
            JSONObject geometry = features.getJSONObject(0).getJSONObject("geometry");
            JSONArray coordinates = geometry.getJSONArray("coordinates");
            double longitude = coordinates.getDouble(0);
            double latitude = coordinates.getDouble(1);
            if (!"Point".equals(geometry.getString("type")) || !Double.isFinite(longitude) || Math.abs(longitude) > 180
                    || !Double.isFinite(latitude) || Math.abs(latitude) > 90) {
                throw new IOException("Photon returned an invalid destination pin.");
            }
            Point2D.Double match = new Point2D.Double(longitude, latitude);
            synchronized (this) {
                lastAddress = address;
                lastMatch = match;
            }
            return match;
        } catch (JSONException exception) {
            throw new IOException("Photon returned an unreadable destination result.");
        }
    }

    private static HttpRequest.Builder request(URI uri) {
        return HttpRequest.newBuilder(uri).timeout(Duration.ofSeconds(10)).header("User-Agent", USER_AGENT);
    }

    private HttpResponse<byte[]> download(HttpRequest request, String service) throws IOException, InterruptedException {
        CompletableFuture<HttpResponse<byte[]>> pending = client.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray());
        try {
            return pending.get(10, TimeUnit.SECONDS);
        } catch (TimeoutException exception) {
            pending.cancel(true);
            throw new IOException(service + " timed out. Check your connection and try again.");
        } catch (InterruptedException exception) {
            pending.cancel(true);
            throw exception;
        } catch (ExecutionException exception) {
            throw new IOException(service + " could not be reached. Check your connection and try again.");
        }
    }

    private static void requireSuccess(HttpResponse<?> response, String service) throws IOException {
        if (response.statusCode() != 200) {
            throw new IOException(service + " returned HTTP " + response.statusCode() + ". Try again later.");
        }
    }

    private static final class Pin {
        private final double longitude;
        private final double latitude;
        private final Color color;
        private final String label;

        private Pin(double longitude, double latitude, Color color, String label) {
            this.longitude = longitude;
            this.latitude = latitude;
            this.color = color;
            this.label = label;
        }
    }
}
