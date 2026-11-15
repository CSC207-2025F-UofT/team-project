package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.signup.LoginSignupViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import view.LoginSignupView;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class AppBuilder {
    final UserFactory userFactory = new UserFactory();

    // set which data access implementation to use, can be any
    // of the classes from the data_access package

    // DAO version using local file storage
    final FileUserDataAccessObject userDataAccessObject =
            new FileUserDataAccessObject("users.csv", userFactory);

    private LoginSignupView loginSignupView;
    private LoginSignupViewModel loginSignupViewModel;

    public AppBuilder addLoginSignupView() {
        loginSignupViewModel = new LoginSignupViewModel();
        loginSignupView = new LoginSignupView(loginSignupViewModel);
        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(loginSignupViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        final SignupController controller = new SignupController(userSignupInteractor);
        loginSignupView.setSignupController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Login / Sign Up");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(loginSignupView);

        return application;
    }
}