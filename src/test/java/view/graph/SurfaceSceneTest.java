package view.graph;

import interface_adapter.graph.GraphState;
import org.junit.jupiter.api.Test;

import java.awt.geom.Rectangle2D;

import static org.junit.jupiter.api.Assertions.*;

class SurfaceSceneTest {
    private static GraphState plane() {
        int n = 11;
        double[][] z = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                z[i][j] = -1 + 0.2 * i;   // z = x on [-1, 1]
            }
        }
        GraphState s = new GraphState();
        s.setKind(GraphState.Kind.SURFACE);
        s.setWindow(-1, 1, -1, 1);
        s.setZRange(-1, 1);
        s.setZ(z);
        return s;
    }

    private final Rectangle2D area = new Rectangle2D.Double(0, 0, 400, 300);

    @Test
    void pickingAProjectedPointGivesItBack() {
        SurfaceScene scene = SurfaceScene.build(plane(), new OrbitCamera(), area);
        double[] p = scene.project(0.3, -0.4, 0.3);
        double[] xy = scene.pick(p[0], p[1]);
        assertNotNull(xy);
        assertEquals(0.3, xy[0], 0.02);
        assertEquals(-0.4, xy[1], 0.02);
    }

    @Test
    void offTheSurfaceIsNull() {
        SurfaceScene scene = SurfaceScene.build(plane(), new OrbitCamera(), area);
        assertNull(scene.pick(2, 2));
    }

    @Test
    void quadsAreSortedFarToNearAndHolesSkipped() {
        GraphState s = plane();
        double[][] z = s.getZ();
        z[5][5] = Double.NaN;
        SurfaceScene scene = SurfaceScene.build(s, new OrbitCamera(), area);
        assertEquals(100 - 4, scene.quads().size(), "one NaN corner knocks out 4 cells");
        for (int k = 1; k < scene.quads().size(); k++) {
            assertTrue(scene.quads().get(k - 1).depth <= scene.quads().get(k).depth);
        }
    }

    @Test
    void cameraPitchIsClamped() {
        OrbitCamera cam = new OrbitCamera();
        cam.rotate(0, 100000);
        assertTrue(cam.pitch() <= Math.toRadians(89) + 1e-9);
        cam.reset();
        assertEquals(OrbitCamera.DEFAULT_PITCH, cam.pitch(), 1e-12);
    }
}
