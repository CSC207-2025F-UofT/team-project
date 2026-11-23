package view;

/** Orthographic camera for the heightmap and objects drawn over it. */
final class Camera {
    private static final double SIN_ELEVATION = Math.sin(Math.toRadians(34));
    private static final double COS_ELEVATION = Math.cos(Math.toRadians(34));

    private final double cosYaw;
    private final double sinYaw;

    Camera(double yaw) {
        cosYaw = Math.cos(yaw);
        sinYaw = Math.sin(yaw);
    }

    Point project(double x, double y, double z) {
        double across = x * cosYaw - y * sinYaw;
        double forward = x * sinYaw + y * cosYaw;
        return new Point(across, forward * SIN_ELEVATION - z * COS_ELEVATION,
                forward * COS_ELEVATION + z * SIN_ELEVATION);
    }

    static final class Point {
        final double x;
        final double y;
        final double depth;

        Point(double x, double y, double depth) {
            this.x = x;
            this.y = y;
            this.depth = depth;
        }
    }
}
