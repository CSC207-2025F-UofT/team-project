package interface_adapter.simulation;

/** Display coordinates in metres relative to the initial camera position. Yaw is in radians. */
public final class CameraPose {
    public final double x;
    public final double y;
    public final double z;
    public final double yaw;

    public CameraPose() {
        this(0, 0, 0, Math.toRadians(37));
    }

    private CameraPose(double x, double y, double z, double yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
    }

    public CameraPose move(double forward, double right, double up) {
        double cos = Math.cos(yaw);
        double sin = Math.sin(yaw);
        return new CameraPose(x + right * cos - forward * sin, y - right * sin - forward * cos, z + up, yaw);
    }

    public CameraPose rotate(double radians) {
        return new CameraPose(x, y, z, Math.IEEEremainder(yaw + radians, 2 * Math.PI));
    }

    CameraPose centered() {
        return new CameraPose(0, 0, 0, yaw);
    }
}
