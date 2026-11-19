package interface_adapter.new_game;

import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;
import use_case.new_game.NewGameOutputBoundary;
import use_case.new_game.NewGameOutputData;

/**
 * Shows a freshly started game: fills the HUD with the starting stats and
 * switches to the game screen.
 */
public class NewGamePresenter implements NewGameOutputBoundary {

    private final HudViewModel hudViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String gameViewName;

    /**
     * @param gameViewName the card name of the game screen, injected by
     *                     AppBuilder so this presenter does not depend on the view layer
     */
    public NewGamePresenter(HudViewModel hudViewModel, ViewManagerModel viewManagerModel,
                            String gameViewName) {
        this.hudViewModel = hudViewModel;
        this.viewManagerModel = viewManagerModel;
        this.gameViewName = gameViewName;
    }

    @Override
    public void prepareSuccessView(NewGameOutputData outputData) {
        HudState state = new HudState();
        state.setDay(outputData.getDay());
        state.setTotalDays(outputData.getTotalDays());
        state.setEnergy(outputData.getEnergy());
        state.setSanity(outputData.getSanity());
        state.setAcademicSurvival(outputData.getAcademicSurvival());
        state.setMoney(outputData.getMoney());
        hudViewModel.setState(state);
        hudViewModel.firePropertyChanged();

        viewManagerModel.setState(gameViewName);
        viewManagerModel.firePropertyChanged();
    }
}
