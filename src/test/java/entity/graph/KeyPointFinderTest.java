package entity.graph;

import entity.MathFunction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyPointFinderTest {
    private final KeyPointFinder finder = new KeyPointFinder(600);
    private final Viewport view = new Viewport(-10, 10, -10, 10);

    private static KeyPoint only(List<KeyPoint> points, KeyPoint.Kind kind, double x) {
        return points.stream().filter(p -> p.getKind() == kind && Math.abs(p.getX() - x) < 1e-6)
                .findFirst().orElseThrow(() -> new AssertionError(kind + " at " + x + " missing in " + points));
    }

    @Test
    void parabolaHasTwoRootsALowAndAYIntercept() {
        List<KeyPoint> pts = finder.find(new MathFunction("x^2-1", x -> x * x - 1), view);
        only(pts, KeyPoint.Kind.ROOT, -1);
        only(pts, KeyPoint.Kind.ROOT, 1);
        only(pts, KeyPoint.Kind.Y_INTERCEPT, 0);
        assertEquals(-1, pts.stream().filter(p -> p.getKind() == KeyPoint.Kind.Y_INTERCEPT)
                .findFirst().orElseThrow().getY(), 1e-12);
        // low at (0, -1) is the same spot as the intercept, kept once
        assertEquals(3, pts.size(), pts.toString());
    }

    @Test
    void cubicGivesItsHighAndLow() {
        List<KeyPoint> pts = finder.find(new MathFunction("x^3-3x", x -> x * x * x - 3 * x), view);
        assertEquals(2, only(pts, KeyPoint.Kind.MAXIMUM, -1).getY(), 1e-6);
        assertEquals(-2, only(pts, KeyPoint.Kind.MINIMUM, 1).getY(), 1e-6);
    }

    @Test
    void signFlipAtAnAsymptoteIsNotARoot() {
        List<KeyPoint> pts = finder.find(new MathFunction("1/x", x -> 1 / x), view);
        assertTrue(pts.stream().noneMatch(p -> p.getKind() == KeyPoint.Kind.ROOT), pts.toString());
    }

    @Test
    void tooManyOfOneKindAreDropped() {
        Viewport wide = new Viewport(-1000, 1000, -2, 2);
        List<KeyPoint> pts = finder.find(new MathFunction("sin x", Math::sin), wide);
        assertTrue(pts.stream().noneMatch(p -> p.getKind() == KeyPoint.Kind.ROOT));
    }
}
