package entity;

import java.util.Objects;

public final class BallFactory {

    public BallFactory() {
    }

    public Ball createDroppedBall(ObjectPreset preset, double x, double y,
                                  double groundHeight, double dropSpeed) {
        Objects.requireNonNull(preset, "preset");
        if (dropSpeed < 0) {
            throw new IllegalArgumentException("dropSpeed must be non-negative");
        }

        Vector3D position = new Vector3D(x, y, groundHeight + preset.getRadius());
        Vector3D velocity = new Vector3D(0, 0, -dropSpeed);
        return new Ball(position, velocity, preset);
    }

    public Ball createBall(Vector3D position, Vector3D velocity, ObjectPreset preset) {
        Objects.requireNonNull(position, "position");
        Objects.requireNonNull(velocity, "velocity");
        Objects.requireNonNull(preset, "preset");
        return new Ball(position, velocity, preset);
    }
}
