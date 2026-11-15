package interface_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for the View Manager. Its state is the name of the View which
 * is currently active. An initial state of "" is used.
 */
public class ViewManagerModel {

    private final List<ViewManagerModelListener> listeners = new ArrayList<>();
    private String state = "";

    public String getState(){
        return this.state;
    }

    public void setState(String state){
        this.state = state;
    }

    public void addListener(ViewManagerModelListener listener){
        listeners.add(listener);
    }

    public void notifyViewChanged(){
        for (ViewManagerModelListener listener : listeners) {
            listener.viewChanged();
        }
    }
}
