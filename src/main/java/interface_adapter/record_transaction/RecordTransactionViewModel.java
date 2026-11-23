package interface_adapter.record_transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Record Transaction View.
 */
public class RecordTransactionViewModel {

    public static final String VIEW_NAME = "record transaction";

    private final List<RecordTransactionViewModelListener> listeners =
            new ArrayList<>();

    private RecordTransactionState state = new RecordTransactionState();

    public RecordTransactionState getState() {
        return state;
    }

    public void setState(RecordTransactionState state) {
        this.state = state;
    }

    public void addListener(RecordTransactionViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (RecordTransactionViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}