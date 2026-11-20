package org.accessgraph.view;

import org.accessgraph.interface_adapter.BrowserGateway;
import org.accessgraph.interface_adapter.LocationController;
import org.accessgraph.interface_adapter.TileGateway;
import org.accessgraph.use_case.station_map.LocationGateway;
import org.accessgraph.use_case.station_map.MapSession;
import org.accessgraph.use_case.station_map.MapViewport;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.time.Clock;

final class MapPanel extends JPanel {
    private static final URI MAP_CREDIT = URI.create("https://www.openstreetmap.org/copyright");
    /** After scrolling stops, ease to the nearest whole zoom level, where tiles are drawn at native sharpness. */
    private static final int SNAP_DELAY_MILLIS = 180;
    private static final double ZOOM_ANIMATION_MILLIS = 180;
    private final MapSession session;
    private final BrowserGateway browser;
    private final Clock clock;
    private final LocationController location;
    private final TileLoader tiles;
    private final MapCanvas canvas;
    private final JButton zoomIn = Ui.button("+", false);
    private final JButton zoomOut = Ui.button("−", false);
    private final JButton stopSharing = Ui.button("Stop sharing", false);
    private final JButton retry = Ui.button("Retry map", false);
    private final JLabel status = Ui.label("Location off", 13, false);
    private final JLabel mapStatus = Ui.label("", 13, false);
    private final Timer expiry = new Timer(1000, event -> expireLocation());
    private final Timer snap = new Timer(SNAP_DELAY_MILLIS, event -> snapZoom());
    private final Timer animation = new Timer(15, event -> animateZoom());
    private double animationFrom;
    private double animationTo;
    private long animationStart;
    private double anchorX = 0.5;
    private double anchorY = 0.5;
    private boolean active;

    MapPanel(MapSession session, TileGateway tileGateway, LocationGateway locations, BrowserGateway browser,
             Clock clock, Runnable signOut) {
        super(new BorderLayout());
        this.session = session;
        this.browser = browser;
        this.clock = clock;
        tiles = new TileLoader(tileGateway, SwingUtilities::invokeLater, this::tilesChanged);
        canvas = new MapCanvas(tiles, new MapCanvas.Gestures() {
            @Override public void zoom(double steps, double xFraction, double yFraction) {
                animation.stop();
                anchorX = xFraction;
                anchorY = yFraction;
                MapViewport viewport = session.getViewport();
                if (session.zoom(steps, xFraction * viewport.getWidth(), yFraction * viewport.getHeight())) {
                    refresh();
                }
                snap.restart();
            }

            @Override public void zoomStep(int direction) { step(direction); }

            @Override public void pan(double xFraction, double yFraction) {
                MapViewport viewport = session.getViewport();
                session.pan(xFraction * viewport.getWidth(), yFraction * viewport.getHeight());
                refresh();
            }
        });
        canvas.setStations(session.getStations());
        location = new LocationController(session, locations, browser, clock, SwingUtilities::invokeLater,
                this::refresh, this::showLocationStatus);

        snap.setRepeats(false);
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
        zoomOut.addActionListener(event -> step(-1));
        zoomIn.getAccessibleContext().setAccessibleName("Zoom in");
        zoomIn.setToolTipText("Zoom in");
        zoomIn.addActionListener(event -> step(1));
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
        retry.setToolTipText("Some map tiles could not load. Try again.");
        retry.addActionListener(event -> {
            canvas.reloadTiles();
            updateMapStatus();
        });
        messages.add(retry);
        footer.add(messages, BorderLayout.CENTER);

        JPanel credits = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        credits.setOpaque(false);
        if (!session.getStations().isEmpty()) {
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
        snap.stop();
        animation.stop();
        expiry.stop();
        canvas.clear();
        location.stop();
        mapStatus.setText("");
        retry.setVisible(false);
    }

    private void expireLocation() { location.expire(); }

    private void resizeViewport() {
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        if (!active || width < 1 || height < 1) { return; }
        if (session.resize(width, height)) { refresh(); }
    }

    /** Buttons and keys move one whole zoom level around the map's center. */
    private void step(int direction) {
        snap.stop();
        double base = animation.isRunning() ? animationTo : Math.round(session.getViewport().getZoom());
        animateTo(base + direction, 0.5, 0.5);
    }

    private void snapZoom() {
        animateTo(Math.round(session.getViewport().getZoom()), anchorX, anchorY);
    }

    private void animateTo(double target, double xFraction, double yFraction) {
        double level = Math.max(0, Math.min(MapViewport.MAX_ZOOM, target));
        if (!active || level == session.getViewport().getZoom()) { return; }
        animationFrom = session.getViewport().getZoom();
        animationTo = level;
        anchorX = xFraction;
        anchorY = yFraction;
        animationStart = System.nanoTime();
        animation.restart();
    }

    private void animateZoom() {
        double progress = Math.min(1, (System.nanoTime() - animationStart) / 1e6 / ZOOM_ANIMATION_MILLIS);
        double eased = 1 - Math.pow(1 - progress, 3);
        double level = progress >= 1 ? animationTo : animationFrom + (animationTo - animationFrom) * eased;
        MapViewport viewport = session.getViewport();
        if (session.zoomTo(level, anchorX * viewport.getWidth(), anchorY * viewport.getHeight())) { refresh(); }
        if (progress >= 1) { animation.stop(); }
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
        MapViewport viewport = session.getViewport();
        canvas.setPosition(session.getPosition(clock.instant()));
        canvas.setViewport(viewport);
        zoomIn.setEnabled(viewport.canZoomIn());
        zoomOut.setEnabled(viewport.canZoomOut());
        updateMapStatus();
    }

    private void tilesChanged() {
        if (!active) { return; }
        canvas.repaint();
        updateMapStatus();
    }

    private void updateMapStatus() {
        if (!active) { return; }
        boolean failed = tiles.hasFailures();
        retry.setVisible(failed);
        mapStatus.setText(failed ? "Some map tiles could not load." : tiles.isLoading() ? "Loading map…" : "");
    }
}
