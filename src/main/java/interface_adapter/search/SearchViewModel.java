package interface_adapter.search;

import use_case.search.SearchOutputData;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class SearchViewModel {

    public static final String RESULTS_PROPERTY = "results";
    public static final String ERROR_PROPERTY = "error";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    private List<SearchOutputData.SongResult> results;
    private String errorMessage;

    public void setResults(List<SearchOutputData.SongResult> results) {
        this.results = results;
    }

    public List<SearchOutputData.SongResult> getResults() {
        return results;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(RESULTS_PROPERTY, null, results);
        support.firePropertyChange(ERROR_PROPERTY, null, errorMessage);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
