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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.basic.BasicComboBoxUI;
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
 * a red circle showing the search radius, and one pin per nearby quest, and shows
 * a preview card when a pin is clicked. The yellow bar holds the filters (for now
 * only the radius drop-down), so later filters can be added next to it.
 *
 * <p>This view only displays the view model's state and forwards user actions to
 * the controller. It never filters quests or measures distances. It uses the same
 * page style as the Journal ({@link JournalStyle}) so the app looks like one app.
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
    private static final double FIT_FRACTION = 0.8;
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
    private static final int FILTER_GAP = 8;
    private static final int DROPDOWN_WIDTH = 92;
    private static final int DROPDOWN_HEIGHT = 26;

    private static final Color PIN_COLOUR = new Color(0xE0, 0x4F, 0x3F);
    private static final Color SELECTED_PIN_COLOUR = new Color(0x8E, 0x24, 0xAA);
    private static final Color YOU_ARE_HERE_COLOUR = new Color(0x1A, 0x73, 0xE8);
    private static final Color RADIUS_FILL = new Color(0xE0, 0x4F, 0x3F, 28);
    private static final Color RADIUS_EDGE = new Color(0xE0, 0x4F, 0x3F, 200);
    private static final Color ERROR_COLOUR = new Color(0xB3, 0x26, 0x1E);

    private final FindNearbyQuestsController controller;
    private final JXMapViewer map = new JXMapViewer();
    private final JComboBox<Double> radiusBox = new JComboBox<>(RADIUS_PRESETS_KM);
    private final JLabel messageLabel =
            JournalStyle.label(" ", JournalStyle.UI_FONT.deriveFont(12f));
    private final JButton retryButton;

    private final JPanel previewCard = new RoundedPanel(JournalStyle.CARD_GREEN);
    private final JLabel previewTitle = previewLabel(Font.BOLD, 15f);
    private final JLabel previewTheme = previewLabel(Font.PLAIN, 13f);
    private final JLabel previewXp = previewLabel(Font.PLAIN, 13f);
    private final JLabel previewDistance = previewLabel(Font.PLAIN, 13f);

    private FindNearbyQuestsState state;
    private List<PinState> pins = new ArrayList<>();
    private PinState selectedPin;
    private boolean updatingFromState;
    private boolean fitPending;
    private double fittedRadiusKm = -1;
    private Consumer<String> openQuestAction = questId -> {};

    /**
     * Creates the map page and registers it with the view model.
     *
     * @param viewModel the view model to observe
     * @param controller receives the radius the user picks
     */
    public MapView(FindNearbyQuestsViewModel viewModel, FindNearbyQuestsController controller) {
        this.controller = controller;
        this.state = viewModel.getState();
        viewModel.addPropertyChangeListener(this);

        setLayout(null);
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        add(buildTitle());
        add(buildFilterBar());

        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(messageLabel);
        retryButton = JournalStyle.roundedButton("Retry", JournalStyle.PHOTO_BUTTON,
                () -> controller.execute(getSelectedRadiusKm()));
        retryButton.setVisible(false);
        add(retryButton);

        add(buildMap());
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

    /**
     * The page title, placed like the Journal's header. There is no back arrow yet
     * because the app has no home page to go back to.
     */
    private JLabel buildTitle() {
        JLabel title = JournalStyle.label("Map", JournalStyle.UI_FONT.deriveFont(Font.BOLD, 26f));
        title.setBounds(65, 22, JournalStyle.WIDTH - 130, 36);
        return title;
    }

    /**
     * The yellow bar from the Journal, used here as the filter bar. It holds the
     * radius drop-down; later filters (for example theme) go next to it.
     */
    private JLabel buildFilterBar() {
        JLabel bar = new JLabel(
                JournalStyle.loadScaledIcon("tabs", JournalStyle.WIDTH, STRIP_HEIGHT));
        int verticalGap = (STRIP_HEIGHT - DROPDOWN_HEIGHT) / 2;
        bar.setLayout(new FlowLayout(FlowLayout.LEFT, FILTER_GAP, verticalGap));
        bar.setBounds(0, STRIP_TOP, JournalStyle.WIDTH, STRIP_HEIGHT);

        JLabel radiusLabel = JournalStyle.label("Radius", JournalStyle.UI_FONT.deriveFont(12f));
        radiusLabel.setBorder(BorderFactory.createEmptyBorder(0, MARGIN, 0, 0));
        radiusLabel.setLabelFor(radiusBox);
        bar.add(radiusLabel);
        bar.add(buildRadiusDropdown());
        return bar;
    }

    /** A drop-down drawn as a white pill, like the Journal's buttons. */
    private JPanel buildRadiusDropdown() {
        radiusBox.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton(new ChevronIcon());
                arrow.setBorderPainted(false);
                arrow.setContentAreaFilled(false);
                arrow.setFocusPainted(false);
                return arrow;
            }

            @Override
            public void paintCurrentValueBackground(Graphics graphics, Rectangle bounds,
                                                    boolean hasFocus) {
                // The pill behind the drop-down is the background.
            }
        });
        radiusBox.setOpaque(false);
        radiusBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 4));
        radiusBox.setFont(JournalStyle.UI_FONT.deriveFont(12f));
        radiusBox.setForeground(JournalStyle.INK);
        radiusBox.getAccessibleContext().setAccessibleName("Search radius");
        radiusBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean selected, boolean focus) {
                super.getListCellRendererComponent(
                        list, formatKm((Double) value), index, selected, focus);
                setFont(JournalStyle.UI_FONT.deriveFont(selected ? Font.BOLD : Font.PLAIN, 12f));
                setForeground(JournalStyle.INK);
                setBackground(selected && index >= 0 ? JournalStyle.GREEN : JournalStyle.PAPER);
                setOpaque(index >= 0);
                return this;
            }
        });
        // Only a user's choice reruns the use case; showing the state's radius does not.
        radiusBox.addActionListener(event -> {
            if (!updatingFromState) {
                controller.execute(getSelectedRadiusKm());
            }
        });

        JPanel pill = new RoundedPanel(JournalStyle.PAPER);
        pill.setLayout(new BorderLayout());
        pill.setPreferredSize(new Dimension(DROPDOWN_WIDTH, DROPDOWN_HEIGHT));
        pill.add(radiusBox);
        return pill;
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
                    fitMapToCircle();
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
        selectRadiusInBox(state.getRadiusKm());
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
        if (pinsChanged || state.getRadiusKm() != fittedRadiusKm) {
            fitMapToCircle();
        }
        map.repaint();
    }

    /** Zooms so the whole search circle is on screen, centred on "you are here". */
    private void fitMapToCircle() {
        if (!state.hasCentre()) {
            return;
        }
        if (map.getWidth() == 0 || map.getHeight() == 0) {
            // Not laid out yet: fitting now would use a 0x0 viewport. Fit on first resize.
            fitPending = true;
            return;
        }
        fitPending = false;
        double lat = state.getCentreLatitude();
        double lon = state.getCentreLongitude();
        if (state.hasCircle()) {
            // The circle's four edge points; south and west mirror north and east.
            double north = state.getCircleNorthLatitude();
            double east = state.getCircleEastLongitude();
            Set<GeoPosition> edges = new HashSet<>(List.of(
                    new GeoPosition(north, lon), new GeoPosition(2 * lat - north, lon),
                    new GeoPosition(lat, east), new GeoPosition(lat, 2 * lon - east)));
            map.zoomToBestFit(edges, FIT_FRACTION);
            if (map.getZoom() < CLOSEST_FIT_ZOOM) {
                map.setZoom(CLOSEST_FIT_ZOOM);
            }
        } else {
            map.setZoom(DEFAULT_ZOOM);
        }
        map.setAddressLocation(new GeoPosition(lat, lon));
        fittedRadiusKm = state.getRadiusKm();
    }

    /** Shows the state's radius in the drop-down without rerunning the use case. */
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
            int arc = Math.min(CORNER, getHeight());
            JournalStyle.paintRounded(graphics, fill, getWidth(), getHeight(), arc);
        }
    }

    /** A small downward chevron for the drop-down. */
    private static final class ChevronIcon implements Icon {
        private static final int SIZE = 10;

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(JournalStyle.INK);
            g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            Path2D chevron = new Path2D.Double();
            chevron.moveTo(x + 1, y + 3);
            chevron.lineTo(x + SIZE / 2.0, y + SIZE - 3);
            chevron.lineTo(x + SIZE - 1, y + 3);
            g.draw(chevron);
            g.dispose();
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
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
            if (state.hasCentre() && state.hasCircle()) {
                drawRadiusCircle(g);
            }
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

        /**
         * Draws the search circle. The edge points come from the use case, so the
         * view only converts positions to pixels and never measures distances.
         */
        private void drawRadiusCircle(Graphics2D g) {
            Point2D centre = toScreen(state.getCentreLatitude(), state.getCentreLongitude());
            Point2D north = toScreen(state.getCircleNorthLatitude(), state.getCentreLongitude());
            Point2D east = toScreen(state.getCentreLatitude(), state.getCircleEastLongitude());
            double radiusY = Math.abs(centre.getY() - north.getY());
            double radiusX = Math.abs(east.getX() - centre.getX());
            Ellipse2D circle = new Ellipse2D.Double(centre.getX() - radiusX,
                    centre.getY() - radiusY, 2 * radiusX, 2 * radiusY);
            g.setColor(RADIUS_FILL);
            g.fill(circle);
            g.setColor(RADIUS_EDGE);
            g.setStroke(new BasicStroke(2f));
            g.draw(circle);
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
