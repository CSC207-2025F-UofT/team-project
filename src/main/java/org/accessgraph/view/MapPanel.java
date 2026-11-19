package org.accessgraph.view;

import org.accessgraph.interface_adapter.BrowserGateway;
import org.accessgraph.interface_adapter.LocationController;
import org.accessgraph.interface_adapter.MapImageGateway;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapRequest;
import org.accessgraph.use_case.station_map.MapSession;
import org.accessgraph.use_case.station_map.MapViewport;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.time.Clock;
import java.util.concurrent.ExecutionException;

final class MapPanel extends JPanel {
    private static final URI MAP_CREDIT = URI.create("https://www.openstreetmap.org/copyright");
    private static final int MAX_RENDER_WIDTH = 1280;
    private static final int MAX_RENDER_HEIGHT = 900;
    private final MapSession session;
    private final MapImageGateway maps;
    private final BrowserGateway browser;
    private final Clock clock;
    private final LocationController location;
    private final MapCanvas canvas;
    private final JButton zoomIn = Ui.button("+", false);
    private final JButton zoomOut = Ui.button("−", false);
    private final JButton stopSharing = Ui.button("Stop sharing", false);
    private final JButton retry = Ui.button("Retry map", false);
    private final JLabel status = Ui.label("Location off", 13, false);
    private final JLabel mapStatus = Ui.label("", 13, false);
    private final Timer renderDelay = new Timer(100, event -> render());
    private final Timer expiry = new Timer(1000, event -> expireLocation());
    private SwingWorker<BufferedImage, Void> worker;
    private MapViewport requested;
    private boolean active;

    MapPanel(MapSession session, MapImageGateway maps, LocationGateway locations, BrowserGateway browser,
             Clock clock, Runnable signOut) {
        super(new BorderLayout());
        this.session = session;
        this.maps = maps;
        this.browser = browser;
        this.clock = clock;
        canvas = new MapCanvas(new MapCanvas.Gestures() {
            @Override public void zoom(int steps, double xFraction, double yFraction) {
                MapPanel.this.zoom(steps, xFraction, yFraction);
            }

            @Override public void pan(double xFraction, double yFraction) {
                MapViewport viewport = session.getViewport();
                session.pan(xFraction * viewport.getWidth(), yFraction * viewport.getHeight());
                refresh();
            }
        });
        location = new LocationController(session, locations, browser, clock, SwingUtilities::invokeLater,
                this::refresh, this::showLocationStatus);

        renderDelay.setRepeats(false);
        canvas.addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent event) { resizeViewport(); }
        });
        add(toolbar(signOut), BorderLayout.NORTH);
        add(canvas, BorderLayout.CENTER);
        add(footer(), BorderLayout.SOUTH);
    }

    private JPanel toolbar(Runnable signOut) {
        JPanel header = new JPanel(new BorderLayout(10, 0));
        header.setBackground(Ui.PINE);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 14));
        JLabel brand = Ui.label("AccessGraph", 22, true);
        brand.setForeground(Color.WHITE);
        header.add(brand, BorderLayout.WEST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0));
        actions.setOpaque(false);
        zoomOut.getAccessibleContext().setAccessibleName("Zoom out");
        zoomOut.setToolTipText("Zoom out");
        zoomOut.addActionListener(event -> zoom(-1, 0.5, 0.5));
        zoomIn.getAccessibleContext().setAccessibleName("Zoom in");
        zoomIn.setToolTipText("Zoom in");
        zoomIn.addActionListener(event -> zoom(1, 0.5, 0.5));
        JButton locate = Ui.button("My location", true);
        locate.addActionListener(event -> location.locate());
        stopSharing.addActionListener(event -> location.stop());
        stopSharing.setVisible(false);
        JButton exit = Ui.button("Sign out", false);
        exit.addActionListener(event -> signOut.run());
        actions.add(zoomOut);
        actions.add(zoomIn);
        actions.add(locate);
        actions.add(stopSharing);
        actions.add(exit);
        header.add(actions, BorderLayout.CENTER);
        return header;
    }

    private JPanel footer() {
        JPanel footer = new JPanel(new BorderLayout(8, 0));
        footer.setBackground(Ui.PAPER);
        footer.setBorder(BorderFactory.createEmptyBorder(4, 15, 4, 10));

        JPanel messages = new JPanel(new FlowLayout(FlowLayout.LEFT, 9, 0));
        messages.setOpaque(false);
        status.getAccessibleContext().setAccessibleName("Location status");
        messages.add(status);
        mapStatus.setForeground(Ui.MUTED);
        messages.add(mapStatus);
        retry.setVisible(false);
        retry.setToolTipText("Map unavailable. Try again.");
        retry.addActionListener(event -> {
            requested = null;
            refresh();
        });
        messages.add(retry);
        footer.add(messages, BorderLayout.CENTER);

        JPanel credits = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        credits.setOpaque(false);
        if (!session.request().getStations().isEmpty()) {
            JLabel legend = Ui.label("●  Accessible TTC stations", 12, true);
            legend.setForeground(Ui.JADE);
            credits.add(legend);
        }
        JButton attribution = Ui.button("© OpenStreetMap contributors", false, 4, 4);
        attribution.setFont(Ui.font(12, false));
        attribution.addActionListener(event -> {
            try {
                browser.open(MAP_CREDIT);
            } catch (IOException exception) {
                mapStatus.setText("Could not open the map licence.");
            }
        });
        credits.add(attribution);
        footer.add(credits, BorderLayout.EAST);
        return footer;
    }

    void activate() {
        active = true;
        session.reset(900, 540);
        resizeViewport();
        refresh();
        expiry.start();
        location.locate();
    }

    void deactivate() {
        active = false;
        renderDelay.stop();
        expiry.stop();
        cancelWorker();
        requested = null;
        canvas.clear();
        location.stop();
    }

    private void expireLocation() { location.expire(); }

    private void resizeViewport() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (!active || width < 1 || height < 1) { return; }
        double scale = Math.min(1, Math.min(MAX_RENDER_WIDTH / (double) width, MAX_RENDER_HEIGHT / (double) height));
        if (session.resize(Math.max(1, (int) (width * scale)), Math.max(1, (int) (height * scale)))) {
            refresh();
        }
    }

    private void zoom(int steps, double xFraction, double yFraction) {
        MapViewport viewport = session.getViewport();
        if (session.zoom(steps, xFraction * viewport.getWidth(), yFraction * viewport.getHeight())) {
            refresh();
        }
    }

    private void showLocationStatus(LocationController.Status locationStatus) {
        switch (locationStatus) {
            case WAITING: status.setText("Allow location in your browser."); break;
            case SHARING: status.setText("Location on"); break;
            case DENIED: status.setText("Location permission denied."); break;
            case UNAVAILABLE: status.setText("Waiting for a fresh location."); break;
            case BROWSER_ERROR: status.setText("Could not open location permission."); break;
            default: status.setText("Location off");
        }
        stopSharing.setVisible(locationStatus == LocationController.Status.WAITING
                || locationStatus == LocationController.Status.SHARING
                || locationStatus == LocationController.Status.UNAVAILABLE);
    }

    private void refresh() {
        if (!active) { return; }
        canvas.setPosition(session.getPosition(clock.instant()));
        MapViewport viewport = session.getViewport();
        zoomIn.setEnabled(viewport.canZoomIn());
        zoomOut.setEnabled(viewport.canZoomOut());
        if (requested == viewport) { return; }
        requested = viewport;
        cancelWorker();
        mapStatus.setText("Loading map…");
        retry.setVisible(false);
        renderDelay.restart();
    }

    private void render() {
        if (!active) { return; }
        MapRequest request = session.request();
        worker = new SwingWorker<BufferedImage, Void>() {
            @Override protected BufferedImage doInBackground() throws IOException, InterruptedException {
                return maps.fetch(request);
            }

            @Override protected void done() {
                if (worker != this) { return; }
                worker = null;
                try {
                    canvas.setImage(get(), request.getViewport());
                    mapStatus.setText("");
                } catch (ExecutionException exception) {
                    mapStatus.setText("");
                    retry.setVisible(true);
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        worker.execute();
    }

    private void cancelWorker() {
        SwingWorker<BufferedImage, Void> previous = worker;
        worker = null;
        if (previous != null) { previous.cancel(true); }
    }
}
