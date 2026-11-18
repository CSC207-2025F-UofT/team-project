package entity.graph;

import entity.MathFunction;
import entity.SurfaceFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlotTest {
    private final CurvePlot parabola = new CurvePlot(new MathFunction("x^2-4", x -> x * x - 4),
            new Viewport(-5, 5, -5, 5));

    @Test
    void traceGivesTheRealValueNotThePixel() {
        TracePoint p = parabola.trace(1.5, 99, 0.01);
        assertEquals(1.5, p.getX(), 1e-12);
        assertEquals(-1.75, p.getY(), 1e-12);
        assertNull(p.getKind());
        assertFalse(p.isSurface());
    }

    @Test
    void traceSnapsOntoANearbyKeyPoint() {
        TracePoint p = parabola.trace(2.04, 0, 0.1);
        assertEquals(KeyPoint.Kind.ROOT, p.getKind());
        assertEquals(2, p.getX(), 1e-9);
    }

    @Test
    void lookingElsewhereKeepsHome() {
        Plot moved = parabola.lookingAt(parabola.getViewport().panned(3, 0));
        assertEquals(parabola.getViewport(), moved.getHome());
        assertEquals(-2, moved.getViewport().getXMin(), 1e-12);
    }

    @Test
    void surfaceTracesTheRealZAndSamplesAGrid() {
        SurfacePlot bowl = new SurfacePlot(new SurfaceFunction("z=x^2+y^2", (x, y) -> x * x + y * y),
                new Viewport(-2, 2, -2, 2));
        assertEquals(5, bowl.trace(1, 2, 0).getZ(), 1e-12);
        assertNull(bowl.trace(3, 0, 0), "outside the floor");
        SurfaceGrid grid = bowl.getGrid();
        assertEquals(SurfacePlot.GRID, grid.size());
        assertEquals(8, grid.zAt(0, 0), 1e-12);
        assertTrue(grid.getZMin() < 0.1 && grid.getZMax() > 7.9);
    }

    @Test
    void visitorPicksTheRightKind() {
        String kind = parabola.accept(new PlotVisitor<String>() {
            @Override
            public String visitCurve(CurvePlot plot) {
                return "2d";
            }

            @Override
            public String visitSurface(SurfacePlot plot) {
                return "3d";
            }
        });
        assertEquals("2d", kind);
    }
}
