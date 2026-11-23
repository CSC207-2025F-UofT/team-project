package interface_adapter.watchlist;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class WatchListViewModel {

    private List<String> stockWatchers = new ArrayList<>();
    private String changesText = "";
    private String errorMessage;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public List<String> getStockWatchers(){
        return stockWatchers;
    }

    public void setStockWatchers(List<String> stockWatchers){
        this.stockWatchers = stockWatchers;
    }

    public String getChangesText(){
        return changesText;
    }

    public void setChangesText(String changesText){
        this.changesText = changesText;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public void firePropertyChanged(){
        support.firePropertyChange("watchlist", null, this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        support.addPropertyChangeListener(listener);
    }

}
