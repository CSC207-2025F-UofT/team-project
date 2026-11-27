package interface_adapter.viewplayerstats;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import use_case.viewplayerstats.ViewPlayerStatsOutputBoundary;
import use_case.viewplayerstats.ViewPlayerStatsOutputData;

/**
 * Converts use-case results into the statistics page's display state.
 */
public final class ViewPlayerStatsPresenter
        implements ViewPlayerStatsOutputBoundary {

    private final ViewPlayerStatsViewModel viewModel;

    /**
     * Creates a presenter for the statistics page.
     *
     * @param viewModel the model that receives display updates
     */
    public ViewPlayerStatsPresenter(
            ViewPlayerStatsViewModel viewModel) {

        this.viewModel = Objects.requireNonNull(viewModel);
    }

    /**
     * Formats statistics and replaces the current display state.
     *
     * @param outputData the statistics returned by the use case
     */
    @Override
    public void presentSuccess(ViewPlayerStatsOutputData outputData) {
        Objects.requireNonNull(outputData);

        String winRateText = String.format(
                Locale.ROOT,
                "%.0f%%",
                outputData.getWinRate() * 100);

        ViewPlayerStatsState state = new ViewPlayerStatsState(
                outputData.getPlayerId(),
                String.valueOf(outputData.getGamesPlayed()),
                String.valueOf(outputData.getGameWin()),
                winRateText,
                String.valueOf(outputData.getUnoCallsMade()),
                outputData.getAchievements(),
                "");

        viewModel.setStats(state);
    }

    /**
     * Displays a failure message and clears previous statistics.
     *
     * @param message the reason the request failed
     */
    @Override
    public void presentFailure(String message) {
        Objects.requireNonNull(message);

        ViewPlayerStatsState state = new ViewPlayerStatsState(
                "", "", "", "", "", List.of(), message);

        viewModel.setStats(state);
    }
}