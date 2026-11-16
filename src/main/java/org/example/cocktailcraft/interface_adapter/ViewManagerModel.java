package org.example.cocktailcraft.interface_adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Navigation state only; it does not represent a logged-in user. */
public class ViewManagerModel {
    private final List<ViewManagerModelListener> listeners = new ArrayList<>();
    private String currentViewName = "home";

    public String getCurrentViewName() {
        return currentViewName;
    }

    public void setState(String viewName) {
        currentViewName = Objects.requireNonNull(viewName);
    }

    public void addListener(ViewManagerModelListener listener) {
        listeners.add(Objects.requireNonNull(listener));
    }

    public void notifyViewChanged() {
        for (ViewManagerModelListener listener : new ArrayList<>(listeners)) {
            listener.viewChanged();
        }
    }
}
