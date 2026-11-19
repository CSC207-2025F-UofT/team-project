package org.accessgraph.view;

import org.accessgraph.data_access.DemoAccount;
import org.accessgraph.entity.GeoPosition;
import org.accessgraph.use_case.sign_in.SignIn;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapRequest;
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
import java.util.concurrent.LinkedBlockingQueue;
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
            assertEquals(14, demo.nextViewport().getZoom());

            demo.locations.listener.onPosition(new GeoPosition(50, -79, 12, NOW.minusSeconds(90)));
            SwingUtilities.invokeAndWait(() -> assertNull(demo.session.getPosition(NOW)));
            demo.locations.listener.onPosition(UNION);
            MapViewport centered = demo.nextViewport();
            assertEquals(16, centered.getZoom());
            MapViewport.Pixel pin = centered.screenPoint(UNION.getLatitude(), UNION.getLongitude());
            assertEquals(centered.getWidth() / 2.0, pin.x, 0.5);
            assertEquals(centered.getHeight() / 2.0, pin.y, 0.5);
            demo.awaitPaint(Color.decode("#246BD5"));

            SwingUtilities.invokeAndWait(() -> {
                JButton minus = button(demo.map(), "−");
                minus.doClick();
            });
            assertEquals(15, demo.nextViewport().getZoom());
            SwingUtilities.invokeAndWait(() -> {
                MouseWheelEvent wheel = new MouseWheelEvent(demo.canvas(), MouseWheelEvent.MOUSE_WHEEL, 0,
                        0, 200, 200, 200, 200, 0, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -1, -1);
                demo.canvas().dispatchEvent(wheel);
                assertTrue(wheel.isConsumed());
            });
            MapViewport zoomed = demo.nextViewport();
            assertEquals(16, zoomed.getZoom());
            SwingUtilities.invokeAndWait(() -> {
                MapCanvas canvas = demo.canvas();
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_PRESSED, 0, 0, 200, 200, 1,
                        false, MouseEvent.BUTTON1));
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_DRAGGED, 0, 0, 250, 230, 0,
                        false, MouseEvent.NOBUTTON));
                canvas.dispatchEvent(new MouseEvent(canvas, MouseEvent.MOUSE_RELEASED, 0, 0, 250, 230, 1,
                        false, MouseEvent.BUTTON1));
            });
            MapViewport panned = demo.nextViewport();
            assertEquals(zoomed.getLeft() - 50, panned.getLeft());
            assertEquals(zoomed.getTop() - 30, panned.getTop());
            SwingUtilities.invokeAndWait(() -> button(demo.map(), "My location").doClick());
            MapViewport recentered = demo.nextViewport();
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
            assertEquals(14, demo.nextViewport().getZoom());
            demo.awaitPaint(TILE);
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
        private final LinkedBlockingQueue<MapRequest> requests = new LinkedBlockingQueue<>();
        private final MapSession session = new MapSession(List.of());
        private AccessGraphPanel panel;

        Fixture() throws Exception {
            SwingUtilities.invokeAndWait(() -> {
                panel = new AccessGraphPanel(new SignIn(new DemoAccount()), DemoAccount.USERNAME, DemoAccount.PASSWORD,
                        session, request -> {
                            assertFalse(SwingUtilities.isEventDispatchThread());
                            MapViewport viewport = request.getViewport();
                            BufferedImage image = new BufferedImage(viewport.getWidth(), viewport.getHeight(),
                                    BufferedImage.TYPE_INT_RGB);
                            Graphics2D graphics = image.createGraphics();
                            graphics.setColor(TILE);
                            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
                            graphics.dispose();
                            requests.add(request);
                            return image;
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

        MapViewport nextViewport() throws InterruptedException {
            MapRequest request = requests.poll(3, TimeUnit.SECONDS);
            assertNotNull(request, "Expected the map to render.");
            return request.getViewport();
        }

        void awaitPaint(Color color) throws Exception {
            long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(3);
            AtomicReference<Color> pixel = new AtomicReference<>();
            do {
                SwingUtilities.invokeAndWait(() -> {
                    BufferedImage image = new BufferedImage(canvas().getWidth(), canvas().getHeight(),
                            BufferedImage.TYPE_INT_RGB);
                    Graphics2D graphics = image.createGraphics();
                    canvas().paint(graphics);
                    graphics.dispose();
                    pixel.set(new Color(image.getRGB(image.getWidth() / 2, image.getHeight() / 2)));
                });
                if (color.equals(pixel.get())) { return; }
                Thread.sleep(20);
            } while (System.nanoTime() < deadline);
            assertEquals(color, pixel.get());
        }

        void close() throws Exception { SwingUtilities.invokeAndWait(panel::close); }
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
