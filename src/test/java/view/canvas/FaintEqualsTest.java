package view.canvas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.JPanel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static java.awt.event.MouseEvent.MOUSE_DRAGGED;
import static java.awt.event.MouseEvent.MOUSE_PRESSED;
import static java.awt.event.MouseEvent.MOUSE_RELEASED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// faint "=" chip: tap calls back, a drag goes to the canvas like the chip wasn't there
class FaintEqualsTest {
    private final JPanel parent = new JPanel(null);
    private final JPanel canvas = new JPanel();
    private final List<Integer> canvasEvents = new ArrayList<>();
    private final List<Point> canvasPoints = new ArrayList<>();
    private int taps;
    private FaintEquals chip;

    @BeforeEach
    void setUp() {
        MouseAdapter record = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                log(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                log(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                log(e);
            }
        };
        canvas.addMouseListener(record);
        canvas.addMouseMotionListener(record);
        chip = new FaintEquals(canvas, () -> taps++);
        parent.setSize(600, 400);
        parent.add(chip);
        parent.add(canvas);
        canvas.setBounds(0, 0, 600, 400);
        chip.showAt(new Rectangle(100, 100, 24, 24));
    }

    private void log(MouseEvent e) {
        canvasEvents.add(e.getID());
        canvasPoints.add(e.getPoint());
    }

    // chip coords
    private void send(int id, int x, int y) {
        chip.dispatchEvent(new MouseEvent(chip, id, 0, 0, x, y, 1, false, MouseEvent.BUTTON1));
    }

    @Test
    void spotSitsRightOfTheFormulaAtMidHeight() {
        Rectangle r = FaintEquals.spot(new Rectangle(100, 100, 100, 50), 50);
        assertEquals(32, r.width);
        assertEquals(32, r.height);
        assertEquals(230, r.x);
        assertEquals(125, r.getCenterY(), 0.5);
    }

    @Test
    void smallFormulaGetsTheSmallestChip() {
        assertEquals(18, FaintEquals.spot(new Rectangle(100, 100, 40, 10), 10).width);
    }

    @Test
    void tapCallsBack() {
        assertTrue(chip.isShown());
        assertTrue(chip.contains(12, 12));
        send(MOUSE_PRESSED, 12, 12);
        send(MOUSE_RELEASED, 14, 12);
        assertEquals(1, taps);
        assertTrue(canvasEvents.isEmpty());
        assertFalse(chip.isShown());
    }

    @Test
    void dragIsHandedToTheCanvas() {
        send(MOUSE_PRESSED, 12, 12);
        send(MOUSE_DRAGGED, 22, 12);
        send(MOUSE_DRAGGED, 42, 12);
        send(MOUSE_RELEASED, 42, 12);
        assertEquals(List.of(MOUSE_PRESSED, MOUSE_DRAGGED, MOUSE_DRAGGED, MOUSE_RELEASED), canvasEvents);
        assertEquals(new Point(112, 112), canvasPoints.get(0), "press re-sent where it started, canvas coords");
        assertEquals(0, taps);
        assertFalse(chip.isShown());
    }

    @Test
    void hideMidGestureStillFinishesTheHandOff() {
        send(MOUSE_PRESSED, 12, 12);
        send(MOUSE_DRAGGED, 22, 12);
        chip.hideNow();  // canvas press -> page dismiss does this
        send(MOUSE_DRAGGED, 42, 12);
        send(MOUSE_RELEASED, 42, 12);
        assertEquals(MOUSE_RELEASED, canvasEvents.get(canvasEvents.size() - 1));
        assertEquals(4, canvasEvents.size());
    }

    @Test
    void hiddenChipLetsPressesThrough() {
        chip.showAt(new Rectangle(0, 0, 20, 20));
        chip.hideNow();
        assertFalse(chip.contains(10, 10));
    }
}
