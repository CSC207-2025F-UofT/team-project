package view;

import interface_adapter.simulation.CameraPose;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CameraTest {
    private static final double TOLERANCE = 1e-9;

    @Test
    void forwardAndSidewaysFollowTheCurrentCameraAngle() {
        CameraPose pose = new CameraPose().move(2, 0, 0);
        Camera.Point forward = projectedPosition(pose);
        assertEquals(0, forward.x, TOLERANCE);
        assertTrue(forward.depth < 0);

        pose = pose.rotate(Math.PI / 2);
        assertEquals(2, projectedPosition(pose).x, TOLERANCE);
        pose = pose.move(0, 3, 0);
        assertEquals(5, projectedPosition(pose).x, TOLERANCE);

        pose = pose.move(4, 0, 0);
        assertEquals(5, projectedPosition(pose).x, TOLERANCE);
        assertTrue(projectedPosition(pose).depth < 0);
    }

    @Test
    void upAndDownFollowTheWorldVerticalAtAnyAngle() {
        CameraPose pose = new CameraPose().rotate(1.2).move(0, 0, 4);
        Camera.Point expected = new Camera(pose.yaw).project(0, 0, 4);
        assertEquals(expected.y, projectedPosition(pose).y, TOLERANCE);
        assertEquals(expected.depth, projectedPosition(pose).depth, TOLERANCE);
        assertTrue(expected.depth > 0);

        pose = pose.move(0, 0, -4);
        assertEquals(0, projectedPosition(pose).y, TOLERANCE);
        assertEquals(0, projectedPosition(pose).depth, TOLERANCE);
    }

    private static Camera.Point projectedPosition(CameraPose pose) {
        return new Camera(pose.yaw).project(pose.x, pose.y, pose.z);
    }
}
