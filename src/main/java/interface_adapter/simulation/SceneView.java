package interface_adapter.simulation;

import java.nio.file.Path;

/** Operations the presenter can ask the screen to perform. A cancelled chooser returns null. */
public interface SceneView {
    Path chooseSaveFile();

    Path chooseLoadFile();

    boolean confirmOverwrite(String fileName);

    void display(SimulationState state, CameraPose camera);
}
