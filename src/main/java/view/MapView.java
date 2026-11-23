package view;

import interface_adapter.find_nearby_quests.FindNearbyQuestsController;
import interface_adapter.find_nearby_quests.FindNearbyQuestsState;
import interface_adapter.find_nearby_quests.FindNearbyQuestsState.PinState;
import interface_adapter.find_nearby_quests.FindNearbyQuestsViewModel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * The Map tab (Use Case 4). Draws OpenStreetMap tiles, a "you are here" marker,
 * and one pin per nearby quest, and shows a preview when a pin is clicked.
 *
 * <p>This view only displays the view model's state and forwards user actions to
 * the controller. It never filters quests or measures distances.
 */
public class MapView extends JPanel implements PropertyChangeListener {
    private static final Double[] RADIUS_PRESETS_KM = {0.5, 1.0, 2.0, 5.0, 10.0};
    private static final String TILE_SERVER = "https://tile.openstreetmap.org";
    private static final String USER_AGENT = "LinkedOut-CSC207/1.0";
    private static final String ATTRIBUTION = "© OpenStreetMap contributors";
    private static final File TILE_CACHE_DIR =
            new File(System.getProperty("user.home"), ".linkedout/tile-cache");

    private static final int DEFAULT_ZOOM = 4;
    private static final int CLOSEST_FIT_ZOOM = 3;
    private static final double FIT_FRACTION = 0.7;
    private static final int PIN_HIT_RADIUS_PX = 14;
    private static final int GAP = 8;
    private static final int PADDING = 12;

    private static final Color PIN_COLOUR = new Color(0xE0, 0x4F, 0x3F);
    private static final Color SELECTED_PIN_COLOUR = new Color(0x8E, 0x24, 0xAA);
    private static final Color YOU_ARE_HERE_COLOUR = new Color(0x1A, 0x73, 0xE8);
    private static final Color ERROR_COLOUR = new Color(0xB3, 0x26, 0x1E);
    private static final Color INFO_COLOUR = new Color(0x44, 0x44, 0x44);

    private final FindNearbyQuestsController controller;
    private final JXMapViewer map = new JXMapViewer();
    private final JComboBox<Double> radiusBox = new JComboBox<>(RADIUS_PRESETS_KM);
    private final JLabel messageLabel = new JLabel(" ");
    private final JButton retryButton = new JButton("Retry");

    private final JPanel previewPanel = new JPanel(new BorderLayout(GAP, GAP));
    private final JLabel previewTitle = new JLabel();
    private final JLabel previewTheme = new JLabel();
    private final JLabel previewXp = new JLabel();
    private final JLabel previewDistance = new JLabel();
    private final JButton openQuestButton = new JButton("Open quest");
    private final JButton closePreviewButton = new JButton("Close");

    private FindNearbyQuestsState state;
    private List<PinState> pins = new ArrayList<>();
    private PinState selectedPin;
    private boolean updatingFromState;
    private boolean fitPending;
    private boolean hasFitted;
    private Consumer<String> openQuestAction = questId -> {};

    /**
     * Creates the map view and registers it with the view model.
     *
     * @param viewModel the view model to observe
     * @param controller receives the radius the user picks
     */
    public MapView(FindNearbyQuestsViewModel viewModel, FindNearbyQuestsController controller) {
        this.controller = controller;
        this.state = viewModel.getState();
        viewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());
        add(buildToolbar(), BorderLayout.NORTH);
        add(buildMap(), BorderLayout.CENTER);
        add(buildPreview(), BorderLayout.SOUTH);
        selectRadiusInBox(state.getRadiusKm());
    }

    /**
     * Sets what happens when the user clicks "Open quest". Use Case 4 does not own
     * the quest page, so the app wires this to the quest page's entry point.
     *
     * @param action receives the id of the quest to open
     */
    public void setOpenQuestAction(Consumer<String> action) {
        this.openQuestAction = action;
    }

    /**
     * Returns the radius currently selected in the drop-down.
     *
     * @return the radius in kilometres
     */
    public double getSelectedRadiusKm() {
        return (Double) radiusBox.getSelectedItem();
    }

    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, GAP, GAP));
        bar.add(new JLabel("Search radius:"));
        radiusBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focus) {
                String text = BigDecimal.valueOf((Double) value).stripTrailingZeros()
                        .toPlainString() + " km";
                return super.getListCellRendererComponent(list, text, index, selected, focus);
            }
        });
        radiusBox.addActionListener(event -> {
            if (!updatingFromState) {
                controller.execute(getSelectedRadiusKm());
            }
        });
        bar.add(radiusBox);
        bar.add(Box.createHorizontalStrut(GAP));
        bar.add(messageLabel);
        retryButton.setVisible(false);
        retryButton.addActionListener(event -> controller.execute(getSelectedRadiusKm()));
        bar.add(retryButton);
        return bar;
    }

    private JXMapViewer buildMap() {
        DefaultTileFactory tileFactory =
                new DefaultTileFactory(new OSMTileFactoryInfo("OpenStreetMap", TILE_SERVER));
        tileFactory.setUserAgent(USER_AGENT);
        tileFactory.setLocalCache(new FileBasedLocalCache(TILE_CACHE_DIR, false));
        map.setTileFactory(tileFactory);
        map.setZoom(DEFAULT_ZOOM);
        map.setPreferredSize(new Dimension(800, 520));

        MouseInputListener pan = new PanMouseInputListener(map);
        map.addMouseListener(pan);
        map.addMouseMotionListener(pan);
        map.addMouseWheelListener(new ZoomMouseWheelListenerCursor(map));
        map.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                PinState hit = pinAt(event.getPoint());
                if (hit != null) {
                    showPreview(hit);
                }
            }
        });
        map.setOverlayPainter(new MarkerPainter());
        map.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                if (fitPending) {
                    fitMapToPins();
                }
            }
        });

        JLabel attribution = new JLabel(ATTRIBUTION);
        attribution.setFont(attribution.getFont().deriveFont(Font.PLAIN, 11f));
        attribution.setOpaque(true);
        attribution.setBackground(new Color(255, 255, 255, 200));
        attribution.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
        JPanel attributionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        attributionRow.setOpaque(false);
        attributionRow.add(attribution);
        map.setLayout(new BorderLayout());
        map.add(attributionRow, BorderLayout.SOUTH);
        return map;
    }

    private JPanel buildPreview() {
        previewTitle.setFont(previewTitle.getFont().deriveFont(Font.BOLD, 16f));
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.add(previewTitle);
        details.add(Box.createVerticalStrut(4));
        details.add(previewTheme);
        details.add(previewXp);
        details.add(previewDistance);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, GAP, 0));
        buttons.add(closePreviewButton);
        buttons.add(openQuestButton);
        closePreviewButton.addActionListener(event -> hidePreview());
        openQuestButton.addActionListener(event -> {
            if (selectedPin != null) {
                openQuestAction.accept(selectedPin.getQuestId());
            }
        });

        previewPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING)));
        previewPanel.add(details, BorderLayout.CENTER);
        previewPanel.add(buttons, BorderLayout.EAST);
        previewPanel.setVisible(false);
        return previewPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        state = (FindNearbyQuestsState) event.getNewValue();
        selectRadiusInBox(state.getRadiusKm());
        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(ERROR_COLOUR);
            messageLabel.setText(state.getErrorMessage());
        } else if (state.getInfoMessage() != null) {
            messageLabel.setForeground(INFO_COLOUR);
            messageLabel.setText(state.getInfoMessage());
        } else {
            messageLabel.setText(" ");
        }
        retryButton.setVisible(state.isRetryAvailable());

        final List<PinState> newPins = state.getPins();
        final boolean pinsChanged = !samePins(pins, newPins);
        pins = newPins;
        if (pinsChanged) {
            hidePreview();
        }
        if (pinsChanged || !hasFitted) {
            fitMapToPins();
        }
        map.repaint();
    }

    private void fitMapToPins() {
        if (!state.hasCentre()) {
            return;
        }
        if (map.getWidth() == 0 || map.getHeight() == 0) {
            // Not laid out yet: fitting now would use a 0x0 viewport. Fit on first resize.
            fitPending = true;
            return;
        }
        fitPending = false;
        GeoPosition centre = new GeoPosition(state.getCentreLatitude(), state.getCentreLongitude());
        // Each pin is added with its mirror image across the centre, so the fitted
        // area stays centred on "you are here" and every pin is on screen.
        Set<GeoPosition> positions = new HashSet<>();
        positions.add(centre);
        for (PinState pin : pins) {
            positions.add(new GeoPosition(pin.getLatitude(), pin.getLongitude()));
            positions.add(new GeoPosition(2 * centre.getLatitude() - pin.getLatitude(),
                    2 * centre.getLongitude() - pin.getLongitude()));
        }
        if (positions.size() == 1) {
            map.setZoom(DEFAULT_ZOOM);
        } else {
            map.zoomToBestFit(positions, FIT_FRACTION);
            if (map.getZoom() < CLOSEST_FIT_ZOOM) {
                map.setZoom(CLOSEST_FIT_ZOOM);
            }
        }
        map.setAddressLocation(centre);
        hasFitted = true;
    }

    private void selectRadiusInBox(double radiusKm) {
        updatingFromState = true;
        for (Double preset : RADIUS_PRESETS_KM) {
            if (preset == radiusKm) {
                radiusBox.setSelectedItem(preset);
            }
        }
        updatingFromState = false;
    }

    private PinState pinAt(java.awt.Point click) {
        PinState best = null;
        double bestDistance = PIN_HIT_RADIUS_PX;
        for (PinState pin : pins) {
            Point2D tip = toScreen(pin.getLatitude(), pin.getLongitude());
            // The pin's head is drawn above its tip, so measure to the head's centre.
            double distance = click.distance(tip.getX(), tip.getY() - PinShape.HEAD_OFFSET);
            if (distance <= bestDistance) {
                best = pin;
                bestDistance = distance;
            }
        }
        return best;
    }

    private void showPreview(PinState pin) {
        selectedPin = pin;
        previewTitle.setText(pin.getTitle());
        previewTheme.setText("Theme: " + pin.getTheme());
        previewXp.setText("Reward: " + pin.getXpText());
        previewDistance.setText("Distance: " + pin.getDistanceText());
        previewPanel.setVisible(true);
        revalidate();
        map.repaint();
    }

    private void hidePreview() {
        selectedPin = null;
        previewPanel.setVisible(false);
        revalidate();
        map.repaint();
    }

    private Point2D toScreen(double latitude, double longitude) {
        return map.convertGeoPositionToPoint(new GeoPosition(latitude, longitude));
    }

    private static boolean samePins(List<PinState> a, List<PinState> b) {
        if (a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).getQuestId().equals(b.get(i).getQuestId())) {
                return false;
            }
        }
        return true;
    }

    /** Sizes of the drawn pin, shared by the painter and the click test. */
    private static final class PinShape {
        static final double HEAD_RADIUS = 9;
        static final double HEAD_OFFSET = 20;
        static final double DOT_RADIUS = 3.5;
        static final double YOU_RADIUS = 7;
        static final double YOU_HALO_RADIUS = 16;
    }

    /** Draws the "you are here" marker and the quest pins on top of the tiles. */
    private final class MarkerPainter implements Painter<JXMapViewer> {
        @Override
        public void paint(Graphics2D original, JXMapViewer viewer, int width, int height) {
            Graphics2D g = (Graphics2D) original.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (state.hasCentre()) {
                drawYouAreHere(g, toScreen(state.getCentreLatitude(), state.getCentreLongitude()));
            }
            for (PinState pin : pins) {
                if (pin != selectedPin) {
                    drawPin(g, toScreen(pin.getLatitude(), pin.getLongitude()), PIN_COLOUR);
                }
            }
            if (selectedPin != null) {
                drawPin(g, toScreen(selectedPin.getLatitude(), selectedPin.getLongitude()),
                        SELECTED_PIN_COLOUR);
            }
            g.dispose();
        }

        private void drawYouAreHere(Graphics2D g, Point2D p) {
            double halo = PinShape.YOU_HALO_RADIUS;
            g.setColor(new Color(YOU_ARE_HERE_COLOUR.getRed(), YOU_ARE_HERE_COLOUR.getGreen(),
                    YOU_ARE_HERE_COLOUR.getBlue(), 60));
            g.fill(new Ellipse2D.Double(p.getX() - halo, p.getY() - halo, 2 * halo, 2 * halo));
            double r = PinShape.YOU_RADIUS;
            Ellipse2D dot = new Ellipse2D.Double(p.getX() - r, p.getY() - r, 2 * r, 2 * r);
            g.setColor(YOU_ARE_HERE_COLOUR);
            g.fill(dot);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2.5f));
            g.draw(dot);
        }

        private void drawPin(Graphics2D g, Point2D tip, Color colour) {
            double r = PinShape.HEAD_RADIUS;
            double cx = tip.getX();
            double cy = tip.getY() - PinShape.HEAD_OFFSET;
            Path2D point = new Path2D.Double();
            point.moveTo(tip.getX(), tip.getY());
            point.lineTo(cx - r * 0.8, cy + r * 0.6);
            point.lineTo(cx + r * 0.8, cy + r * 0.6);
            point.closePath();
            Area pin = new Area(new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r));
            pin.add(new Area(point));

            g.setColor(colour);
            g.fill(pin);
            g.setColor(colour.darker());
            g.setStroke(new BasicStroke(1.2f));
            g.draw(pin);
            double d = PinShape.DOT_RADIUS;
            g.setColor(Color.WHITE);
            g.fill(new Ellipse2D.Double(cx - d, cy - d, 2 * d, 2 * d));
        }
    }
}
