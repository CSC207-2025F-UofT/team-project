package interface_adapter;

/**
 *  Keeps track of which screen is showing. To switch screens:
 * viewManagerModel.setState(ViewNames.WELCOME);
 * viewManagerModel.firePropertyChanged();
 * */
public class ViewManagerModel extends ViewModel<String{
    /** Starts with no screen chosen. */
    public ViewManagerModel() {
        super("view manager");
        setState("");
    }
}
