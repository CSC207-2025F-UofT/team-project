package interface_adapter.map;

import use_case.load_map.LoadMapOutputBoundary;
import use_case.load_map.LoadMapOutputData;

/** Converts map-loading results into display state and notifies the view. */
public final class MapPresenter implements LoadMapOutputBoundary {
    private final MapViewModel viewModel;

    public MapPresenter(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoadMapOutputData outputData) {
        viewModel.setState(new MapState(outputData.getTileIds(), outputData.getTileSize(), ""));
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        MapState previous = viewModel.getState();
        viewModel.setState(new MapState(previous.getTileIds(), previous.getTileSize(),
                "Unable to load map: " + error));
        viewModel.firePropertyChanged();
    }
}
