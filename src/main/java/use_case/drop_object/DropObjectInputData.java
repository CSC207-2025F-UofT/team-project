package use_case.drop_object;

import entity.ObjectPreset;

public class DropObjectInputData {

    private double x;
    private double y;
    private ObjectPreset preset;
    private double dropSpeed;

    public DropObjectInputData(double dropSpeed, double x, double y, ObjectPreset preset) {
        this.dropSpeed = dropSpeed;
        this.x = x;
        this.y = y;
        this.preset = preset;
    }

    public double getDropSpeed() {
        return dropSpeed;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public ObjectPreset getPreset() {
        return preset;
    }
}
