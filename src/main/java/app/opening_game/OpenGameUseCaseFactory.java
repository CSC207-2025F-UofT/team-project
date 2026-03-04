package app.opening_game;

import data_access.OpenGameFileDataAccess;
import interface_adapter.ViewManagerModel;
import interface_adapter.opengame.OpenGameController;
import interface_adapter.opengame.OpenGamePresenter;
import interface_adapter.opengame.OpenGameViewModel;
import use_case.openGame.OpenGameDataAccessInterface;
import use_case.openGame.OpenGameInputBoundary;
import use_case.openGame.OpenGameInteractor;
import use_case.openGame.OpenGameOutputBoundary;
import use_case.openGame.ScreenSwitchBoundary;
import view.OpenGameView;

public class OpenGameUseCaseFactory {

    public static OpenGameView create(
            ScreenSwitchBoundary switcher,
            ViewManagerModel viewManagerModel   // <-- FIX: we now require this
    ) {

        // 1. Create ViewModel
        OpenGameViewModel viewModel = new OpenGameViewModel();

        // 2. Create Presenter
        OpenGameOutputBoundary presenter =
                new OpenGamePresenter(viewModel, viewManagerModel);

        // 3. Create Data Access
        OpenGameDataAccessInterface dataAccess =
                new OpenGameFileDataAccess("game_save.json");

        // 4. Create Interactor (UPDATED)
        OpenGameInputBoundary interactor =
                new OpenGameInteractor(presenter, dataAccess, switcher);

        // 5. Create Controller
        OpenGameController controller = new OpenGameController(interactor);

        // 6. Create View (Swing UI)
        return new OpenGameView(controller, viewModel);
    }
}
