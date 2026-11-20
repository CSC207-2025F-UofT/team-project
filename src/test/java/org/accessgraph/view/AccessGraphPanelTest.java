package org.accessgraph.view;

import org.accessgraph.data_access.DemoAccount;
import org.accessgraph.entity.GeoPosition;
import org.accessgraph.use_case.sign_in.SignIn;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapSession;
import org.accessgraph.use_case.station_map.MapViewport;
import org.junit.jupiter.api.Test;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class AccessGraphPanelTest {
    private static final Instant NOW = Instant.parse("2026-11-19T16:00:00Z");
    private static final Color TILE = new Color(0xA9B6A7);
    private static final GeoPosition UNION = new GeoPosition(43.6455, -79.3797, 17, NOW);

    @Test
    void signInGatesMapAndLocationAndFreshFixOpensTheNeighborhoodView() throws Exception {
        Fixture demo = new Fixture();
        try {
            SwingUtilities.invokeAndWait(() -> {
                assertTrue(demo.login().isVisible());
                assertFalse(demo.map().isVisible());
                assertTrue(find(demo.panel, JTabbedPane.class).isEmpty());
                assertEquals(0, demo.locations.started);
                demo.signIn("wrong");
                assertTrue(demo.login().isVisible());
                assertEquals(0, demo.locations.started);
                assertTrue(find(demo.login(), JLabel.class).stream()
                        .anyMatch(label -> "Check your username and password.".equals(label.getText())));
                demo.signIn(DemoAccount.PASSWORD);
                assertFalse(demo.login().isVisible());
                assertTrue(demo.map().isVisible());
                assertEquals(1, demo.locations.started);
                assertEquals(FakeLocations.PAGE, demo.opened.get());
            });
            demo.awaitZoom(14);
            demo.awaitPaint(TILE, 20, 20);

            demo.locations.listener.onPosition(new GeoPosition(50, -79, 12, NOW.minusSeconds(90)));
            SwingUtilities.invokeAndWait(() -> assertNull(demo.session.getPosition(NOW)));
            demo.locations.listener.onPosition(UNION);
            MapViewport centered = demo.awaitZoom(16);
            MapViewport.Pixel pin = centered.screenPoint(UNION.getLatitude(), UNION.getLongitude());
            assertEquals(centered.getWidth() / 2.0, pin.x, 0.5);
            assertEquals(centered.getHeight() / 2.0, pin.y, 0.5);
            demo.awaitPaint(Color.decode("#246BD5"), -1, -1);
            demo.awaitPaint(TILE, 20, 20);

            // Zooming out while the next level downloads keeps drawing the tiles already in memory.
            demo.gate = new CountDownLatch(1);
            SwingUtilities.invokeAndWait(() -> button(demo.map(), "−").doClick());
            demo.awaitZoom(15);
            demo.awaitPaint(TILE, 20, 20);
            demo.gate.countDown();
            demo.awaitRequest(15);

            // Scrolling responds immediately, then settles on the nearest whole (sharp) zoom level.
            AtomicReference<MapViewport> scrolled = new AtomicReference<>();
            SwingUtilities.invokeAndWait(() -> {
                MapViewport before = demo.session.getViewport();
                scroll(demo.canvas(), 1000, 1);
                scroll(demo.canvas(), 1000, 8);
                MapViewport after = demo.session.getViewport();
                assertEquals(14.875, after.getZoom());
                MapViewport.Pixel previousPin = before.screenPoint(UNION.getLatitude(), UNION.getLongitude());
                MapViewport.Pixel nextPin = after.screenPoint(UNION.getLatitude(), UNION.getLongitude());
                double scale = Math.pow(2, -0.125);
                assertEquals(200 + (previousPin.x - 200) * scale, nextPin.x, 0.001);
                assertEquals(200 + (previousPin.y - 200) * scale, nextPin.y, 0.001);
            });
            MapViewport snapped = demo.awaitZoom(15);
            SwingUtilities.invokeAndWait(() -> {
                MapCanvas canvas = demo.canvas();
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, 200, 200, 1,
                        false, MouseEvent.BUTTON1));
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, 250, 230, 0,
                        false, MouseEvent.NOBUTTON));
                MapViewport panned = demo.session.getViewport();
                assertEquals(snapped.getLeft() - 50, panned.getLeft(), 1e-6, "The map follows the pointer live.");
                assertEquals(snapped.getTop() - 30, panned.getTop(), 1e-6);
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0, 250, 230, 1,
                        false, MouseEvent.BUTTON1));
                scrolled.set(panned);
            });
            assertEquals(scrolled.get().getLeft(), demo.session.getViewport().getLeft());
            SwingUtilities.invokeAndWait(() -> button(demo.map(), "My location").doClick());
            MapViewport recentered = demo.awaitZoom(16);
            assertEquals(centered.getLeft(), recentered.getLeft());
            assertEquals(centered.getTop(), recentered.getTop());

            SwingUtilities.invokeAndWait(() -> {
                button(demo.map(), "Stop sharing").doClick();
                assertNull(demo.session.getPosition(NOW));
                button(demo.map(), "Sign out").doClick();
                assertTrue(demo.login().isVisible());
                assertFalse(demo.map().isVisible());
                assertEquals(1, demo.locations.stopped);
            });
            demo.locations.listener.onPosition(UNION);
            SwingUtilities.invokeAndWait(() -> assertNull(demo.session.getPosition(NOW)));
        } finally {
            demo.close();
        }
    }

    @Test
    void denyingPermissionKeepsTheTorontoMapAndLocationCanBeRetried() throws Exception {
        Fixture demo = new Fixture();
        try {
            SwingUtilities.invokeAndWait(() -> demo.signIn(DemoAccount.PASSWORD));
            demo.awaitZoom(14);
            demo.awaitPaint(TILE, -1, -1);
            demo.locations.listener.onFailure(LocationGateway.Failure.PERMISSION_DENIED);
            SwingUtilities.invokeAndWait(() -> {
                assertTrue(demo.map().isVisible());
                assertEquals(14, demo.session.getViewport().getZoom());
                assertTrue(find(demo.map(), JLabel.class).stream()
                        .anyMatch(label -> "Location permission denied.".equals(label.getText())));
                button(demo.map(), "My location").doClick();
                assertEquals(2, demo.locations.started);
            });
        } finally {
            demo.close();
        }
    }

    private static void scroll(MapCanvas canvas, long when, double rotation) {
        MouseWheelEvent wheel = new MouseWheelEvent(canvas, MouseWheelEvent.MOUSE_WHEEL, when,
                0, 200, 200, 200, 200, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, (int) rotation, rotation);
        canvas.dispatchEvent(wheel);
        assertTrue(wheel.isConsumed());
    }

    private static JButton button(Container root, String label) {
        return find(root, JButton.class).stream().filter(item -> label.equals(item.getText())).findFirst().orElseThrow();
    }

    private static <T> List<T> find(Container root, Class<T> type) {
        List<T> results = new ArrayList<>();
        for (Component child : root.getComponents()) {
            if (type.isInstance(child)) { results.add(type.cast(child)); }
            if (child instanceof Container) { results.addAll(find((Container) child, type)); }
        }
        return results;
    }

    private static void layout(Container container) {
        container.doLayout();
        for (Component child : container.getComponents()) {
            if (child instanceof Container) { layout((Container) child); }
        }
    }

    private static final class Fixture {
        private final FakeLocations locations = new FakeLocations();
        private final AtomicReference<URI> opened = new AtomicReference<>();
        private final ConcurrentLinkedQueue<Integer> requestedZooms = new ConcurrentLinkedQueue<>();
        private final AtomicReference<Boolean> loadedOnUiThread = new AtomicReference<>(false);
        private final MapSession session = new MapSession(List.of());
        private volatile CountDownLatch gate = new CountDownLatch(0);
        private AccessGraphPanel panel;

        Fixture() throws Exception {
            BufferedImage tile = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = tile.createGraphics();
            graphics.setColor(TILE);
            graphics.fillRect(0, 0, 256, 256);
            graphics.dispose();
            SwingUtilities.invokeAndWait(() -> {
                panel = new AccessGraphPanel(new SignIn(new DemoAccount()), DemoAccount.USERNAME, DemoAccount.PASSWORD,
                        session, (zoom, x, y) -> {
                            if (SwingUtilities.isEventDispatchThread()) { loadedOnUiThread.set(true); }
                            requestedZooms.add(zoom);
                            gate.await();
                            return tile;
                        }, locations, opened::set, Clock.fixed(NOW, ZoneOffset.UTC));
                panel.setSize(740, 520);
                layout(panel);
            });
        }

        LoginPanel login() { return (LoginPanel) panel.getComponent(0); }
        MapPanel map() { return (MapPanel) panel.getComponent(1); }
        MapCanvas canvas() { return find(map(), MapCanvas.class).get(0); }

        void signIn(String password) {
            find(login(), JTextField.class).get(0).setText(DemoAccount.USERNAME);
            find(login(), JPasswordField.class).get(0).setText(password);
            button(login(), "Sign in").doClick();
            layout(panel);
        }

        MapViewport awaitZoom(double zoom) throws Exception {
            AtomicReference<MapViewport> viewport = new AtomicReference<>();
            await(() -> {
                viewport.set(session.getViewport());
                return viewport.get().getZoom() == zoom;
            }, "Expected zoom " + zoom);
            return viewport.get();
        }

        void awaitRequest(int zoom) throws Exception {
            await(() -> requestedZooms.contains(zoom), "Expected a zoom " + zoom + " tile request");
        }

        /** Paints the canvas and checks one pixel; negative coordinates mean the canvas center. */
        void awaitPaint(Color color, int x, int y) throws Exception {
            AtomicReference<Color> pixel = new AtomicReference<>();
            await(() -> {
                BufferedImage image = new BufferedImage(canvas().getWidth(), canvas().getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics = image.createGraphics();
                canvas().paint(graphics);
                graphics.dispose();
                pixel.set(new Color(image.getRGB(x < 0 ? image.getWidth() / 2 : x,
                        y < 0 ? image.getHeight() / 2 : y)));
                return color.equals(pixel.get());
            }, "Expected " + color + " but painted " + pixel.get());
            assertFalse(loadedOnUiThread.get(), "Tiles must load off the Swing thread.");
        }

        private static void await(Supplier<Boolean> condition, String message) throws Exception {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
            AtomicReference<Boolean> met = new AtomicReference<>(false);
            do {
                SwingUtilities.invokeAndWait(() -> met.set(condition.get()));
                if (met.get()) { return; }
                Thread.sleep(15);
            } while (System.nanoTime() < deadline);
            fail(message);
        }

        void close() throws Exception {
            gate.countDown();
            SwingUtilities.invokeAndWait(panel::close);
        }
    }

    private static final class FakeLocations implements LocationGateway {
        private static final URI PAGE = URI.create("http://127.0.0.1:8080/#demo");
        private Listener listener;
        private int started;
        private int stopped;

        @Override public URI start(Listener listener) {
            this.listener = listener;
            started++;
            return PAGE;
        }

        @Override public void stop() { stopped++; }
    }
}
