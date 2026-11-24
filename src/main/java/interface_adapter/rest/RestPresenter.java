package interface_adapter.rest;

import use_case.rest.RestOutputBoundary;
import use_case.rest.RestOutputData;

/**
 * Formats the new day and the save result for display.
 */
public class RestPresenter implements RestOutputBoundary {
    static final String SAVED = "Progress saved.";
    static final String NOT_SAVED = "Progress was not saved.";

    private final RestViewModel viewModel;

    public RestPresenter(RestViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(RestOutputData data) {
        viewModel.setState(
                ResidenceFormat.day(data.getDayNumber(), data.getDate()),
                ResidenceFormat.weather(data.getCondition(), data.getHighTemperature(), data.getLowTemperature()),
                ResidenceFormat.energy(data.getEnergy(), data.getMaxEnergy()));
        prepareSaveRetryView(data.isSaved());
    }

    @Override
    public void prepareSaveRetryView(boolean saved) {
        viewModel.setSaveStatus(saved ? SAVED : NOT_SAVED, !saved);
    }

    @Override
    public void prepareFailView(String message) {
        viewModel.setSaveStatus(message, false);
    }
}
