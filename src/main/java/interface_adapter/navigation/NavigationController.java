package interface_adapter.navigation;

import interface_adapter.ViewManagerModel;

/**
 * Handles button clicks that only move between screens (no business logic),
 * e.g. "Back", "Sign up?". Keeping these out of the use cases
 * avoids adding empty interactor methods just for navigation.
 */
public class NavigationController {
    private final ViewManagerModel viewManagerModel;

    public NavigationController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void goTo(String viewName) {
        viewManagerModel.switchTo(viewName);
    }
}
