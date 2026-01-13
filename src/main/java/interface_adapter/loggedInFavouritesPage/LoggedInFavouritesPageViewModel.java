package interface_adapter.loggedInFavouritesPage;

import interface_adapter.ViewModel;

public class LoggedInFavouritesPageViewModel extends ViewModel<LoggedInFavouritesPageState> {
    public static final String TITLE_LABEL = "My Favourites";
    public static final String GO_BACK_LABEL = "Go Back";

    public LoggedInFavouritesPageViewModel() {
        super("Logged In Favourites View");
        setState(new LoggedInFavouritesPageState());
    }
}
