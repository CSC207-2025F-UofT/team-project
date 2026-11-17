package main.interface_adapters;

import java.util.ArrayList;
import java.util.List;

public class ViewManagerModel {

    private String activeView;
    private final List<ViewManagerModelListener> listeners = new ArrayList<>();

    public String getActiveView() {return this.activeView;}

    public void setActiveView(String newView) { this.activeView = newView;}

    public void addListener(ViewManagerModelListener listener) {
        this.listeners.add(listener);
    }

    public void notifyViewChanged() {
        for (ViewManagerModelListener listener : listeners) {
            listener.viewChanged();
        }
    }
}
