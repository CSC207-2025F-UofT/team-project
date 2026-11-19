package interface_adapter.new_game;

import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudState;
import interface_adapter.hud.HudViewModel;
import org.junit.jupiter.api.Test;
import use_case.new_game.NewGameOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NewGamePresenterTest {

    @Test
    void successViewFillsHudAndSwitchesToGameView() {
        HudViewModel hudViewModel = new HudViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        List<Object> hudEvents = new ArrayList<>();
        hudViewModel.addPropertyChangeListener(evt -> hudEvents.add(evt.getNewValue()));

        NewGamePresenter presenter = new NewGamePresenter(hudViewModel, viewManagerModel, "game");
        presenter.prepareSuccessView(new NewGameOutputData(1, 7, 100, 100, 50, 50));

        HudState state = hudViewModel.getState();
        assertEquals(1, state.getDay());
        assertEquals(7, state.getTotalDays());
        assertEquals(100, state.getEnergy());
        assertEquals(100, state.getSanity());
        assertEquals(50, state.getAcademicSurvival());
        assertEquals(50, state.getMoney());
        assertEquals(1, hudEvents.size());
        assertEquals("game", viewManagerModel.getState());
    }
}
