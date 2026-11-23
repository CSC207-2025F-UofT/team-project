package view;

import interface_adapter.simulation.CameraPose;
import interface_adapter.simulation.ScenePresenter;
import interface_adapter.simulation.SimulationState;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class TerrainView3D extends JPanel {
    private static final Color BACKGROUND = new Color(227, 238, 241);
    private static final Color LOWLAND = new Color(86, 132, 89);
    private static final Color UPLAND = new Color(183, 171, 132);
    private static final int MARGIN = 30;

    private final ScenePresenter presenter;
    private Camera camera;
    private CameraPose pose;
    private SimulationState state;
    private SimulationState projectedState;
    private final List<Shape> shapes = new ArrayList<>();
    private final Bounds bounds = new Bounds();

    public TerrainView3D(ScenePresenter presenter) {
        this.presenter = presenter;
        setBackground(BACKGROUND);
        setPreferredSize(new Dimension(840, 540));
        getAccessibleContext().setAccessibleName("3D terrain");
        setToolTipText("Drag to rotate. W/S forward/back, A/D left/right, Q/E up/down.");
        bindMovement('W', 1, 0, 0);
        bindMovement('S', -1, 0, 0);
        bindMovement('A', 0, -1, 0);
        bindMovement('D', 0, 1, 0);
        bindMovement('Q', 0, 0, 1);
        bindMovement('E', 0, 0, -1);

        MouseAdapter rotation = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                presenter.beginRotation(event.getX());
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                presenter.dragTo(event.getX());
            }
        };
        addMouseListener(rotation);
        addMouseMotionListener(rotation);
    }

    private void bindMovement(char key, int forward, int right, int up) {
        // Typed bindings let a focused text field handle its own letters first.
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(Character.toLowerCase(key)), key);
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key), key);
        getActionMap().put(key, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent event) {
                presenter.moveCamera(forward, right, up);
            }
        });
    }

    void display(SimulationState state, CameraPose pose) {
        if (this.pose == null || this.pose.yaw != pose.yaw) {
            camera = new Camera(pose.yaw);
            projectedState = null;
        }
        this.state = state;
        this.pose = pose;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (state == null || !state.hasTerrain()) {
            return;
        }
        if (state != projectedState) {
            project(state);
            projectedState = state;
        }

        Graphics2D drawing = (Graphics2D) graphics.create();
        try {
            Screen screen = new Screen(bounds, getWidth(), getHeight(), camera.project(pose.x, pose.y, pose.z));
            for (Shape shape : shapes) {
                shape.paint(drawing, screen);
            }
        } finally {
            drawing.dispose();
        }
    }

    private void project(SimulationState state) {
        shapes.clear();
        bounds.reset();
        double minHeight = Double.POSITIVE_INFINITY;
        double maxHeight = Double.NEGATIVE_INFINITY;
        Camera.Point[][] vertices = new Camera.Point[state.getRows()][state.getCols()];
        double cell = state.getCellSize();
        for (int row = 0; row < state.getRows(); row++) {
            for (int col = 0; col < state.getCols(); col++) {
                double height = state.getHeight(row, col);
                minHeight = Math.min(minHeight, height);
                maxHeight = Math.max(maxHeight, height);
                Camera.Point point = camera.project(col * cell, row * cell, height);
                vertices[row][col] = point;
                bounds.include(point, 0);
            }
        }

        double range = Math.max(cell, maxHeight - minHeight);
        for (int row = 0; row < state.getRows() - 1; row++) {
            for (int col = 0; col < state.getCols() - 1; col++) {
                double nw = state.getHeight(row, col);
                double ne = state.getHeight(row, col + 1);
                double sw = state.getHeight(row + 1, col);
                double se = state.getHeight(row + 1, col + 1);

                shapes.add(new Triangle(vertices[row][col], vertices[row][col + 1], vertices[row + 1][col],
                        terrainColour((ne - nw) / cell, (sw - nw) / cell, (nw + ne + sw) / 3,
                                minHeight, range)));
                shapes.add(new Triangle(vertices[row + 1][col + 1], vertices[row + 1][col], vertices[row][col + 1],
                        terrainColour((se - sw) / cell, (se - ne) / cell, (se + sw + ne) / 3,
                                minHeight, range)));
            }
        }

        for (SimulationState.Ball ball : state.getBalls()) {
            Camera.Point point = camera.project(ball.x, ball.y, ball.z);
            shapes.add(new Sphere(point, ball.radius, new Color(ball.colour)));
            bounds.include(point, ball.radius);
        }
        shapes.sort(Comparator.comparingDouble(Shape::depth));
    }

    private static Color terrainColour(double dx, double dy, double height, double minHeight, double range) {
        double illumination = (0.80 + 0.48 * dx + 0.36 * dy) / Math.sqrt(1 + dx * dx + dy * dy);
        double brightness = 0.59 + 0.49 * Math.max(0, illumination);
        double elevation = (height - minHeight) / range;
        return new Color(channel(LOWLAND.getRed(), UPLAND.getRed(), elevation, brightness),
                channel(LOWLAND.getGreen(), UPLAND.getGreen(), elevation, brightness),
                channel(LOWLAND.getBlue(), UPLAND.getBlue(), elevation, brightness));
    }

    private static int channel(int low, int high, double elevation, double brightness) {
        return (int) ((low + (high - low) * elevation) * brightness);
    }

    private interface Shape {
        double depth();

        void paint(Graphics2D graphics, Screen screen);
    }

    private static final class Triangle implements Shape {
        private final Camera.Point a;
        private final Camera.Point b;
        private final Camera.Point c;
        private final Color colour;

        private Triangle(Camera.Point a, Camera.Point b, Camera.Point c, Color colour) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.colour = colour;
        }

        @Override
        public double depth() {
            return (a.depth + b.depth + c.depth) / 3;
        }

        @Override
        public void paint(Graphics2D graphics, Screen screen) {
            // Filled triangles share edges. Antialiasing them separately can show seams.
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            Path2D path = screen.triangle;
            path.reset();
            path.moveTo(screen.x(a.x), screen.y(a.y));
            path.lineTo(screen.x(b.x), screen.y(b.y));
            path.lineTo(screen.x(c.x), screen.y(c.y));
            path.closePath();
            graphics.setColor(colour);
            graphics.fill(path);
        }
    }

    private static final class Sphere implements Shape {
        private final Camera.Point center;
        private final double radius;
        private final Color colour;

        private Sphere(Camera.Point center, double radius, Color colour) {
            this.center = center;
            this.radius = radius;
            this.colour = colour;
        }

        @Override
        public double depth() {
            return center.depth + radius * 0.5;
        }

        @Override
        public void paint(Graphics2D graphics, Screen screen) {
            double drawRadius = Math.max(2, radius * screen.scale);
            double x = screen.x(center.x);
            double y = screen.y(center.y);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setPaint(new RadialGradientPaint(
                    (float) (x - drawRadius * 0.3), (float) (y - drawRadius * 0.35), (float) (drawRadius * 1.4),
                    new float[] {0, 0.46f, 1}, new Color[] {colour.brighter(), colour, colour.darker().darker()}));
            graphics.fill(new Ellipse2D.Double(x - drawRadius, y - drawRadius, drawRadius * 2, drawRadius * 2));
        }
    }

    private static final class Screen {
        private final Path2D triangle = new Path2D.Double();
        private final double scale;
        private final double offsetX;
        private final double offsetY;

        private Screen(Bounds bounds, int width, int height, Camera.Point cameraPosition) {
            double spanX = bounds.maxX - bounds.minX;
            double spanY = bounds.maxY - bounds.minY;
            double fit = Math.min(Math.max(1, width - 2 * MARGIN) / spanX,
                    Math.max(1, height - 2 * MARGIN) / spanY);
            // Keep a single drawing scale. Moving closer magnifies the map, up to 4x.
            double startDistance = 2 * Math.max(spanX, spanY);
            double distance = Math.max(startDistance / 4, startDistance + cameraPosition.depth);
            scale = fit * (startDistance / distance);
            offsetX = width / 2.0 - ((bounds.minX + bounds.maxX) / 2 + cameraPosition.x) * scale;
            offsetY = height / 2.0 - ((bounds.minY + bounds.maxY) / 2 + cameraPosition.y) * scale;
        }

        private double x(double projectedX) {
            return offsetX + scale * projectedX;
        }

        private double y(double projectedY) {
            return offsetY + scale * projectedY;
        }
    }

    private static final class Bounds {
        private double minX;
        private double maxX;
        private double minY;
        private double maxY;

        private void reset() {
            minX = Double.POSITIVE_INFINITY;
            maxX = Double.NEGATIVE_INFINITY;
            minY = Double.POSITIVE_INFINITY;
            maxY = Double.NEGATIVE_INFINITY;
        }

        private void include(Camera.Point point, double radius) {
            minX = Math.min(minX, point.x - radius);
            maxX = Math.max(maxX, point.x + radius);
            minY = Math.min(minY, point.y - radius);
            maxY = Math.max(maxY, point.y + radius);
        }
    }
}
