package interface_adapter.simulation;

import java.nio.file.Path;

/** Operations the presenter can ask the screen to perform. A cancelled chooser returns null. */
public interface SceneView {
    Path chooseSaveFile();

    Path chooseLoadFile();

    boolean confirmOverwrite(String fileName);

    void display(SimulationState state, CameraPose camera);

    /** Starts calling the presenter's clockTicked about 60 times a second. */
    void startClock();

    void stopClock();
}
