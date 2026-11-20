package entity;

public class Ball {

    private Vector3D position;
    private Vector3D velocity;
    private ObjectPreset preset;

    public Ball(Vector3D position, Vector3D velocity, ObjectPreset preset) {
        this.position = position;
        this.velocity = velocity;
        this.preset = preset;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setVelocity(Vector3D velocity) {
        this.velocity = velocity;
    }

    public ObjectPreset getPreset() {
        return preset;
    }

    public void setPreset(ObjectPreset preset) {
        this.preset = preset;
    }

    // needs to be implemented when terrain class is created
    public void isOnGround() {
        return;
    }

    @Override
    public String toString() {
        return "Ball{position=" + position + ", velocity=" + velocity
                + ", preset=" + preset + "}";
    }
}
