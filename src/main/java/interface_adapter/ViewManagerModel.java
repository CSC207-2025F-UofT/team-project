package interface_adapter;

/**
 * Holds the name of the screen that should be showing.
 * Presenters switch screens with:
 *     viewManagerModel.setState(SomeView.VIEW_NAME);
 *     viewManagerModel.firePropertyChanged();
 */
public class ViewManagerModel extends ViewModel<String> {
    public ViewManagerModel() {
        super("view manager");
        setState("");
    }
}
