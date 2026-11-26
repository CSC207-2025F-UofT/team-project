package use_case.view_history;

import entity.CommunicationMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Loads the child's saved messages and passes them to the presenter
 * in chronological order (oldest first).
 */
public class ViewHistoryInteractor implements ViewHistoryInputBoundary {

    private final ViewHistoryDataAccessInterface dataAccess;
    private final ViewHistoryOutputBoundary presenter;

    public ViewHistoryInteractor(ViewHistoryDataAccessInterface dataAccess,
                                 ViewHistoryOutputBoundary presenter) {
        this.dataAccess = Objects.requireNonNull(dataAccess, "dataAccess cannot be null");
        this.presenter = Objects.requireNonNull(presenter, "presenter cannot be null");
    }

    @Override
    public void execute() {
        // Sort here so UC4 does not depend on the order the data access returns.
        List<CommunicationMessage> history = new ArrayList<>(dataAccess.getHistory());
        history.sort(Comparator.comparing(CommunicationMessage::getTimestamp));

        // An empty history is a normal result (an empty list), not a failure.
        List<ViewHistoryOutputData.Entry> entries = new ArrayList<>();
        for (CommunicationMessage message : history) {
            entries.add(new ViewHistoryOutputData.Entry(
                    message.getText(), message.getTimestamp()));
        }

        presenter.prepareSuccessView(new ViewHistoryOutputData(entries));
    }
}