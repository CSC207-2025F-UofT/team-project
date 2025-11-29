package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.homepage.LoginChoosePresenter;
import interface_adapter.homepage.LoginChooseViewModel;
import interface_adapter.sign_up.SignupController;
import interface_adapter.sign_up.SignupPresenter;
import interface_adapter.sign_up.SignupState;
import interface_adapter.sign_up.SignupViewModel;
import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginPresenter;
import interface_adapter.user_login.UserLoginViewModel;
import interface_adapter.user_logout.UserLogoutController;
import interface_adapter.user_logout.UserLogoutPresenter;
import service.Backend;
import service.Frontend;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupUserDataAccessInterface;
import use_case.user_login.UserLoginInputBoundary;
import use_case.user_login.UserLoginInteractor;
import use_case.user_login.UserLoginOutputBoundary;
import use_case.user_logout.UserLogoutInputBoundary;
import use_case.user_logout.UserLogoutInteractor;
import use_case.user_logout.UserLogoutOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final UserFactory userFactory = new UserFactory();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewManager viewManager;

    private final FileUserDataAccessObject userDataAccessObject;

    private final Frontend frontend;

    // ViewModels
    private final LoginChooseViewModel loginChooseViewModel = new LoginChooseViewModel();
    private final UserLoginViewModel userLoginViewModel = new UserLoginViewModel();
    private final SignupViewModel signupViewModel= new SignupViewModel();

    // Views
    private LoginChooseView loginChooseView;
    private CreateUserAccountView createUserAccountView;
    private UserLoginView userLoginView;

    public AppBuilder(Frontend frontend, FileUserDataAccessObject dao) {
        this.frontend = frontend;
        this.userDataAccessObject = dao;
        this.viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

        cardPanel.setLayout(cardLayout);
    }

    /** LoginChoose View */
    public AppBuilder addLoginChooseView() {
        loginChooseView = new LoginChooseView(loginChooseViewModel);
        cardPanel.add(loginChooseView, loginChooseView.getViewName());
        return this;
    }

    /** LoginChoose Presenter */
    public AppBuilder addLoginChoosePresenter() {

        LoginChoosePresenter presenter =
                new LoginChoosePresenter(viewManagerModel, userLoginView); // ← 还原

        loginChooseViewModel.addPropertyChangeListener(presenter);

        return this;
    }

    /** CreateUserAccountView */
    public AppBuilder addCreateUserAccountView() {
        createUserAccountView = new CreateUserAccountView(signupViewModel);
        cardPanel.add(createUserAccountView, createUserAccountView.getViewName());
        return this;
    }

    /** UserLogin View */
    public AppBuilder addUserLoginView() {
        userLoginView = new UserLoginView(userLoginViewModel, "user");
        cardPanel.add(userLoginView, userLoginView.getViewName());
        return this;
    }

    /** UserLogin UseCase */
    public AppBuilder addUserLoginUseCase() {

        UserLoginOutputBoundary presenter =
                new UserLoginPresenter(viewManagerModel, userLoginViewModel, frontend);

        UserLoginInputBoundary interactor =
                new UserLoginInteractor(userDataAccessObject, presenter);

        UserLoginController controller = new UserLoginController(interactor);
        userLoginView.setLoginController(controller);

        return this;
    }

    /** Logout UseCase */
    public AppBuilder addUserLogoutUseCase() {

        UserLogoutOutputBoundary presenter =
                new UserLogoutPresenter(viewManagerModel, userLoginViewModel, frontend);

        UserLogoutInputBoundary interactor =
                new UserLogoutInteractor(userDataAccessObject, presenter);

        UserLogoutController controller = new UserLogoutController(interactor);
        frontend.setUserLogoutController(controller);

        return this;
    }

    public AppBuilder addDashboardViews(Backend backend) {

        JPanel admin = new MainDashboardView(frontend, backend, "admin");
        JPanel staff = new MainDashboardView(frontend, backend, "staff");
        JPanel user  = new MainDashboardView(frontend, backend, "user");
        
        cardPanel.add(admin, "adminDashboard");
        cardPanel.add(staff, "staffDashboard");
        cardPanel.add(user, "userDashboard");

        return this;
    }

    public AppBuilder addSignupUseCase() {
        final SignupOutputBoundary signupOutputBoundary = new SignupPresenter(viewManagerModel,
                signupViewModel, userLoginViewModel);
        final SignupInputBoundary userSignupInteractor = new SignupInteractor(
                userDataAccessObject, signupOutputBoundary, userFactory);

        SignupController controller = new SignupController(userSignupInteractor);
        createUserAccountView.setSignupController(controller);
        return this;
    }

    public void build() {
        frontend.setCardPanel(cardPanel);
        frontend.revalidate();
        frontend.repaint();
    }
    
}
