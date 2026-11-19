package entity;

public class ObjectPreset {

    private String name;
    private double radius;
    private double mass;
    private double bounciness;
    private double friction;
    private int colour;

    public ObjectPreset(String name, double radius, double mass,
                        double bounciness, double friction, int colour) {
        this.name = name;
        this.radius = radius;
        this.mass = mass;
        this.bounciness = bounciness;
        this.friction = friction;
        this.colour = colour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getBounciness() {
        return bounciness;
    }

    public void setBounciness(double bounciness) {
        this.bounciness = bounciness;
    }

    public double getFriction() {
        return friction;
    }

    public void setFriction(double friction) {
        this.friction = friction;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    @Override
    public String toString() {
        return "ObjectPreset{name='" + name + '\''
                + ", radius=" + radius
                + ", mass=" + mass
                + ", bounciness=" + bounciness
                + ", friction=" + friction
                + ", colour=" + colour + '}';
    }
}
