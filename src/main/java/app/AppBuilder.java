package app;

import data_access.FileSceneDataAccess;
import data_access.FileSceneDataAccess.TerrainMapper;
import data_access.InMemorySceneDataAccess;
import entity.Scene;
import interface_adapter.load_scene.LoadSceneController;
import interface_adapter.save_scene.SaveSceneController;
import interface_adapter.simulation.ScenePresenter;
import interface_adapter.simulation.SimulationState;
import interface_adapter.simulation.SimulationViewModel;
import use_case.load_scene.LoadSceneInteractor;
import use_case.save_scene.SaveSceneInteractor;
import use_case.scene.SceneViewData;
import view.ScenePanel;

import javax.swing.JPanel;

public final class AppBuilder {
    private final ScenePanel app;

    public AppBuilder(Scene initialScene, TerrainMapper terrainMapper) {
        InMemorySceneDataAccess scenes = new InMemorySceneDataAccess(initialScene);
        FileSceneDataAccess files = new FileSceneDataAccess(scenes, terrainMapper);
        SceneViewData display = SceneViewData.from(initialScene,
                initialScene.getTerrain() == null ? null : terrainMapper.toHeightGrid(initialScene.getTerrain()));

        SimulationViewModel viewModel = new SimulationViewModel(SimulationState.from(display, ""));
        ScenePresenter presenter = new ScenePresenter(viewModel);
        app = new ScenePanel(presenter);
        SaveSceneController save = new SaveSceneController(new SaveSceneInteractor(files, presenter));
        LoadSceneController load = new LoadSceneController(new LoadSceneInteractor(files, presenter));
        presenter.start(app, save, load);
    }

    public AppBuilder addSimulationView() {
        app.addSimulationView();
        return this;
    }

    public AppBuilder addSaveSceneUseCase() {
        app.addFileControls();
        return this;
    }

    public JPanel build() {
        return app;
    }
}
