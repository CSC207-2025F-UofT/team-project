package interface_adapter.home;

import interface_adapter.ViewModel;
import use_case.account.AccountProfile;

public final class HomeViewModel extends ViewModel<HomeState> {
    public static final String VIEW_NAME = "Home";

    public HomeViewModel() {
        super(VIEW_NAME, HomeState.empty());
    }

    /** Shared by the presenters that sign a user in. */
    public void showSignedIn(AccountProfile profile) {
        setState(new HomeState("Signed in as " + profile.displayName(), "@" + profile.username()));
    }
}
