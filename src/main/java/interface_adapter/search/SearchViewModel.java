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

    // --- setters ---
    public void setResults(List<SearchOutputData.SongResult> results) {
        List<SearchOutputData.SongResult> old = this.results;
        this.results = results;
        support.firePropertyChange(RESULTS_PROPERTY, old, results);
    }

    public void setErrorMessage(String errorMessage) {
        String old = this.errorMessage;
        this.errorMessage = errorMessage;
        support.firePropertyChange(ERROR_PROPERTY, old, errorMessage);
    }

    // --- getters ---
    public List<SearchOutputData.SongResult> getResults() {
        return results;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    // presenter / view register listeners
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
