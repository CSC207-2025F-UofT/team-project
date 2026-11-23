package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Scene {

    private Terrain terrain;
    private final List<Ball> balls;
    private double gravity;
    private double simSpeed;

    public Scene() {
        this(null);
    }

    public Scene(Terrain terrain) {
        this.terrain = terrain;
        this.balls = new ArrayList<>();
        this.gravity = 9.81;
        this.simSpeed = 1.0;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public double getGravity() {
        return gravity;
    }

    public void setGravity(double gravity) {
        this.gravity = gravity;
    }

    public double getSimSpeed() {
        return simSpeed;
    }

    public void setSimSpeed(double simSpeed) {
        this.simSpeed = simSpeed;
    }

    public void addBall(Ball ball) {
        balls.add(Objects.requireNonNull(ball, "ball"));
    }

    /** Removes the first matching ball; returns whether a ball was removed. */
    public boolean removeBall(Ball ball) {
        return balls.remove(ball);
    }

    /** Returns an unmodifiable view of the balls in this scene. */
    public List<Ball> getBalls() {
        return Collections.unmodifiableList(balls);
    }

    /** Creates an independent scene with copied ball state. */
    public Scene copy() {
        Scene copy = new Scene(terrain);
        copy.gravity = gravity;
        copy.simSpeed = simSpeed;
        for (Ball ball : balls) {
            copy.addBall(new Ball(ball.getPosition(), ball.getVelocity(), ball.getPreset()));
        }
        return copy;
    }
}
