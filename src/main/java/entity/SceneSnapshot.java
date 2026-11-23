package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class SceneSnapshot {

    private final List<BallState> ballStates;

    private SceneSnapshot(List<BallState> ballStates) {
        this.ballStates = Collections.unmodifiableList(new ArrayList<>(ballStates));
    }

    public static SceneSnapshot capture(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        List<BallState> states = new ArrayList<>();
        for (Ball ball : scene.getBalls()) {
            states.add(new BallState(ball.getPosition(), ball.getVelocity()));
        }
        return new SceneSnapshot(states);
    }

    public void restore(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        if (scene.getBalls().size() != ballStates.size()) {
            throw new IllegalArgumentException("Scene must contain the same number of balls as the snapshot");
        }
        for (int i = 0; i < ballStates.size(); i++) {
            Ball ball = scene.getBalls().get(i);
            BallState state = ballStates.get(i);
            ball.setPosition(state.position);
            ball.setVelocity(state.velocity);
        }
    }

    public int getBallCount() {
        return ballStates.size();
    }

    private static final class BallState {
        private final Vector3D position;
        private final Vector3D velocity;

        private BallState(Vector3D position, Vector3D velocity) {
            this.position = position;
            this.velocity = velocity;
        }
    }
}
