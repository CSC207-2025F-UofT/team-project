package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Stroke {
    private final String colour; // hex code
    private final double width;
    private final List<Point> points = new ArrayList<>();

    public Stroke(String colour, double width) {
        if (colour == null || !colour.matches("#[0-9A-Fa-f]{6}")) {  // the string represents hex code
            throw new IllegalArgumentException("Colour must be a hex string like #FF0000");
        }
        if (width <= 0) {
            throw new IllegalArgumentException("Width must be positive");
        }
        this.colour = colour.toUpperCase();
        this.width = width;
    }

    public void addPoint(double x, double y) {
        points.add(new Point(x, y));
    }

    public String getColour() {
        return colour;
    }

    public double getWidth() {
        return width;
    }

    public List<Point> getPoints() {
        return Collections.unmodifiableList(points);
    }

    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}