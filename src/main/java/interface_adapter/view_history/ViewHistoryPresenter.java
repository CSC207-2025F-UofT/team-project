package interface_adapter.view_history;

import use_case.view_history.ViewHistoryOutputBoundary;
import use_case.view_history.ViewHistoryOutputData;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Turns the View History result into text for the history screen,
 * e.g. "Today", "10:00 AM", "I want water.".
 */
public class ViewHistoryPresenter implements ViewHistoryOutputBoundary {

    private static final DateTimeFormatter TIME_FORMAT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE, MMM d", Locale.ENGLISH);

    private final ViewHistoryViewModel viewModel;
    private final Clock clock;

    public ViewHistoryPresenter(ViewHistoryViewModel viewModel) {
        this(viewModel, Clock.systemDefaultZone());
    }

    /**
     * Lets tests choose what "today" is.
     */
    public ViewHistoryPresenter(ViewHistoryViewModel viewModel, Clock clock) {
        this.viewModel = viewModel;
        this.clock = clock;
    }

    @Override
    public void prepareSuccessView(ViewHistoryOutputData outputData) {
        LocalDate today = LocalDate.now(clock);

        List<ViewHistoryState.HistoryRow> rows = new ArrayList<>();
        for (ViewHistoryOutputData.Entry entry : outputData.getEntries()) {
            LocalDateTime time = entry.getTimestamp();
            rows.add(new ViewHistoryState.HistoryRow(
                    dayLabel(time.toLocalDate(), today),
                    time.format(TIME_FORMAT),
                    entry.getText()));
        }

        ViewHistoryState state = viewModel.getState();
        state.setRows(rows);
        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        ViewHistoryState state = viewModel.getState();
        state.setRows(new ArrayList<>());
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }

    private static String dayLabel(LocalDate date, LocalDate today) {
        if (date.equals(today)) {
            return "Today";
        }
        if (date.equals(today.minusDays(1))) {
            return "Yesterday";
        }
        return date.format(DATE_FORMAT);
    }
}