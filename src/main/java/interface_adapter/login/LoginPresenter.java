package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

/**
 * Presenter for the Login use case.
 */
public class LoginPresenter implements LoginOutputBoundary{
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;
    private final String successViewName;

    public LoginPresenter(LoginViewModel loginViewModel, ViewManagerModel viewManagerModel, String successViewName){
        this.loginViewModel = loginViewModel;
        this.viewManagerModel = viewManagerModel;
        this.successViewName = successViewName;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData){
        LoginViewModel.LoginState state = loginViewModel.getState();

        state.setUsername(outputData.getUsername());
        state.setErrorMessage("");

        loginViewModel.setState(state);
        loginViewModel.firePropertyChanged();

        viewManagerModel.setState(successViewName);
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage){
        LoginViewModel.LoginState state = loginViewModel.getState();

        state.setErrorMessage(errorMessage);

        loginViewModel.setState(state);
        loginViewModel.firePropertyChanged();
    }
}
