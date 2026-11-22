package interface_adapter.Search;

import interface_adapter.ViewManagerModel;
//import interface_adapter.Search.LoggedInState;
import interface_adapter.Search.SearchViewModel;
import user_case.Search.SearchOutputBoundary;
import user_case.Search.SearchOutputData;

/**
 * The Presenter for the Search Use Case.
 */
public abstract class SearchPresenter implements SearchOutputBoundary {

//    private final SearchViewModel searchViewModel;
//    private final SearchInViewModel loggedInViewModel;
//    private final ViewManagerModel viewManagerModel;
//
//    public LoginPresenter(ViewManagerModel viewManagerModel,
//                          LoggedInViewModel loggedInViewModel,
//                          LoginViewModel loginViewModel) {
//        this.viewManagerModel = viewManagerModel;
//        this.loggedInViewModel = loggedInViewModel;
//        this.loginViewModel = loginViewModel;
//    }
//
//    @Override
//    public void prepareSuccessView(LoginOutputData response) {
//        // On success, update the loggedInViewModel's state
//        final LoggedInState loggedInState = loggedInViewModel.getState();
//        loggedInState.setUsername(response.getUsername());
//        this.loggedInViewModel.notifyStateChanged();
//
//        // and clear everything from the LoginViewModel's state
//        this.loginViewModel.setState(new LoginState());
//        this.loginViewModel.notifyStateChanged();
//
//        // switch to the logged in view
//        this.viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
//        this.viewManagerModel.notifyViewChanged();
//    }
//
//    @Override
//    public void prepareFailView(String error) {
//        final LoginState loginState = loginViewModel.getState();
//        loginState.setLoginError(error);
//        loginViewModel.notifyStateChanged();
//    }
}
