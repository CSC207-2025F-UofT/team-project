package org.accessgraph.data_access;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
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
    private volatile boolean outage;

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
    void downloadsIdentifiedTilesAndReusesThePersistedCopy() throws Exception {
        BufferedImage image = gateway().tile(15, 9156, 11966);
        assertEquals(256, image.getWidth());
        assertEquals(256, image.getHeight());
        assertEquals(Color.MAGENTA.getRGB(), image.getRGB(0, 0));
        assertEquals(List.of("/tiles/15/9156/11966.png"), List.copyOf(tileRequests));
        assertTrue(tileUserAgents.stream().allMatch(agent -> agent.startsWith("AccessGraph/")));

        tileRequests.clear();
        assertEquals(Color.MAGENTA.getRGB(), gateway().tile(15, 9156, 11966).getRGB(10, 10));
        assertTrue(tileRequests.isEmpty(), "A fresh tile should come from the disk cache.");
        assertThrows(IllegalArgumentException.class, () -> gateway().tile(2, 4, 0));
    }

    @Test
    void expiredTilesAreConditionallyRevalidated() throws Exception {
        expireTiles = true;
        gateway().tile(15, 9156, 11966);
        gateway().tile(15, 9156, 11966);
        assertEquals(2, tileRequests.size());
        assertEquals(List.of("\"test-tile\"|" + MODIFIED), List.copyOf(conditionalRequests));
    }

    @Test
    void reportsTileErrors() {
        tileStatus = 503;
        assertTrue(assertThrows(IOException.class, () -> gateway().tile(15, 9156, 11966))
                .getMessage().contains("OpenStreetMap returned HTTP 503"));
    }

    @Test
    void showsTheExpiredCopyWhenTheServerIsUnavailable() throws Exception {
        expireTiles = true;
        gateway().tile(15, 9156, 11966);
        outage = true;
        assertEquals(Color.MAGENTA.getRGB(), gateway().tile(15, 9156, 11966).getRGB(0, 0));
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
            if (outage) {
                response.sendResponseHeaders(503, -1);
            } else if (etag != null) {
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
