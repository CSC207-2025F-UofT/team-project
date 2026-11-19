package view.graph;

import java.awt.geom.Rectangle2D;

// spins the 3d box around z, tilts it up/down, weak perspective; box coords are -1..1
final class OrbitCamera {
    static final double DEFAULT_YAW = Math.toRadians(-38);
    static final double DEFAULT_PITCH = Math.toRadians(24);
    private static final double MIN_PITCH = Math.toRadians(-12);
    private static final double MAX_PITCH = Math.toRadians(89);
    private static final double RADIANS_PER_PIXEL = 0.0105;
    private static final double EYE = 6;          // perspective distance, bigger = flatter
    private static final double FILL = 0.34;      // box half size vs the short side of the area

    private double yaw = DEFAULT_YAW;
    private double pitch = DEFAULT_PITCH;
    private int version;   // bumps on every move, scenes cache on it

    void rotate(double dxPixels, double dyPixels) {
        yaw += dxPixels * RADIANS_PER_PIXEL;
        pitch = Math.max(MIN_PITCH, Math.min(MAX_PITCH, pitch + dyPixels * RADIANS_PER_PIXEL));
        version++;
    }

    void reset() {
        yaw = DEFAULT_YAW;
        pitch = DEFAULT_PITCH;
        version++;
    }

    int version() {
        return version;
    }

    double yaw() {
        return yaw;
    }

    double pitch() {
        return pitch;
    }

    // box point -> {screen x, screen y, depth}, bigger depth = nearer the eye
    double[] project(double x, double y, double z, Rectangle2D area) {
        double c = Math.cos(yaw);
        double s = Math.sin(yaw);
        double x1 = x * c - y * s;
        double y1 = x * s + y * c;        // into the screen
        double cp = Math.cos(pitch);
        double sp = Math.sin(pitch);
        double up = y1 * sp + z * cp;
        double depth = -y1 * cp + z * sp;
        double persp = EYE / (EYE - depth);
        double scale = Math.min(area.getWidth(), area.getHeight()) * FILL;
        return new double[]{area.getCenterX() + scale * x1 * persp, area.getCenterY() - scale * up * persp, depth};
    }
}
