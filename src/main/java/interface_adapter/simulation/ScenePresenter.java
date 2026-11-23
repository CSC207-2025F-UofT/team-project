package interface_adapter.simulation;

import interface_adapter.load_scene.LoadSceneController;
import interface_adapter.save_scene.SaveSceneController;
import use_case.load_scene.LoadSceneOutputBoundary;
import use_case.load_scene.LoadSceneOutputData;
import use_case.save_scene.SaveSceneOutputBoundary;
import use_case.save_scene.SaveSceneOutputData;

import java.nio.file.Path;
import java.util.Locale;

/** Handles user input and receives the save/load model's callbacks through its output boundaries. */
public final class ScenePresenter implements SaveSceneOutputBoundary, LoadSceneOutputBoundary,
        SimulationViewModel.Listener {
    private final SimulationViewModel viewModel;
    private SceneView view;
    private SaveSceneController save;
    private LoadSceneController load;
    private int previousMouseX;

    public ScenePresenter(SimulationViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /** Completes the two-way connections before the Swing window opens. */
    public void start(SceneView view, SaveSceneController save, LoadSceneController load) {
        this.view = view;
        this.save = save;
        this.load = load;
        viewModel.setListener(this);
    }

    public void saveRequested() {
        Path path = view.chooseSaveFile();
        if (path != null && path.getFileName() != null) {
            String name = path.getFileName().toString();
            if (!name.toLowerCase(Locale.ROOT).endsWith(".json")) {
                path = path.resolveSibling(name + ".json");
            }
            save.execute(path);
        }
    }

    public void loadRequested() {
        Path path = view.chooseLoadFile();
        if (path != null) {
            load.execute(path);
        }
    }

    public void moveCamera(int forward, int right, int up) {
        SimulationState state = viewModel.getState();
        if (state.hasTerrain()) {
            double step = (Math.max(state.getRows(), state.getCols()) - 1) * state.getCellSize() / 40;
            viewModel.setCamera(viewModel.getCamera().move(forward * step, right * step, up * step));
        }
    }

    public void beginRotation(int mouseX) {
        previousMouseX = mouseX;
    }

    public void dragTo(int mouseX) {
        double radians = (mouseX - previousMouseX) * 0.01;
        previousMouseX = mouseX;
        viewModel.setCamera(viewModel.getCamera().rotate(radians));
    }

    @Override
    public void prepareSuccessView(SaveSceneOutputData output) {
        viewModel.setFileStatus("Saved " + output.getFileName());
    }

    @Override
    public void prepareOverwriteView(SaveSceneOutputData output) {
        if (view.confirmOverwrite(output.getFileName())) {
            save.execute(Path.of(output.getFilePath()), true);
        }
    }

    @Override
    public void prepareSuccessView(LoadSceneOutputData output) {
        viewModel.setState(SimulationState.from(output.getScene(), "Loaded " + output.getFileName()));
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setFileStatus(error);
    }

    @Override
    public void displayChanged(SimulationState state, CameraPose camera) {
        view.display(state, camera);
    }
}
