// src/java/view/StaticMapPanel.java
package view;

import interface_adapter.browselandmarks.BrowseLandmarksState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;

public class StaticMapPanel extends JPanel {

    private static final String API_KEY = "AIzaSyCk9bPskLw7eUI-_Y9G6tW8eDAE-iXI8Ms";
    private static final int MAP_WIDTH = 800;
    private static final int MAP_HEIGHT = 800;

    private final JLabel mapLabel = new JLabel("Loading map...", SwingConstants.CENTER);

    public static class MarkerPoint {
        public String name;
        public double lat;
        public double lng;
        public int x;
        public int y;
    }

    private List<MarkerPoint> markers;

    // fixed campus view
    private final double centerLat = 43.6620;
    private final double centerLng = -79.3955;
    private final int zoom = 17;

    public interface MarkerClickListener {
        void onMarkerClicked(String name);
    }

    private MarkerClickListener listener = name -> {};

    public void setMarkerClickListener(MarkerClickListener listener) {
        this.listener = listener;
    }

    public StaticMapPanel() {
        setLayout(new BorderLayout());
        add(mapLabel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(MAP_WIDTH, MAP_HEIGHT));

        mapLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MarkerPoint m = findMarkerNear(e.getX(), e.getY());
                if (m != null) {
                    listener.onMarkerClicked(m.name);
                }
            }
        });
    }

    public void setLandmarks(List<BrowseLandmarksState.LandmarkVM> vms) {
        // convert VMs -> marker points
        markers = vms.stream().map(vm -> {
            MarkerPoint mp = new MarkerPoint();
            mp.name = vm.name;
            mp.lat = vm.latitude;
            mp.lng = vm.longitude;
            return mp;
        }).toList();

        loadMapImage(); // refresh
    }

    private void loadMapImage() {
        try {
            String url = buildStaticMapUrl();
            BufferedImage img = ImageIO.read(new URL(url));
            mapLabel.setIcon(new ImageIcon(img));
            mapLabel.setText(null);

            computeMarkerScreenPositions();
        } catch (Exception e) {
            mapLabel.setText("Failed to load map");
            e.printStackTrace();
        }
    }

    private String buildStaticMapUrl() {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?");
        sb.append("center=").append(centerLat).append(",").append(centerLng);
        sb.append("&zoom=").append(zoom);
        sb.append("&size=").append(MAP_WIDTH).append("x").append(MAP_HEIGHT);
        sb.append("&maptype=roadmap");

        if (markers != null && !markers.isEmpty()) {
            sb.append("&markers=color:blue%7Csize:mid");
            for (MarkerPoint m : markers) {
                sb.append("%7C").append(m.lat).append(",").append(m.lng);
            }
        }

        sb.append("&key=").append(API_KEY);
        return sb.toString();
    }

    private MarkerPoint findMarkerNear(int x, int y) {
        if (markers == null) return null;
        final int RADIUS = 18;
        MarkerPoint best = null;
        double bestDist2 = RADIUS * RADIUS;

        for (MarkerPoint m : markers) {
            int dx = x - m.x;
            int dy = y - m.y;
            double d2 = dx * dx + dy * dy;
            if (d2 <= bestDist2) {
                bestDist2 = d2;
                best = m;
            }
        }
        return best;
    }

    private void computeMarkerScreenPositions() {
        if (markers == null) return;
        for (MarkerPoint m : markers) {
            Point p = latLngToPoint(m.lat, m.lng, centerLat, centerLng, zoom, MAP_WIDTH, MAP_HEIGHT);
            m.x = p.x;
            m.y = p.y;
        }
    }

    // Web Mercator projection helpers
    private static Point latLngToPoint(double lat, double lng,
                                       double centerLat, double centerLng,
                                       int zoom, int width, int height) {

        PointD worldPoint = project(lat, lng);
        PointD centerWorld = project(centerLat, centerLng);

        double x = (worldPoint.x - centerWorld.x) + width / 2.0;
        double y = (worldPoint.y - centerWorld.y) + height / 2.0;

        return new Point((int) Math.round(x), (int) Math.round(y));
    }

    private static PointD project(double lat, double lng) {
        double siny = Math.sin(Math.toRadians(lat));
        siny = Math.min(Math.max(siny, -0.9999), 0.9999);

        double x = 256 * (0.5 + lng / 360.0);
        double y = 256 * (0.5 - Math.log((1 + siny) / (1 - siny)) / (4 * Math.PI));
        return new PointD(x, y);
    }

    private static class PointD {
        final double x, y;
        PointD(double x, double y) { this.x = x; this.y = y; }
    }
}
