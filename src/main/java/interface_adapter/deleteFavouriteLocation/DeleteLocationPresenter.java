package interface_adapter.deleteFavouriteLocation;

import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.deleteFavouriteLocation.DeleteLocationOutputBoundary;
import use_case.deleteFavouriteLocation.DeleteLocationOutputData;

public class DeleteLocationPresenter implements DeleteLocationOutputBoundary {

    private final WeatherReportPageViewModel viewModel;

    public DeleteLocationPresenter(WeatherReportPageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DeleteLocationOutputData data) {
        // For example, update a list of favourites inside the WeatherReportPageState
        viewModel.getState().setFavouriteLocations(data.getRemainingLocations());
        viewModel.getState().setPopUpMessage("This location was successfully removed!");
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.getState().setPopUpMessage(errorMessage);
        viewModel.firePropertyChanged();
    }
}