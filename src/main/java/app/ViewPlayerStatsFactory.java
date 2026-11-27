package app;

import interface_adapter.viewplayerstats.ViewPlayerStatsController;
import interface_adapter.viewplayerstats.ViewPlayerStatsPresenter;
import interface_adapter.viewplayerstats.ViewPlayerStatsViewModel;

import use_case.viewplayerstats.ViewPlayerStatsDataAccessInterface;
import use_case.viewplayerstats.ViewPlayerStatsInteractor;
import use_case.viewplayerstats.ViewPlayerStatsSessionInterface;

import view.ViewPlayerStatsView;

/**
 * Assembles the components required by the player statistics screen.
 */
public final class ViewPlayerStatsFactory {

    private ViewPlayerStatsFactory() {
        // This class is used through its static create method.
    }

    /**
     * Creates a statistics screen using the supplied adapters.
     *
     * @param dataAccess provides stored player statistics
     * @param session    provides the current player's identity
     * @return the assembled statistics screen
     */
    public static ViewPlayerStatsView create(
            ViewPlayerStatsDataAccessInterface dataAccess,
            ViewPlayerStatsSessionInterface session) {

        ViewPlayerStatsViewModel viewModel =
                new ViewPlayerStatsViewModel();

        ViewPlayerStatsPresenter presenter =
                new ViewPlayerStatsPresenter(viewModel);

        ViewPlayerStatsInteractor interactor =
                new ViewPlayerStatsInteractor(
                        dataAccess,
                        session,
                        presenter);

        ViewPlayerStatsController controller =
                new ViewPlayerStatsController(interactor);

        return new ViewPlayerStatsView(viewModel, controller);
    }
}