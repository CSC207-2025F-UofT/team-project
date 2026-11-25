package view;

import interface_adapter.find_nearby_quests.FindNearbyQuestsController;
import interface_adapter.find_nearby_quests.FindNearbyQuestsState;
import interface_adapter.find_nearby_quests.FindNearbyQuestsState.PinState;
import interface_adapter.find_nearby_quests.FindNearbyQuestsViewModel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.cache.FileBasedLocalCache;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import view.journal.JournalStyle;

/**
 * The Map page (Use Case 4). Draws OpenStreetMap tiles, a "you are here" marker,
 * and one pin per nearby quest, and shows a preview card when a pin is clicked.
 *
 * <p>This view only displays the view model's state and forwards user actions to
 * the controller. It never filters quests or measures distances. It uses the same
 * page style as the Journal ({@link JournalStyle}) so the app looks like one app.
 */
public class MapView extends JPanel implements PropertyChangeListener {
    private static final double[] RADIUS_PRESETS_KM = {0.5, 1.0, 2.0, 5.0, 10.0};
    private static final String TILE_SERVER = "https://tile.openstreetmap.org";
    private static final String USER_AGENT = "LinkedOut-CSC207/1.0";
    private static final String ATTRIBUTION = "© OpenStreetMap contributors";
    private static final File TILE_CACHE_DIR =
            new File(System.getProperty("user.home"), ".linkedout/tile-cache");

    private static final int DEFAULT_ZOOM = 4;
    private static final int CLOSEST_FIT_ZOOM = 3;
    private static final double FIT_FRACTION = 0.7;
    private static final int PIN_HIT_RADIUS_PX = 14;

    // Page layout, matching the Journal pages (header ends at y = 58, strip at y = 72).
    private static final int STRIP_TOP = 72;
    private static final int STRIP_HEIGHT = 40;
    private static final int STATUS_TOP = 114;
    private static final int STATUS_HEIGHT = 26;
    private static final int MAP_TOP = 142;
    private static final int MARGIN = 10;
    private static final int CORNER = 36;
    private static final int RETRY_WIDTH = 72;

    private static final Color PIN_COLOUR = new Color(0xE0, 0x4F, 0x3F);
    private static final Color SELECTED_PIN_COLOUR = new Color(0x8E, 0x24, 0xAA);
    private static final Color YOU_ARE_HERE_COLOUR = new Color(0x1A, 0x73, 0xE8);
    private static final Color ERROR_COLOUR = new Color(0xB3, 0x26, 0x1E);

    private final FindNearbyQuestsController controller;
    private final JXMapViewer map = new JXMapViewer();
    private final Map<Double, JToggleButton> radiusButtons = new LinkedHashMap<>();
    private final JLabel messageLabel =
            JournalStyle.label(" ", JournalStyle.UI_FONT.deriveFont(12f));
    private final JButton retryButton;

    private final JPanel previewCard = new RoundedPanel(JournalStyle.CARD_GREEN);
    private final JLabel previewTitle = previewLabel(Font.BOLD, 15f);
    private final JLabel previewTheme = previewLabel(Font.PLAIN, 13f);
    private final JLabel previewXp = previewLabel(Font.PLAIN, 13f);
    private final JLabel previewDistance = previewLabel(Font.PLAIN, 13f);

    private FindNearbyQuestsState state;
    private double selectedRadiusKm;
    private List<PinState> pins = new ArrayList<>();
    private PinState selectedPin;
    private boolean fitPending;
    private boolean hasFitted;
    private Consumer<String> openQuestAction = questId -> {};
    private Runnable backAction = () -> {};

    /**
     * Creates the map page and registers it with the view model.
     *
     * @param viewModel the view model to observe
     * @param controller receives the radius the user picks
     */
    public MapView(FindNearbyQuestsViewModel viewModel, FindNearbyQuestsController controller) {
        this.controller = controller;
        this.state = viewModel.getState();
        this.selectedRadiusKm = state.getRadiusKm();
        viewModel.addPropertyChangeListener(this);

        setLayout(null);
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        JournalStyle.addPageHeader(this, "Map", () -> backAction.run());
        add(buildRadiusStrip());

        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(messageLabel);
        retryButton = JournalStyle.roundedButton("Retry", JournalStyle.PHOTO_BUTTON,
                () -> controller.execute(selectedRadiusKm));
        retryButton.setVisible(false);
        add(retryButton);

        add(buildMap());
        selectRadiusButton(selectedRadiusKm);
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
     * Sets what happens when the user clicks the back arrow in the page header.
     *
     * @param action usually returns to the home page
     */
    public void setBackAction(Runnable action) {
        this.backAction = action;
    }

    /**
     * Returns the radius currently selected in the radius strip.
     *
     * @return the radius in kilometres
     */
    public double getSelectedRadiusKm() {
        return selectedRadiusKm;
    }

    @Override
    public void doLayout() {
        super.doLayout();
        int width = getWidth();
        messageLabel.setBounds(MARGIN + 6, STATUS_TOP, width - 2 * MARGIN - RETRY_WIDTH - 12,
                STATUS_HEIGHT);
        retryButton.setBounds(width - MARGIN - RETRY_WIDTH, STATUS_TOP + 1, RETRY_WIDTH, 24);
        int mapHeight = Math.max(0, getHeight() - MAP_TOP - MARGIN);
        map.setBounds(MARGIN, MAP_TOP, width - 2 * MARGIN, mapHeight);
    }

    /** Same look as the Journal's status tabs: the yellow strip with one toggle per radius. */
    private JLabel buildRadiusStrip() {
        JLabel strip = new JLabel(
                JournalStyle.loadScaledIcon("tabs", JournalStyle.WIDTH, STRIP_HEIGHT));
        strip.setLayout(new GridLayout(1, RADIUS_PRESETS_KM.length));
        strip.setBounds(0, STRIP_TOP, JournalStyle.WIDTH, STRIP_HEIGHT);
        strip.getAccessibleContext().setAccessibleName("Search radius");
        ButtonGroup group = new ButtonGroup();
        for (double radiusKm : RADIUS_PRESETS_KM) {
            JToggleButton button = new JToggleButton(formatKm(radiusKm));
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setForeground(JournalStyle.INK);
            button.setFont(JournalStyle.UI_FONT.deriveFont(Font.PLAIN, 12f));
            button.addItemListener(change -> button.setFont(button.getFont()
                    .deriveFont(button.isSelected() ? Font.BOLD : Font.PLAIN)));
            // Only a click reruns the use case; selecting the button from the state does not.
            button.addActionListener(event -> {
                selectedRadiusKm = radiusKm;
                controller.execute(radiusKm);
            });
            group.add(button);
            strip.add(button);
            radiusButtons.put(radiusKm, button);
        }
        return strip;
    }

    private JXMapViewer buildMap() {
        DefaultTileFactory tileFactory =
                new DefaultTileFactory(new OSMTileFactoryInfo("OpenStreetMap", TILE_SERVER));
        tileFactory.setUserAgent(USER_AGENT);
        tileFactory.setLocalCache(new FileBasedLocalCache(TILE_CACHE_DIR, false));
        map.setTileFactory(tileFactory);
        map.setZoom(DEFAULT_ZOOM);

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
        attribution.setFont(JournalStyle.UI_FONT.deriveFont(10f));
        attribution.setForeground(JournalStyle.INK);
        attribution.setOpaque(true);
        attribution.setBackground(new Color(255, 255, 255, 200));
        attribution.setBorder(BorderFactory.createEmptyBorder(1, 6, 1, 6));
        JPanel attributionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        attributionRow.setOpaque(false);
        // Keep the label clear of the map's rounded bottom-right corner.
        attributionRow.setBorder(BorderFactory.createEmptyBorder(4, 0, 8, CORNER / 2));
        attributionRow.add(attribution);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.add(buildPreviewCard());
        bottom.add(attributionRow);

        map.setLayout(new BorderLayout());
        map.add(bottom, BorderLayout.SOUTH);
        return map;
    }

    /** Same look as a Journal quest card: green, rounded, with white pill buttons. */
    private JPanel buildPreviewCard() {
        JPanel details = new JPanel();
        details.setOpaque(false);
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.add(previewTheme);
        details.add(previewXp);
        details.add(previewDistance);

        JButton closeButton =
                JournalStyle.roundedButton("Close", JournalStyle.PAPER, this::hidePreview);
        JButton openButton = JournalStyle.roundedButton("Open quest", JournalStyle.PAPER, () -> {
            if (selectedPin != null) {
                openQuestAction.accept(selectedPin.getQuestId());
            }
        });
        JPanel buttons = new JPanel(new GridLayout(2, 1, 0, 8));
        buttons.setOpaque(false);
        buttons.add(openButton);
        buttons.add(closeButton);
        JPanel buttonColumn = new JPanel(new BorderLayout());
        buttonColumn.setOpaque(false);
        buttonColumn.add(buttons, BorderLayout.SOUTH);
        buttonColumn.setPreferredSize(new Dimension(96, 0));

        // The title gets the card's full width, so long names end in "..." instead of being cut.
        previewTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        previewCard.setLayout(new BorderLayout(12, 0));
        previewCard.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 14));
        previewCard.add(previewTitle, BorderLayout.NORTH);
        previewCard.add(details, BorderLayout.CENTER);
        previewCard.add(buttonColumn, BorderLayout.EAST);

        JPanel inset = new JPanel(new BorderLayout());
        inset.setOpaque(false);
        inset.setBorder(BorderFactory.createEmptyBorder(0, MARGIN, 0, MARGIN));
        inset.add(previewCard);
        previewCard.setVisible(false);
        return inset;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        state = (FindNearbyQuestsState) event.getNewValue();
        selectRadiusButton(state.getRadiusKm());
        if (state.getErrorMessage() != null) {
            messageLabel.setForeground(ERROR_COLOUR);
            messageLabel.setText(state.getErrorMessage());
        } else if (state.getInfoMessage() != null) {
            messageLabel.setForeground(JournalStyle.INK);
            messageLabel.setText(state.getInfoMessage());
        } else {
            messageLabel.setText(" ");
        }
        String message = messageLabel.getText();
        messageLabel.setToolTipText(message.isBlank() ? null : message);
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

    /** Shows the state's radius as selected without rerunning the use case. */
    private void selectRadiusButton(double radiusKm) {
        JToggleButton button = radiusButtons.get(radiusKm);
        if (button != null) {
            selectedRadiusKm = radiusKm;
            button.setSelected(true);
        }
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
        previewTitle.setToolTipText(pin.getTitle());
        previewTheme.setText("Theme: " + pin.getTheme());
        previewXp.setText("Reward: " + pin.getXpText());
        previewDistance.setText("Distance: " + pin.getDistanceText());
        previewCard.setVisible(true);
        map.revalidate();
        map.repaint();
    }

    private void hidePreview() {
        selectedPin = null;
        previewCard.setVisible(false);
        map.revalidate();
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

    private static String formatKm(double radiusKm) {
        return BigDecimal.valueOf(radiusKm).stripTrailingZeros().toPlainString() + " km";
    }

    private static JLabel previewLabel(int style, float size) {
        JLabel label = JournalStyle.label(" ", JournalStyle.UI_FONT.deriveFont(style, size));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    /** A panel filled with a rounded rectangle, like the Journal's cards. */
    private static final class RoundedPanel extends JPanel {
        private final Color fill;

        RoundedPanel(Color fill) {
            this.fill = fill;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            JournalStyle.paintRounded(graphics, fill, getWidth(), getHeight(), CORNER);
        }
    }

    /** Sizes of the drawn pin, shared by the painter and the click test. */
    private static final class PinShape {
        static final double HEAD_RADIUS = 9;
        static final double HEAD_OFFSET = 20;
        static final double DOT_RADIUS = 3.5;
        static final double YOU_RADIUS = 7;
        static final double YOU_HALO_RADIUS = 16;
    }

    /**
     * Draws the "you are here" marker and the quest pins on top of the tiles, then
     * paints the page colour outside a rounded rectangle so the map has the same
     * rounded corners as the Journal's cards.
     */
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
            roundCorners(g, width, height);
            g.dispose();
        }

        private void roundCorners(Graphics2D g, int width, int height) {
            Area outside = new Area(new Rectangle2D.Double(0, 0, width, height));
            outside.subtract(new Area(
                    new RoundRectangle2D.Double(0, 0, width, height, CORNER, CORNER)));
            g.setColor(JournalStyle.PAPER);
            g.fill(outside);
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
