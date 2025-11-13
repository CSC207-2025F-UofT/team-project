package interface_adapter.blank;

import interface_adapter.ViewModel;

public class BlankViewModel extends ViewModel<Object> {
    public BlankViewModel() {
        super("blank");  // view name = "blank"
        setState(null);
    }
}