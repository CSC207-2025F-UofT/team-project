package view;

import adapters.viewmodel.GameViewModel;

import javax.swing.JComponent;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/** Paints board effects above the tiles without intercepting mouse input. */
final class BoardEffects extends JComponent {
    private static final BasicStroke ARROW_EDGE = roundStroke(6f);
    private static final BasicStroke ARROW_LINE = roundStroke(2.5f);
    private static final BasicStroke TRAIL_EDGE = roundStroke(8f);
    private static final BasicStroke TRAIL_LINE = roundStroke(3f);

    private final Function<Point, Rectangle> tileBounds;
    private final List<TilePath> arrows = new ArrayList<>();
    private final List<Trajectory> trajectories = new ArrayList<>();
    private final Map<Point, Animation> flashes = new HashMap<>();
    private final Timer timer = new Timer(16, event -> advanceAnimations());

    BoardEffects(Function<Point, Rectangle> tileBounds) {
        this.tileBounds = tileBounds;
    }

    void drawArrow(Point from, Point to) {
        TilePath path = new TilePath(from, to);
        if (!path.from().equals(path.to())) {
            arrows.add(path);
            repaint();
        }
    }

    void clearArrows() {
        arrows.clear();
        repaint();
    }

    void animateTrajectory(Point from, Point to, int durationMillis) {
        TilePath path = new TilePath(from, to);
        trajectories.add(new Trajectory(path, Animation.start(durationMillis)));
        startAnimation();
    }

    void flashTiles(Collection<Point> tiles, int durationMillis) {
        List<Point> positions = tiles.stream().map(BoardEffects::copyTile).toList();
        Animation flash = Animation.start(durationMillis);
        if (!positions.isEmpty()) {
            for (Point position : positions) {
                flashes.put(position, flash);
            }
            startAnimation();
        }
    }

    @Override
    public boolean contains(int x, int y) {
        return false;
    }

    @Override
    public void removeNotify() {
        timer.stop();
        trajectories.clear();
        flashes.clear();
        super.removeNotify();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        if (arrows.isEmpty() && trajectories.isEmpty() && flashes.isEmpty()) {
            return;
        }
        long now = System.nanoTime();
        Graphics2D painter = (Graphics2D) graphics.create();
        painter.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintFlashes(painter, now);
        for (TilePath arrow : arrows) {
            paintArrow(painter, center(arrow.from()), center(arrow.to()));
        }
        for (Trajectory trajectory : trajectories) {
            double progress = trajectory.timing().progress(now);
            if (progress < 1) {
                TilePath path = trajectory.path();
                paintTrajectory(painter, center(path.from()), center(path.to()), progress);
            }
        }
        painter.dispose();
    }

    private void startAnimation() {
        timer.start();
        repaint();
    }

    private void advanceAnimations() {
        long now = System.nanoTime();
        trajectories.removeIf(trajectory -> trajectory.timing().progress(now) >= 1);
        flashes.values().removeIf(flash -> flash.progress(now) >= 1);
        if (trajectories.isEmpty() && flashes.isEmpty()) {
            timer.stop();
        }
        repaint();
    }

    private void paintFlashes(Graphics2D painter, long now) {
        painter.setColor(GameViewStyle.HIT_FLASH);
        for (var entry : flashes.entrySet()) {
            float remaining = 1 - (float) entry.getValue().progress(now);
            Rectangle area = tileBounds.apply(entry.getKey());
            painter.setComposite(AlphaComposite.SrcOver.derive(remaining * remaining));
            painter.fillRect(area.x, area.y, area.width, area.height);
        }
        painter.setComposite(AlphaComposite.SrcOver);
    }

    private static void paintArrow(Graphics2D painter, Point2D from, Point2D to) {
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();
        double length = Math.hypot(dx, dy);
        if (length == 0) {
            return;
        }
        double head = Math.min(14, length / 3);
        double alongX = head * dx / length;
        double alongY = head * dy / length;
        double x = to.getX();
        double y = to.getY();

        Path2D line = new Path2D.Double();
        line.moveTo(from.getX(), from.getY());
        line.lineTo(x, y);
        line.moveTo(x - alongX - 0.55 * alongY, y - alongY + 0.55 * alongX);
        line.lineTo(x, y);
        line.lineTo(x - alongX + 0.55 * alongY, y - alongY - 0.55 * alongX);
        painter.setColor(GameViewStyle.ARROW_OUTLINE);
        painter.setStroke(ARROW_EDGE);
        painter.draw(line);
        painter.setColor(GameViewStyle.ARROW_COLOR);
        painter.setStroke(ARROW_LINE);
        painter.draw(line);
    }

    private static void paintTrajectory(Graphics2D painter, Point2D from, Point2D to, double progress) {
        Point2D tip = interpolate(from, to, progress);
        Point2D tail = interpolate(from, to, Math.max(0, progress - 0.18));
        if (!tail.equals(tip)) {
            Line2D trail = new Line2D.Double(tail, tip);
            paintFadingTrail(painter, trail, GameViewStyle.TRAJECTORY_GLOW, TRAIL_EDGE);
            paintFadingTrail(painter, trail, GameViewStyle.TRAJECTORY_CORE, TRAIL_LINE);
        }
        painter.setColor(GameViewStyle.TRAJECTORY_GLOW);
        painter.fill(new Ellipse2D.Double(tip.getX() - 7, tip.getY() - 7, 14, 14));
        painter.setColor(GameViewStyle.TRAJECTORY_CORE);
        painter.fill(new Ellipse2D.Double(tip.getX() - 3.5, tip.getY() - 3.5, 7, 7));
    }

    private static void paintFadingTrail(Graphics2D painter, Line2D trail, Color color, BasicStroke stroke) {
        Color transparent = new Color(color.getRed(), color.getGreen(), color.getBlue(), 0);
        painter.setPaint(new GradientPaint(trail.getP1(), transparent, trail.getP2(), color));
        painter.setStroke(stroke);
        painter.draw(trail);
    }

    private Point2D center(Point position) {
        Rectangle area = tileBounds.apply(position);
        return new Point2D.Double(area.getCenterX(), area.getCenterY());
    }

    private static Point2D interpolate(Point2D from, Point2D to, double progress) {
        return new Point2D.Double(
                from.getX() + (to.getX() - from.getX()) * progress,
                from.getY() + (to.getY() - from.getY()) * progress);
    }

    private static BasicStroke roundStroke(float width) {
        return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    private static Point copyTile(Point position) {
        Point copy = new Point(position);
        GameViewModel.tileIndex(copy.x, copy.y);
        return copy;
    }

    private record TilePath(Point from, Point to) {
        private TilePath {
            from = copyTile(from);
            to = copyTile(to);
        }
    }

    private record Trajectory(TilePath path, Animation timing) {
    }

    private record Animation(long startNanos, long durationNanos) {
        static Animation start(int durationMillis) {
            if (durationMillis <= 0) {
                throw new IllegalArgumentException("Animation duration must be positive.");
            }
            return new Animation(System.nanoTime(), TimeUnit.MILLISECONDS.toNanos(durationMillis));
        }

        double progress(long now) {
            return Math.min(1, Math.max(0, (double) (now - startNanos) / durationNanos));
        }
    }
}
