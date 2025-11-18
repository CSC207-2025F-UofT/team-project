package test;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.loggedin.LoggedInViewModel;
import use_case.login.LoginInteractor;
import view.LoginView;

import javax.swing.*;

class LoginGUIMain {

    public static void main(String[] args) {

        ViewManagerModel viewManager = new ViewManagerModel();

        LoginViewModel loginViewModel = new LoginViewModel();
        LoggedInViewModel loggedInViewModel = new LoggedInViewModel();

        InMemoryUserDataAccess dao = new InMemoryUserDataAccess();

        LoginPresenter presenter =
                new LoginPresenter(viewManager, loggedInViewModel, loginViewModel);

        LoginInteractor interactor =
                new LoginInteractor(dao, presenter);

        LoginController controller = new LoginController(interactor);

        LoginView loginView = new LoginView(loginViewModel);
        loginView.setLoginController(controller);

        JFrame frame = new JFrame("Login Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(loginView);
        frame.pack();
        frame.setVisible(true);
    }
}
