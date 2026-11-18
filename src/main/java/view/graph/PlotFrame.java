package view.graph;

import interface_adapter.graph.GraphState;

import java.awt.geom.Rectangle2D;

// world <-> pixel for the 2d plot area, y goes up
final class PlotFrame {
    private final double xMin;
    private final double xMax;
    private final double yMin;
    private final double yMax;
    private final Rectangle2D area;

    PlotFrame(GraphState state, Rectangle2D area) {
        this(state.getXMin(), state.getXMax(), state.getYMin(), state.getYMax(), area);
    }

    PlotFrame(double xMin, double xMax, double yMin, double yMax, Rectangle2D area) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.area = area;
    }

    Rectangle2D area() {
        return area;
    }

    double px(double x) {
        return area.getX() + (x - xMin) / (xMax - xMin) * area.getWidth();
    }

    double py(double y) {
        return area.getMaxY() - (y - yMin) / (yMax - yMin) * area.getHeight();
    }

    double worldX(double px) {
        return xMin + (px - area.getX()) / area.getWidth() * (xMax - xMin);
    }

    double worldY(double py) {
        return yMin + (area.getMaxY() - py) / area.getHeight() * (yMax - yMin);
    }

    double xPerPixel() {
        return (xMax - xMin) / area.getWidth();
    }

    double yPerPixel() {
        return (yMax - yMin) / area.getHeight();
    }
}
