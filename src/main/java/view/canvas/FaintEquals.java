package view.canvas;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

// faint "=" chip at a formula's end: tap = popup, a drag goes to the canvas like it wasn't there
final class FaintEquals extends JComponent {
    private static final double SIZE = 0.7;   // x char height
    private static final double GAP = 0.6;    // from the ink, x char height
    private static final int MIN_SIZE = 18;
    private static final int MAX_SIZE = 32;
    private static final int SLOP = 4;        // px a tap may wobble
    private static final int FADE_MILLIS = 200;
    private static final float ALPHA = 0.35f;
    private static final float HOVER_ALPHA = 0.8f;
    private static final Color INK = new Color(100, 116, 139);

    private final Component canvas;
    private final Runnable tapped;
    private final Timer fade = new Timer(15, e -> repaint());
    private boolean shown;
    private boolean hover;
    private long shownAt;
    private MouseEvent press;     // gesture we got, null if none
    private boolean handingOff;   // it turned into a drag, canvas gets the rest

    FaintEquals(Component canvas, Runnable tapped) {
        this.canvas = canvas;
        this.tapped = tapped;
        setOpaque(false);
        setVisible(false);
        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                pressed(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                dragged(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                released(e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setHover(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setHover(false);
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    // square right of the formula, mid height; both in area px
    static Rectangle spot(Rectangle formula, double charHeight) {
        int side = (int) Math.round(Math.max(MIN_SIZE, Math.min(MAX_SIZE, SIZE * charHeight)));
        int x = (int) Math.round(formula.getMaxX() + GAP * charHeight);
        int y = (int) Math.round(formula.getCenterY() - side / 2.0);
        return new Rectangle(x, y, side, side);
    }

    void showAt(Rectangle bounds) {
        setBounds(bounds);
        shown = true;
        shownAt = System.currentTimeMillis();
        fade.restart();
        setVisible(true);
        repaint();
    }

    // stops drawing + lets presses through; a gesture already going still finishes
    void hideNow() {
        shown = false;
        hover = false;
        fade.stop();
        setCursor(Cursor.getDefaultCursor());
        if (press == null) {
            setVisible(false);
        }
        repaint();
    }

    boolean isShown() {
        return shown;
    }

    @Override
    public boolean contains(int x, int y) {
        double r = Math.min(getWidth(), getHeight()) / 2.0;
        return shown && Math.hypot(x - getWidth() / 2.0, y - getHeight() / 2.0) <= r;
    }

    private void pressed(MouseEvent e) {
        if (!shown) {
            return;
        }
        press = e;
        handingOff = false;
    }

    private void dragged(MouseEvent e) {
        if (press == null) {
            return;
        }
        if (!handingOff && far(e)) {
            handingOff = true;
            hideNow();
            forward(press);  // canvas never saw the press, send it from where it started
        }
        if (handingOff) {
            forward(e);
        }
    }

    private void released(MouseEvent e) {
        if (press == null) {
            return;
        }
        boolean tap = !handingOff && !far(e) && shown;
        if (handingOff) {
            forward(e);
        }
        press = null;
        handingOff = false;
        if (tap) {
            hideNow();
            tapped.run();
        } else if (!shown) {
            setVisible(false);
        }
    }

    private boolean far(MouseEvent e) {
        return Math.abs(e.getX() - press.getX()) > SLOP || Math.abs(e.getY() - press.getY()) > SLOP;
    }

    private void forward(MouseEvent e) {
        canvas.dispatchEvent(SwingUtilities.convertMouseEvent(this, e, canvas));
    }

    private void setHover(boolean on) {
        if (!shown || hover == on) {
            return;
        }
        hover = on;
        setCursor(on ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!shown) {
            return;
        }
        float in = Math.min(1f, (System.currentTimeMillis() - shownAt) / (float) FADE_MILLIS);
        if (in >= 1f) {
            fade.stop();
        }
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (hover ? HOVER_ALPHA : ALPHA) * in));
        double d = Math.min(getWidth(), getHeight()) - 2;
        float pen = (float) Math.max(1.5, d / 12);
        g2.setColor(new Color(INK.getRed(), INK.getGreen(), INK.getBlue(), 40));
        g2.fill(new Ellipse2D.Double(1, 1, d, d));
        g2.setColor(INK);
        g2.setStroke(new BasicStroke(pen, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(new Ellipse2D.Double(1, 1, d, d));
        double cx = 1 + d / 2;
        double cy = 1 + d / 2;
        double half = d * 0.22;
        double apart = d * 0.12;
        g2.draw(new Line2D.Double(cx - half, cy - apart, cx + half, cy - apart));
        g2.draw(new Line2D.Double(cx - half, cy + apart, cx + half, cy + apart));
        g2.dispose();
    }
}
