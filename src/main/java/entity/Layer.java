package entity;
import java.util.UUID;

public abstract class Layer {

    private String name;
    private boolean visible;
    private double x;
    private double y;
    private double transparency;
    private String layerId;

    public Layer(String name) {
        this.name = name;
        this.visible = true;
        this.x = 0.0;
        this.y = 0.0;
        this.transparency = 1.0;
        this.layerId = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisible() {
        return visible;
    }

    public void toggleLayer() {
        this.visible = !this.visible;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getTransparency() {
        return transparency;
    }

    public void adjustTransparency(double transparency) {
        if (transparency < 0.0 || transparency > 1.0) {
            throw new IllegalArgumentException(
                    "Transparency must be between 0.0 and 1.0"
            );
        }

        this.transparency = transparency;
    }

    public String getLayerId() {
        return layerId;
    }
}