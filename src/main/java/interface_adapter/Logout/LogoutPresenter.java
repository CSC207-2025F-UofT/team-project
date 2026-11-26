package interface_adapter.Logout;

import interface_adapter.ViewManagerModel;
import interface_adapter.LoggedIn.LoggedInState;
import interface_adapter.LoggedIn.LoggedInViewModel;
import interface_adapter.Login.LoginState;
import interface_adapter.Login.LoginViewModel;
import user_case.Logout.LogoutOutputBoundary;
import user_case.Logout.LogoutOutputData;

public class LogoutPresenter implements LogoutOutputBoundary {

        private LoggedInViewModel loggedInViewModel;
        private ViewManagerModel viewManagerModel;
        private LoginViewModel loginViewModel;

        public LogoutPresenter(ViewManagerModel viewManagerModel,
                               LoggedInViewModel loggedInViewModel,
                               LoginViewModel loginViewModel) {
            this.viewManagerModel = viewManagerModel;
            this.loggedInViewModel = loggedInViewModel;
            this.loginViewModel = loginViewModel;
        }
        @Override
        public void PrepareSuccessView(LogoutOutputData response) {
            final LoggedInState loggedInState = loggedInViewModel.getState();
            loggedInState.setUsername("");
            loggedInViewModel.notifyStateChanged();

            final LoginState loginState = loginViewModel.getState();
            loginState.setUsername(response.getUsername());
            loginViewModel.notifyStateChanged();

            this.viewManagerModel.setState(LoginViewModel.VIEW_NAME);
            this.viewManagerModel.notifyViewChanged();
        }
}
