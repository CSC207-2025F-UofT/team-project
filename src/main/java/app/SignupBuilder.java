package app;

import javax.swing.JPanel;

import dataAccess.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.sign_up.SignupController;
import interface_adapter.sign_up.SignupPresenter;
import interface_adapter.sign_up.SignupViewModel;
import interface_adapter.user_login.UserLoginViewModel;
import use_case.signup.*;
import view.auth.CreateUserAccountView;

/**
 * Builds the signup view, presenter, controller and interactor.
 */
public class SignupBuilder {

    private final JPanel cardPanel;
    private final ViewManagerModel viewManagerModel;
    private final FileUserDataAccessObject userDao;

    private final SignupViewModel signupViewModel = new SignupViewModel();
    private final UserLoginViewModel userLoginViewModel = new UserLoginViewModel();
    private CreateUserAccountView createView;

    public SignupBuilder(JPanel cardPanel,
                         ViewManagerModel viewManagerModel,
                         FileUserDataAccessObject userDao) {
        this.cardPanel = cardPanel;
        this.viewManagerModel = viewManagerModel;
        this.userDao = userDao;
    }

    /**
     * Builds signup view, presenter, controller, interactor.
     */
    public void build() {

        // Signup View
        createView = new CreateUserAccountView(signupViewModel);
        cardPanel.add(createView, createView.getViewName());

        // Signup Presenter
        SignupOutputBoundary presenter =
                new SignupPresenter(viewManagerModel, signupViewModel, userLoginViewModel);

        // Signup Interactor
        SignupInputBoundary interactor =
                new SignupInteractor(userDao, presenter, new UserFactory());

        // Signup Controller
        SignupController controller = new SignupController(interactor);

        createView.setSignupController(controller);
    }
}
