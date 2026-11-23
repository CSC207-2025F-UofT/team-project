package entity;
import java.util.UUID;

public abstract class Layer {

    private String name;
    private boolean visible;
    private int position;
    private double transparency;
    private String ID;

    public Layer(String name, int position) {
        this.name = name;
        this.visible = true;
        this.position = position;
        this.transparency = 1.0;
        this.ID = UUID.randomUUID().toString();
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public String getID() {
        return ID;
    }
}
