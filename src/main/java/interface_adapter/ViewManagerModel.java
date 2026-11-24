package interface_adapter;

/** Its state is the name of the view that should be on screen. */
public final class ViewManagerModel extends ViewModel<String> {
    public ViewManagerModel(String initialView) {
        super("view manager", initialView);
    }
}
