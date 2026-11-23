package interface_adapter;

/**
 * Holds the name of the view currently on screen. Presenters set it to
 * switch screens; the ViewManager listens and flips its CardLayout.
 */
public class ViewManagerModel extends ViewModel<String> {

    public ViewManagerModel() {
        super("view manager");
        setState("");
    }
}
