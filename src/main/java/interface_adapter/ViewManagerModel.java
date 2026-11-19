package interface_adapter;

/**
 * Tracks which screen (card) is currently showing.
 * Presenters switch screens with:
 *   viewManagerModel.setState("view name"); viewManagerModel.firePropertyChanged();
 */
public class ViewManagerModel extends ViewModel<String> {
    public ViewManagerModel() {
        super("view manager");
        setState("");
    }
}
