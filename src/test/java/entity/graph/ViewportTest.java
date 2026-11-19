package entity.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ViewportTest {
    private final Viewport view = new Viewport(-10, 10, -5, 5);

    @Test
    void panShiftsBothEdges() {
        Viewport moved = view.panned(2, -1);
        assertEquals(new Viewport(-8, 12, -6, 4), moved);
        assertEquals(view.width(), moved.width(), 1e-12);
    }

    @Test
    void zoomKeepsTheCentrePointStill() {
        Viewport in = view.zoomed(4, 1, 0.5);
        assertEquals(10, in.width(), 1e-12);
        assertEquals(5, in.height(), 1e-12);
        // 4 was 70% across before, still is
        assertEquals((4 - view.getXMin()) / view.width(), (4 - in.getXMin()) / in.width(), 1e-12);
    }

    @Test
    void zoomStopsAtTheLimits() {
        Viewport tiny = view;
        for (int i = 0; i < 200; i++) {
            tiny = tiny.zoomed(0, 0, 0.1);
        }
        assertTrue(tiny.width() >= Viewport.MIN_SPAN * 0.999);
        assertTrue(tiny.getXMin() < tiny.getXMax());
    }

    @Test
    void badBoundsAndBadFactorsAreRejectedOrIgnored() {
        assertThrows(IllegalArgumentException.class, () -> new Viewport(1, 1, 0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Viewport(0, Double.NaN, 0, 1));
        assertSame(view, view.zoomed(0, 0, -2));
        assertSame(view, view.panned(Double.NaN, 0));
    }
}
