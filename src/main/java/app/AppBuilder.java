package app;

import entity.UserFactory;
import interface_adapter.*;
import interface_adapter.registration.login.*;
import use_case.registration.login.*;
import view.registration.*;
import data_access.*;
import utility.FontLoader;

import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);

    // DAO version using a shared external database
    // final DBUserDataAccessObject userDataAccessObject = new DBUserDataAccessObject(userFactory);

    //private SignupView signupView;
    //private SignupViewModel signupViewModel;
    private LoginViewModel loginViewModel;
    //private LoggedInViewModel loggedInViewModel;
    //private LoggedInView loggedInView;
    private LoginView loginView;

    public AppBuilder() {
        FontLoader.registerFonts();
        cardPanel.setLayout(cardLayout);

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setResizable(false); // Fixed size window
        frame.setLayout(new BorderLayout(10, 10));
        frame.add(cardPanel, BorderLayout.CENTER);
    }

//    public AppBuilder addSignupView() {
//        signupViewModel = new SignupViewModel();
//        signupView = new SignupView(signupViewModel);
//        cardPanel.add(signupView, signupView.getViewName());
//        return this;
//    }

    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }

//    public AppBuilder addLoggedInView() {
//        loggedInViewModel = new LoggedInViewModel();
//        loggedInView = new LoggedInView(loggedInViewModel);
//        cardPanel.add(loggedInView, loggedInView.getViewName());
//        return this;
//    }

//    public AppBuilder addSignupUseCase() {
//        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
//                signupViewModel, loginViewModel);
//        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
//                userDataAccessObject, signupOutputBoundary, userFactory);
//
//        SignupController controller = new SignupController(userSignupInteractor);
//        signupView.setSignupController(controller);
//        return this;
//    }

    public AppBuilder addLoginUseCase() {
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(viewManagerModel,
                loggedInViewModel, loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);

        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }
//
//    public AppBuilder addChangePasswordUseCase() {
//        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
//                loggedInViewModel);
//
//        final ChangePasswordInputBoundary changePasswordInteractor =
//                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);
//
//        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
//        loggedInView.setChangePasswordController(changePasswordController);
//        return this;
//    }
//
//    /**
//     * Adds the Logout Use Case to the application.
//     * @return this builder
//     */
//    public AppBuilder addLogoutUseCase() {
//        final LogoutOutputBoundary logoutOutputBoundary = new LogoutPresenter(viewManagerModel,
//                loggedInViewModel, loginViewModel);
//
//        final LogoutInputBoundary logoutInteractor =
//                new LogoutInteractor(userDataAccessObject, logoutOutputBoundary);
//
//        final LogoutController logoutController = new LogoutController(logoutInteractor);
//        loggedInView.setLogoutController(logoutController);
//        return this;
//    }

    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
