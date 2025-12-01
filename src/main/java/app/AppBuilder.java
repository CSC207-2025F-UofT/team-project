package app;

import data_access.FileUserDataAccessObject;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.homepage.HomepageViewModel;
import interface_adapter.homepage.LoginChooseViewModel;
//import interface_adapter.logged_in.ChangePasswordController;
//import interface_adapter.logged_in.ChangePasswordPresenter;
//import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.user_login.UserLoginController;
import interface_adapter.user_login.UserLoginPresenter;
import interface_adapter.user_login.UserLoginViewModel;
//import use_case.change_password.ChangePasswordInputBoundary;
//import use_case.change_password.ChangePasswordInteractor;
//import use_case.change_password.ChangePasswordOutputBoundary;
import use_case.user_login.UserLoginInputBoundary;
import use_case.user_login.UserLoginInteractor;
import use_case.user_login.UserLoginOutputBoundary;
import view.HomepageView;
import view.LoginChooseView;
//import view.LoggedInView;
import view.UserLoginView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final UserFactory userFactory = new UserFactory();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    /** add start 20251124 **/
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);
    /** add end 20251124 **/

    final FileUserDataAccessObject userDataAccessObject = new FileUserDataAccessObject("users.csv", userFactory);


    private UserLoginViewModel userLoginViewModel;
    private UserLoginView userLoginView;
    /** add start 20251124 **/
    private HomepageViewModel homepageViewModel;
    private HomepageView homepageView;
    private LoginChooseViewModel loginChooseViewModel;
    private LoginChooseView loginChooseView;
    /** add end 20251124 **/

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginChooseView() {
    	loginChooseViewModel = new LoginChooseViewModel();
    	loginChooseView = new LoginChooseView(loginChooseViewModel);
        cardPanel.add(loginChooseView, loginChooseView.getViewName());
        return this;
    }
    
    public AppBuilder addHomepageView() {
    	homepageViewModel = new HomepageViewModel();
        homepageView = new HomepageView(homepageViewModel);
        cardPanel.add(homepageView, homepageView.getViewName());
        return this;
    }
    
    public AppBuilder addUserLoginView() {
        userLoginViewModel = new UserLoginViewModel();
        userLoginView = new UserLoginView(userLoginViewModel);
        cardPanel.add(userLoginView, userLoginView.getViewName());
        return this;
    }

    public AppBuilder addUserLoginUseCase() {
        final UserLoginOutputBoundary userLoginOutputBoundary = new UserLoginPresenter(viewManagerModel,
                homepageViewModel, userLoginViewModel);
        final UserLoginInputBoundary userloginInteractor = new UserLoginInteractor(
                userDataAccessObject, userLoginOutputBoundary);

        UserLoginController loginController = new UserLoginController(userloginInteractor);
        userLoginView.setLoginController(loginController);
        return this;
    }
    
//    public AppBuilder addChangePasswordUseCase() {
//        final ChangePasswordOutputBoundary changePasswordOutputBoundary = new ChangePasswordPresenter(viewManagerModel,
//        		homepageViewModel);
//
//        final ChangePasswordInputBoundary changePasswordInteractor =
//                new ChangePasswordInteractor(userDataAccessObject, changePasswordOutputBoundary, userFactory);
//
//        ChangePasswordController changePasswordController = new ChangePasswordController(changePasswordInteractor);
//        homepageView.setChangePasswordController(changePasswordController);
//        return this;
//    }
    
    public JFrame build() {
//        final JFrame application = new JFrame("User Login");
        final JFrame application = new JFrame("loginChoose");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }

}
