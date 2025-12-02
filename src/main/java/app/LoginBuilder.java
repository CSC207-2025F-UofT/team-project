package app;

import javax.swing.JPanel;

import dataAccess.FileUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.LoginChoosePresenter;
import interface_adapter.homepage.LoginChooseViewModel;
import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginPresenter;
import interface_adapter.user_login.UserLoginViewModel;
import service.Frontend;
import use_case.user_login.*;
import view.auth.LoginChooseView;
import view.auth.UserLoginView;

/**
 * Builds all login-related views, view models, presenters,
 * controllers, and interactors.
 */
public class LoginBuilder {

    private final JPanel cardPanel;
    private final Frontend frontend;
    private final FileUserDataAccessObject userDao;
    private final ViewManagerModel viewManagerModel;

    private final LoginChooseViewModel loginChooseViewModel = new LoginChooseViewModel();
    private final UserLoginViewModel userLoginViewModel = new UserLoginViewModel();

    public LoginBuilder(JPanel cardPanel,
                        ViewManagerModel viewManagerModel,
                        Frontend frontend,
                        FileUserDataAccessObject userDao) {
        this.cardPanel = cardPanel;
        this.frontend = frontend;
        this.userDao = userDao;
        this.viewManagerModel = viewManagerModel;
    }

    /**
     * Builds login choose view, user login view, presenter, controller, interactor.
     */
    public void build() {

        // LoginChoose view
        LoginChooseView chooseView = new LoginChooseView(loginChooseViewModel);
        cardPanel.add(chooseView, chooseView.getViewName());

        // UserLogin view
        UserLoginView loginView = new UserLoginView(userLoginViewModel);
        cardPanel.add(loginView, loginView.getViewName());

        // Presenter for choose
//        LoginChoosePresenter choosePresenter =
//                new LoginChoosePresenter(viewManagerModel, loginView);
        LoginChoosePresenter choosePresenter =
                new LoginChoosePresenter(viewManagerModel, userLoginViewModel);
        loginChooseViewModel.addPropertyChangeListener(choosePresenter);

        // Login Presenter
        UserLoginOutputBoundary presenter =
                new UserLoginPresenter(viewManagerModel, userLoginViewModel, frontend, null);

        // Login Interactor
        UserLoginInputBoundary interactor =
                new UserLoginInteractor(userDao, presenter, null);

        // Login Controller
        UserLoginController controller = new UserLoginController(interactor);
        loginView.setLoginController(controller);
    }
}
