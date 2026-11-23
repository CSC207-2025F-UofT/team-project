package use_case.scene;

import entity.Ball;
import entity.ObjectPreset;
import entity.Scene;

/** Checks the physical values shared by the save and load use cases. */
public final class SceneFileValidator {
    private SceneFileValidator() {
    }

    public static void checkBallPresets(Scene scene) throws SceneFileException {
        for (Ball ball : scene.getBalls()) {
            ObjectPreset preset = ball.getPreset();
            if (preset == null || preset.getName() == null
                    || !positive(preset.getRadius()) || !positive(preset.getMass())
                    || !Double.isFinite(preset.getFriction()) || preset.getFriction() < 0
                    || !(preset.getBounciness() >= 0 && preset.getBounciness() <= 1)) {
                throw new SceneFileException("A ball in the scene has an invalid preset.");
            }
        }
    }

    private static boolean positive(double value) {
        return Double.isFinite(value) && value > 0;
    }
}
