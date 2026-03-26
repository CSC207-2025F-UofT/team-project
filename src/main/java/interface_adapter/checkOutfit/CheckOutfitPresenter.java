package interface_adapter.checkOutfit;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.checkOutfit.CheckOutfitOutputBoundary;
import use_case.checkOutfit.CheckOutfitOutputData;

public class CheckOutfitPresenter implements CheckOutfitOutputBoundary {
    private final CheckOutfitViewModel viewModel;
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final ViewManagerModel  viewManagerModel;

    public CheckOutfitPresenter(CheckOutfitViewModel viewModel,
                                WeatherReportPageViewModel weatherReportPageViewModel,
                                ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(CheckOutfitOutputData outputData) {
        CheckOutfitState s = viewModel.getState();
        s.setPopUpMessage(outputData.getPopUpMessage());
        viewModel.firePropertyChanged("state");

        // optional: clear message so it won't re-pop on later state updates
        s.setPopUpMessage("");
    }

    @Override
    public void prepareFailView(String error) {
        CheckOutfitState s = viewModel.getState();
        s.setPopUpMessage(error);
        viewModel.firePropertyChanged("state");
        s.setPopUpMessage("");
    }

    @Override
    public void goBack() {
        viewManagerModel.setState(weatherReportPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
