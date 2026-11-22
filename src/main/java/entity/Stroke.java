package entity;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stroke {
    private final String colour; // hex code
    private final double width;
    private final List<Point2D.Double> points = new ArrayList<>();

    public Stroke(String colour, double width) {
        if (colour == null || !colour.matches("#[0-9A-Fa-f]{6}")) { // the string represents hex code
            throw new IllegalArgumentException("Colour must be a hex string like #FF0000");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        this.colour = colour.toUpperCase();
        this.width = width;
    }

    public void addPoint(double x, double y) {
        points.add(new Point2D.Double(x, y));
    }

    public String getColour() {
        return colour;
    }

    public double getWidth() {
        return width;
    }

    public List<Point2D.Double> getPoints() {
        List<Point2D.Double> copies = new ArrayList<>();
        for (Point2D.Double p : points) {
            copies.add(new Point2D.Double(p.x, p.y));
        }
        return Collections.unmodifiableList(copies);
    }
}