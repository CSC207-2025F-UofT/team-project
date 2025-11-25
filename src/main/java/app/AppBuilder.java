//package app;
//
//import data_access.FileUserDataAccessObject;
//import entity.UserFactory;
//import interface_adapter.ViewManagerModel;
//import interface_adapter.user_login.UserLoginViewModel;
//import view.UserLoginView;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class AppBuilder {
//    private final JPanel cardPanel = new JPanel();
//    private final CardLayout cardLayout = new CardLayout();
//    final UserFactory userFactory = new UserFactory();
//    final ViewManagerModel viewManagerModel = new ViewManagerModel();
//
//    private UserLoginViewModel userLoginViewModel;
//    private UserLoginView userLoginView;
//
//    public AppBuilder() {
//        cardPanel.setLayout(cardLayout);
//    }
//
//    public AppBuilder addUserLoginView() {
//        userLoginViewModel = new UserLoginViewModel();
//        userLoginView = new UserLoginView(userLoginViewModel);
//        cardPanel.add(userLoginView, userLoginView.getViewName());
//        return this;
//    }
//
///*
//    public AppBuilder addUserLoginUseCase() {
//        final UserLoginOutputBoundary loginOutputBoundary = new UserLoginPresenter(viewManagerModel,
//                loggedInViewModel, loginViewModel);
//        final LoginInputBoundary loginInteractor = new LoginInteractor(
//                userDataAccessObject, loginOutputBoundary);
//
//        LoginController loginController = new LoginController(loginInteractor);
//        loginView.setLoginController(loginController);
//        return this;
//    }
//*/
//
//    public JFrame build() {
//        final JFrame application = new JFrame("User Login");
//        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//        application.add(cardPanel);
//
//        return application;
//    }
//
//}
