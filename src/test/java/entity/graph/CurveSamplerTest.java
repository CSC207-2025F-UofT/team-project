package entity.graph;

import entity.MathFunction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurveSamplerTest {
    private final CurveSampler sampler = new CurveSampler(400);
    private final Viewport view = new Viewport(-10, 10, -10, 10);

    @Test
    void smoothCurveIsOnePiece() {
        Curve c = sampler.sample(new MathFunction("x^2", x -> x * x), view);
        assertEquals(1, c.getPieces().size());
        assertTrue(c.getPieces().get(0).size() > 400);
    }

    @Test
    void steepButContinuousStaysJoined() {
        Curve c = sampler.sample(new MathFunction("x^9", x -> Math.pow(x, 9)), view);
        assertEquals(1, c.getPieces().size());
    }

    @Test
    void asymptotesSplitTheLine() {
        Curve tan = sampler.sample(new MathFunction("tan x", Math::tan), view);
        assertTrue(tan.getPieces().size() >= 6, "tan has 6 asymptotes in [-10, 10]");
        Curve inv = sampler.sample(new MathFunction("1/x", x -> 1 / x), view);
        assertEquals(2, inv.getPieces().size());
    }

    @Test
    void holesSplitAndHugeValuesGetClamped() {
        Curve root = sampler.sample(new MathFunction("sqrt x", Math::sqrt), view);
        assertEquals(1, root.getPieces().size());
        assertTrue(root.getPieces().get(0).getXs()[0] >= 0);
        Curve exp = sampler.sample(new MathFunction("e^x", Math::exp), view);
        for (double y : exp.getPieces().get(0).getYs()) {
            assertTrue(y <= 10 + 4 * 20);
        }
    }
}
