package interface_adapter.continue_game;

import interface_adapter.ViewManagerModel;
import interface_adapter.rest.ResidenceFormat;
import interface_adapter.rest.RestViewModel;
import use_case.continue_game.ContinueGameOutputBoundary;
import use_case.continue_game.ContinueGameOutputData;

/**
 * Shows the restored game at the residence, or explains on the title screen why it could not be loaded.
 */
public class ContinueGamePresenter implements ContinueGameOutputBoundary {
    private final TitleViewModel titleViewModel;
    private final RestViewModel residenceViewModel;
    private final ViewManagerModel viewManagerModel;

    public ContinueGamePresenter(TitleViewModel titleViewModel, RestViewModel residenceViewModel,
                                 ViewManagerModel viewManagerModel) {
        this.titleViewModel = titleViewModel;
        this.residenceViewModel = residenceViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ContinueGameOutputData data) {
        residenceViewModel.setState(
                ResidenceFormat.day(data.getDayNumber(), data.getDate()),
                data.hasWeather()
                        ? ResidenceFormat.weather(data.getCondition(), data.getHighTemperature(),
                                data.getLowTemperature())
                        : ResidenceFormat.noWeather(),
                ResidenceFormat.energy(data.getEnergy(), data.getMaxEnergy()));
        titleViewModel.setMessage("");
        viewManagerModel.setActiveView(RestViewModel.VIEW_NAME);
    }

    @Override
    public void prepareFailView(String message) {
        titleViewModel.setMessage(message);
    }
}
