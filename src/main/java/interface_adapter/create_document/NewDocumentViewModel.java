package interface_adapter.create_document;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the creating a document View.
 */
public class NewDocumentViewModel {

    public static final String TITLE_LABEL = "New Document View";

    private final List<CreationViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "new document";

    private NewDocumentState state = new NewDocumentState();

    public NewDocumentState getState() {
        return this.state;
    }

    public void setState(NewDocumentState state) {
        this.state = state;
    }

    public void addListener(CreationViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (CreationViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}