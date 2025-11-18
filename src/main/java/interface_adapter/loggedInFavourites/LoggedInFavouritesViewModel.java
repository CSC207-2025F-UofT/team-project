package interface_adapter.loggedInFavourites;

import interface_adapter.ViewModel;

public class LoggedInFavouritesViewModel extends ViewModel<LoggedInFavouritesState> {
    public LoggedInFavouritesViewModel() {
        super("Logged In Favourites View");
        setState(new LoggedInFavouritesState());
    }

}
