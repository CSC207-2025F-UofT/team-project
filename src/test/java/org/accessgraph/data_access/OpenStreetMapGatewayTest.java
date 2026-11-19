package org.accessgraph.data_access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.accessgraph.entity.Station;
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
import java.net.http.HttpClient;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

class OpenStreetMapGatewayTest {
    private static final String MODIFIED = "Mon, 09 Nov 2026 09:00:00 GMT";
    @TempDir Path cacheDirectory;
    private HttpServer server;
    private byte[] png;
    private final ConcurrentLinkedQueue<String> tileRequests = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> tileUserAgents = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<String> conditionalRequests = new ConcurrentLinkedQueue<>();
    private volatile boolean expireTiles;
    private volatile int tileStatus = 200;

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
        server.start();
    }

    @AfterEach
    void tearDown() { server.stop(0); }

    @Test
    void rendersTilesAtViewportSizeAndZoomsAndPansTheSameStationPin() throws Exception {
        OpenStreetMapGateway maps = gateway();
        Station station = new BundledStationRepository().load().get(0);
        List<Station> stations = List.of(station);
        MapViewport viewport = at(station, 15);
        BufferedImage image = maps.fetch(new MapRequest(stations, viewport));
        assertEquals(640, image.getWidth());
        assertEquals(400, image.getHeight());
        assertEquals(Color.MAGENTA.getRGB(), image.getRGB(0, 0));
        assertEquals(0x06635B, image.getRGB(320, 200) & 0xFFFFFF);
        assertTrue(tileUserAgents.stream().allMatch(agent -> agent.startsWith("AccessGraph/")));

        MapViewport detail = viewport.zoomAt(1, 300, 200);
        tileRequests.clear();
        image = maps.fetch(new MapRequest(stations, detail));
        assertFalse(tileRequests.isEmpty());
        assertTrue(tileRequests.stream().allMatch(path -> path.startsWith("/tiles/16/")));
        assertEquals(0x06635B, image.getRGB(340, 200) & 0xFFFFFF);
        image = maps.fetch(new MapRequest(stations, detail.pan(25, -15)));
        assertEquals(0x06635B, image.getRGB(365, 185) & 0xFFFFFF);

        tileRequests.clear();
        gateway().fetch(new MapRequest(stations, detail.zoomAt(-1, 300, 200)));
        assertTrue(tileRequests.isEmpty(), "Returning to the original zoom should reuse persisted tiles.");
    }

    @Test
    void expiredTilesAreConditionallyRevalidated() throws Exception {
        expireTiles = true;
        Station station = new BundledStationRepository().load().get(0);
        MapRequest request = new MapRequest(List.of(station), at(station, 15));
        gateway().fetch(request);
        int count = tileRequests.size();
        gateway().fetch(request);
        assertEquals(count, conditionalRequests.size());
        assertTrue(conditionalRequests.stream().allMatch(value -> value.equals("\"test-tile\"|" + MODIFIED)));
        gateway().fetch(request);
        assertEquals(2 * count, tileRequests.size());
    }

    @Test
    void reportsTileErrors() throws Exception {
        tileStatus = 503;
        Station station = new BundledStationRepository().load().get(0);
        MapRequest request = new MapRequest(List.of(station), at(station, 15));
        assertTrue(assertThrows(IOException.class, () -> gateway().fetch(request))
                .getMessage().contains("OpenStreetMap returned HTTP 503"));
    }

    private static MapViewport at(Station station, int zoom) {
        return MapViewport.centered(station.getLatitude(), station.getLongitude(), zoom, 640, 400);
    }

    private OpenStreetMapGateway gateway() {
        String endpoint = "http://127.0.0.1:" + server.getAddress().getPort();
        return new OpenStreetMapGateway(HttpClient.newHttpClient(), URI.create(endpoint + "/tiles"), cacheDirectory);
    }

    private void serveTile(HttpExchange response) throws IOException {
        try {
            tileRequests.add(response.getRequestURI().getPath());
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
                response.sendResponseHeaders(tileStatus, png.length);
                response.getResponseBody().write(png);
            }
        } finally {
            response.close();
        }
    }
}
