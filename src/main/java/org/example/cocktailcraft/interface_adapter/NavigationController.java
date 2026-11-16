package org.example.cocktailcraft.interface_adapter;

/** Preview navigation only. Owners must add authentication and selection flows. */
public class NavigationController {
    private final ViewManagerModel model;

    public NavigationController(ViewManagerModel model) {
        this.model = model;
    }

    public void show(String viewName) {
        model.setState(viewName);
        model.notifyViewChanged();
    }
}
