package org.accessgraph.data_access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.accessgraph.entity.GeoPosition;
import org.accessgraph.entity.Station;
import org.accessgraph.use_case.station_map.MapImage;
import org.accessgraph.use_case.station_map.MapRequest;
import org.accessgraph.use_case.station_map.MapViewport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class OpenStreetMapGatewayTest {
    private static final String MODIFIED = "Mon, 09 Nov 2026 09:00:00 GMT";
    private static final String ADDRESS = "100 Queen St W, Toronto & City Hall";
    @TempDir Path cacheDirectory;
    private HttpServer server;
    private byte[] png;
    private final ConcurrentLinkedQueue<URI> tileRequests = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> tileUserAgents = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> conditionalRequests = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> searchQueries = new ConcurrentLinkedQueue<>();
    private volatile boolean expireTiles;
    private volatile int tileStatus = 200;
    private volatile String searchResult = "{\"features\":[{\"geometry\":{\"type\":\"Point\","
            + "\"coordinates\":[-79.383,43.653]}}]}";

    @BeforeEach
    void setUp() throws IOException {
        BufferedImage tile = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = tile.createGraphics();
        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(0, 0, 256, 256);
        graphics.dispose();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(tile, "png", bytes);
        png = bytes.toByteArray();
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/tiles/", this::serveTile);
        server.createContext("/api", response -> {
            try {
                searchQueries.add(response.getRequestURI().getRawQuery());
                send(response, 200, searchResult.getBytes(StandardCharsets.UTF_8));
            } finally {
                response.close();
            }
        });
        server.start();
    }

    @AfterEach
    void tearDown() { server.stop(0); }

    @Test
    void drawsTileAndJourneyPinsAndReusesTheOnDiskCacheAcrossInstances() throws Exception {
        OpenStreetMapGateway maps = gateway();
        List<Station> rows = new BundledStationRepository().load();
        GeoPosition fix = new GeoPosition(43.64, -79.39, 15, Instant.now());
        MapRequest request = new MapRequest(rows, null, fix, ADDRESS);
        BufferedImage image = maps.fetch(request).getImage();
        assertEquals(640, image.getWidth());
        assertEquals(400, image.getHeight());
        assertEquals(Color.MAGENTA.getRGB(), image.getRGB(0, 0));
        assertTrue(containsColor(image, 0x1455A3), "Device pin");
        assertTrue(containsColor(image, 0xAC2737), "Destination pin");
        assertTrue(containsColor(image, 0x006B68), "Confirmed station pins");
        assertTrue(containsColor(image, 0xA15B00), "Limited station pins");
        assertTrue(containsColor(image, 0x657681), "Unknown station pins");
        assertTrue(tileRequests.size() > 1 && tileRequests.size() <= 12);
        assertTrue(tileRequests.stream().allMatch(uri -> uri.getPath().matches("/tiles/\\d+/\\d+/\\d+\\.png")));
        assertTrue(tileUserAgents.stream().allMatch(agent -> agent.startsWith("AccessGraph/")));
        assertEquals("q=" + ADDRESS + "&limit=1&lat=43.65&lon=-79.38",
                URLDecoder.decode(searchQueries.element(), StandardCharsets.UTF_8));
        int tileDownloads = tileRequests.size();
        maps.fetch(request);
        assertEquals(1, searchQueries.size(), "Changing a map view must not repeat the last address search.");
        gateway().fetch(new MapRequest(rows, null, fix, ""));
        assertEquals(tileDownloads, tileRequests.size());
    }

    @Test
    void expiredTilesAreConditionallyRevalidated() throws Exception {
        expireTiles = true;
        Station station = new BundledStationRepository().load().get(0);
        MapRequest request = new MapRequest(List.of(station), station, null, "");
        BufferedImage focused = gateway().fetch(request).getImage();
        assertTrue(tileRequests.stream().allMatch(uri -> uri.getPath().startsWith("/tiles/15/")));
        assertEquals(0xAC2737, focused.getRGB(314, 200) & 0xFFFFFF);
        int count = tileRequests.size();
        gateway().fetch(request);
        assertEquals(count, conditionalRequests.size());
        assertTrue(conditionalRequests.stream().allMatch(value -> value.equals("\"test-tile\"|" + MODIFIED)));
        gateway().fetch(request);
        assertEquals(2 * count, tileRequests.size());
    }

    @Test
    void zoomFetchesTilesAtTheNewScaleAndProjectsTheFocusedPinInTheSameViewport() throws Exception {
        OpenStreetMapGateway maps = gateway();
        Station station = new BundledStationRepository().load().get(0);
        List<Station> rows = List.of(station);
        GeoPosition fix = new GeoPosition(station.getLatitude(), station.getLongitude() - 0.001, 10, Instant.now());
        searchResult = "{\"features\":[{\"geometry\":{\"type\":\"Point\",\"coordinates\":["
                + station.getLongitude() + "," + (station.getLatitude() + 0.001) + "]}}]}";
        MapImage focused = maps.fetch(new MapRequest(rows, station, fix, ADDRESS));
        MapViewport zoomedIn = focused.getViewport().zoomAt(1, 300, 200);
        tileRequests.clear();
        BufferedImage image = maps.fetch(new MapRequest(rows, station, fix, ADDRESS, zoomedIn)).getImage();
        assertFalse(tileRequests.isEmpty());
        assertTrue(tileRequests.stream().allMatch(uri -> uri.getPath().startsWith("/tiles/16/")));
        assertEquals(0xAC2737, image.getRGB(334, 200) & 0xFFFFFF);
        assertEquals(0xAC2737, image.getRGB(334, 136) & 0xFFFFFF, "Destination pin");
        assertEquals(0x1455A3, image.getRGB(287, 200) & 0xFFFFFF, "Device pin");
        assertEquals(Color.MAGENTA.getRGB(), image.getRGB(314, 200));

        MapViewport zoomedOut = zoomedIn.zoomAt(-1, 300, 200);
        tileRequests.clear();
        BufferedImage restored = maps.fetch(new MapRequest(rows, station, fix, ADDRESS, zoomedOut)).getImage();
        assertTrue(tileRequests.isEmpty(), "Returning to the original zoom should reuse cached tiles.");
        assertEquals(0xAC2737, restored.getRGB(314, 200) & 0xFFFFFF);
        assertEquals(1, searchQueries.size());
    }

    @Test
    void reportsNoMatchMalformedSearchAndTileErrors() throws Exception {
        List<Station> rows = new BundledStationRepository().load();
        MapRequest request = new MapRequest(rows, null, null, "Missing address");
        searchResult = "{\"features\":[]}";
        assertTrue(assertThrows(IOException.class, () -> gateway().fetch(request)).getMessage().contains("No destination"));
        searchResult = "{}";
        assertTrue(assertThrows(IOException.class, () -> gateway().fetch(request)).getMessage().contains("unreadable"));
        assertTrue(tileRequests.isEmpty());
        tileStatus = 503;
        assertTrue(assertThrows(IOException.class, () -> gateway().fetch(new MapRequest(rows, null, null, "")))
                .getMessage().contains("OpenStreetMap returned HTTP 503"));
    }

    private OpenStreetMapGateway gateway() {
        String endpoint = "http://127.0.0.1:" + server.getAddress().getPort();
        return new OpenStreetMapGateway(HttpClient.newHttpClient(), URI.create(endpoint + "/tiles"),
                URI.create(endpoint + "/api"), cacheDirectory);
    }

    private void serveTile(HttpExchange response) throws IOException {
        try {
            tileRequests.add(response.getRequestURI());
            tileUserAgents.add(response.getRequestHeaders().getFirst("User-Agent"));
            String etag = response.getRequestHeaders().getFirst("If-None-Match");
            if (etag != null) {
                conditionalRequests.add(etag + "|" + response.getRequestHeaders().getFirst("If-Modified-Since"));
                response.getResponseHeaders().add("Cache-Control", "public, max-age=3600");
                response.sendResponseHeaders(304, -1);
            } else {
                response.getResponseHeaders().add("Cache-Control", expireTiles ? "max-age=0" : "max-age=3600");
                response.getResponseHeaders().add("ETag", "\"test-tile\"");
                response.getResponseHeaders().add("Last-Modified", MODIFIED);
                send(response, tileStatus, png);
            }
        } finally {
            response.close();
        }
    }

    private static void send(HttpExchange response, int status, byte[] body) throws IOException {
        response.sendResponseHeaders(status, body.length);
        response.getResponseBody().write(body);
    }

    private static boolean containsColor(BufferedImage image, int rgb) {
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) & 0xFFFFFF) == rgb) { return true; }
            }
        }
        return false;
    }
}
