package interface_adapter.find_nearby_quests;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import use_case.find_nearby_quests.FindNearbyQuestsOutputBoundary;
import use_case.find_nearby_quests.FindNearbyQuestsOutputData;
import use_case.find_nearby_quests.NearbyQuest;

/** Turns the use case's results into display text and updates the map's view model. */
public class FindNearbyQuestsPresenter implements FindNearbyQuestsOutputBoundary {
    private static final String THEME_SEPARATOR = ", ";

    private final FindNearbyQuestsViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the view model to update
     */
    public FindNearbyQuestsPresenter(FindNearbyQuestsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(FindNearbyQuestsOutputData outputData) {
        List<FindNearbyQuestsState.PinState> pins = new ArrayList<>();
        for (NearbyQuest quest : outputData.getQuests()) {
            pins.add(new FindNearbyQuestsState.PinState(
                    quest.getQuestId(),
                    quest.getTitle(),
                    String.join(THEME_SEPARATOR, quest.getInterests()),
                    quest.getXp() + " XP",
                    formatDistance(quest.getDistanceKm()),
                    quest.getLatitude(),
                    quest.getLongitude()));
        }

        FindNearbyQuestsState state = viewModel.getState();
        state.setCentre(outputData.getCentreLatitude(), outputData.getCentreLongitude());
        state.setRadiusKm(outputData.getRadiusKm());
        state.setPins(pins);
        state.setErrorMessage(null);
        state.setRetryAvailable(false);
        if (pins.isEmpty()) {
            state.setInfoMessage("No side quests within " + formatRadius(outputData.getRadiusKm())
                    + " km. Try a larger radius.");
        } else {
            state.setInfoMessage(null);
        }
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareLoadFailView(double centreLatitude, double centreLongitude,
                                    String errorMessage) {
        FindNearbyQuestsState state = viewModel.getState();
        state.setCentre(centreLatitude, centreLongitude);
        state.setPins(List.of());
        state.setInfoMessage(null);
        state.setErrorMessage("Couldn't load side quests. " + errorMessage);
        state.setRetryAvailable(true);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareInvalidRadiusView(String errorMessage) {
        FindNearbyQuestsState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        state.setRetryAvailable(false);
        viewModel.firePropertyChanged();
    }

    /**
     * Formats a distance with one decimal place, e.g. "1.3 km".
     *
     * @param distanceKm the distance in kilometres
     * @return the text
     */
    static String formatDistance(double distanceKm) {
        return String.format(Locale.ROOT, "%.1f km", distanceKm);
    }

    /**
     * Formats a radius without trailing zeros, e.g. 2.0 as "2" and 0.5 as "0.5".
     *
     * @param radiusKm the radius in kilometres
     * @return the text
     */
    static String formatRadius(double radiusKm) {
        return BigDecimal.valueOf(radiusKm).stripTrailingZeros().toPlainString();
    }
}
