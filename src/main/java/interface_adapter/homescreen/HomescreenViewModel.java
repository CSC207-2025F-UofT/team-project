package interface_adapter.homescreen;

import interface_adapter.ViewModel;

public class HomescreenViewModel extends ViewModel<HomescreenState> {

    public HomescreenViewModel() {
        super("homescreen");
        setState(new HomescreenState());
    }
}
