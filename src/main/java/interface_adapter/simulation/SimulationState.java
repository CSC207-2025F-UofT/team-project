package interface_adapter.simulation;

import use_case.scene.SceneViewData;
import use_case.scene.SceneViewData.HeightGrid;

import java.util.ArrayList;
import java.util.List;

/** Read-only display values for the Swing panels. */
public final class SimulationState {
    private final HeightGrid terrain;
    private final List<Ball> balls;
    private final String statusText;

    private SimulationState(HeightGrid terrain, List<Ball> balls, String statusText) {
        this.terrain = terrain;
        this.balls = balls;
        this.statusText = statusText;
    }

    public static SimulationState from(SceneViewData scene, String status) {
        List<Ball> balls = new ArrayList<>();
        for (SceneViewData.BallData ball : scene.getBalls()) {
            balls.add(new Ball(ball));
        }
        HeightGrid terrain = scene.getTerrain();
        String text = status.isEmpty() && terrain != null ? terrain.getName() : status;
        return new SimulationState(terrain, List.copyOf(balls), text);
    }

    SimulationState withFileStatus(String status) {
        return new SimulationState(terrain, balls, status);
    }

    public String getStatusText() {
        return statusText;
    }

    public boolean hasTerrain() {
        return terrain != null;
    }

    public boolean hasSameTerrain(SimulationState other) {
        return terrain == other.terrain;
    }

    public int getRows() {
        return terrain.getRows();
    }

    public int getCols() {
        return terrain.getCols();
    }

    public double getCellSize() {
        return terrain.getCellSize();
    }

    public double getHeight(int row, int col) {
        return terrain.getHeight(row, col);
    }

    public List<Ball> getBalls() {
        return balls;
    }

    /** A ball's drawing coordinates, radius and RGB colour. */
    public static final class Ball {
        public final double x;
        public final double y;
        public final double z;
        public final double radius;
        public final int colour;

        private Ball(SceneViewData.BallData ball) {
            x = ball.getX();
            y = ball.getY();
            z = ball.getZ();
            radius = ball.getRadius();
            colour = ball.getColour();
        }
    }
}
