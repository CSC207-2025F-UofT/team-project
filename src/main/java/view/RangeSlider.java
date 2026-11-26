package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A slider with two handles that picks a range of evenly spaced stops
 * (Swing has no built-in range slider). Drag either handle with the mouse,
 * or click it and use the left/right arrow keys. The left handle can never
 * pass the right one. It only knows stop positions (0, 1, 2, ...); what the
 * stops mean (°C) is up to whoever uses it.
 */
public class RangeSlider extends JComponent {
    private static final int SIDE_MARGIN = 26;
    private static final int TRACK_Y = 14;
    private static final int THUMB_RADIUS = 9;
    private static final int TICK_HEIGHT = 6;
    private static final int LABEL_TOP = 30;
    private static final int LABEL_WIDTH = 40;
    private static final int STOP_SPACING = 22;
    private static final int MIN_STOP_SPACING = 18;
    private static final int LABEL_HEIGHT = 36;
    private static final Color TRACK = new Color(0xC2, 0xC7, 0xD0);
    private static final Color SELECTED = new Color(0x2F, 0x5B, 0xD3);
    private static final Color THUMB_BORDER = new Color(0x4A, 0x4F, 0x5C);

    private final int stopCount;
    private final List<JLabel> labels = new ArrayList<>();
    private final List<ChangeListener> listeners = new ArrayList<>();
    private int low;
    private int high;
    private boolean movingHigh;

    /**
     * Makes the slider with one label per stop.
     *
     * @param stopLabels the text under each stop (HTML is allowed, for two lines)
     */
    public RangeSlider(List<String> stopLabels) {
        this.stopCount = stopLabels.size();
        this.low = 0;
        this.high = stopCount - 1;

        setLayout(null);
        for (String text : stopLabels) {
            final JLabel label = new JLabel(text, SwingConstants.CENTER);
            labels.add(label);
            add(label);
        }

        setFocusable(true);
        final MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                movingHigh = pickHighHandle(e.getX());
                moveActiveHandleTo(nearestStop(e.getX()));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                moveActiveHandleTo(nearestStop(e.getX()));
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                final int active = movingHigh ? high : low;
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    moveActiveHandleTo(active - 1);
                }
                else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    moveActiveHandleTo(active + 1);
                }
            }
        });
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                repaint();
            }
        });
    }

    public int getLow() {
        return low;
    }

    public int getHigh() {
        return high;
    }

    /**
     * Moves both handles, e.g. to reset the form.
     *
     * @param newLow left handle's stop
     * @param newHigh right handle's stop (not smaller than newLow)
     */
    public void setRange(int newLow, int newHigh) {
        low = clamp(newLow);
        high = Math.max(low, clamp(newHigh));
        repaint();
        fireChanged();
    }

    /**
     * Gets told whenever a handle moves.
     *
     * @param listener who to notify
     */
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    //moving the handles
    private void moveActiveHandleTo(int stop) {
        final int target = clamp(stop);
        if (movingHigh) {
            high = Math.max(target, low);
        }
        else {
            low = Math.min(target, high);
        }
        repaint();
        fireChanged();
    }

    /** Picks whichever handle is closer to the click; ties go by which side was clicked. */
    private boolean pickHighHandle(int x) {
        final int lowDistance = Math.abs(x - stopX(low));
        final int highDistance = Math.abs(x - stopX(high));
        if (lowDistance == highDistance) {
            return x > stopX(high);
        }
        return highDistance < lowDistance;
    }

    private int nearestStop(int x) {
        final double step = stepWidth();
        return clamp((int) Math.round((x - SIDE_MARGIN) / step));
    }

    private int clamp(int stop) {
        return Math.max(0, Math.min(stopCount - 1, stop));
    }

    private double stepWidth() {
        return (getWidth() - 2.0 * SIDE_MARGIN) / (stopCount - 1);
    }

    private int stopX(int stop) {
        return (int) Math.round(SIDE_MARGIN + stop * stepWidth());
    }

    private void fireChanged() {
        final ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(event);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIDE_MARGIN * 2 + (stopCount - 1) * STOP_SPACING, LABEL_TOP + LABEL_HEIGHT + 4);
    }

    /** Never squashed below this, so the form's layout can't collapse it to nothing. */
    @Override
    public Dimension getMinimumSize() {
        return new Dimension(SIDE_MARGIN * 2 + (stopCount - 1) * MIN_STOP_SPACING,
                getPreferredSize().height);
    }

    @Override
    public void doLayout() {
        for (int i = 0; i < labels.size(); i++) {
            labels.get(i).setBounds(stopX(i) - LABEL_WIDTH / 2, LABEL_TOP, LABEL_WIDTH, LABEL_HEIGHT);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(TRACK);
        g.drawLine(stopX(0), TRACK_Y, stopX(stopCount - 1), TRACK_Y);
        g.setColor(SELECTED);
        g.drawLine(stopX(low), TRACK_Y, stopX(high), TRACK_Y);

        g.setStroke(new BasicStroke(1));
        g.setColor(THUMB_BORDER);
        for (int i = 0; i < stopCount; i++) {
            g.drawLine(stopX(i), TRACK_Y + THUMB_RADIUS + 1, stopX(i), TRACK_Y + THUMB_RADIUS + 1 + TICK_HEIGHT);
        }

        drawThumb(g, stopX(low), hasFocus() && !movingHigh);
        drawThumb(g, stopX(high), hasFocus() && movingHigh);
        g.dispose();
    }

    private static void drawThumb(Graphics2D g, int x, boolean active) {
        final int size = THUMB_RADIUS * 2;
        g.setColor(Color.WHITE);
        g.fillOval(x - THUMB_RADIUS, TRACK_Y - THUMB_RADIUS, size, size);
        g.setStroke(new BasicStroke(active ? 2.5f : 1.5f));
        g.setColor(active ? SELECTED : THUMB_BORDER);
        g.drawOval(x - THUMB_RADIUS, TRACK_Y - THUMB_RADIUS, size, size);
    }
}
