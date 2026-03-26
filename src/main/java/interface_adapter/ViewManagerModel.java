package interface_adapter;

import view.ViewManager;

public class ViewManagerModel extends ViewModel<String> {
    public ViewManagerModel() {
        super("view manager");
        this.setState("");
    }
}
