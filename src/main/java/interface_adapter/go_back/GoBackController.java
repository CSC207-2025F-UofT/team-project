package interface_adapter.go_back;

import interface_adapter.ViewManagerModel;

// This controller is simple: it just tells the ViewManager to switch views.
public class GoBackController {

    private final ViewManagerModel viewManagerModel;

    public GoBackController(ViewManagerModel viewManagerModel) {
        this.viewManagerModel = viewManagerModel;
    }

    public void execute(String viewName) {
        viewManagerModel.setState(viewName);
        viewManagerModel.firePropertyChange();
    }
}
