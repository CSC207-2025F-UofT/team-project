package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.register.RegisterPresenter;
import interface_adapter.register.RegisterViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.register.RegisterController;
import interface_adapter.register.RegisterPresenter;
import interface_adapter.login.LoginPresenter;

import frameworks_and_drivers.RegisterView;
import frameworks_and_drivers.ViewManager;
import frameworks_and_drivers.LoginView;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.register.RegisterInputBoundary;
import use_case.register.RegisterInteractor;
import use_case.register.RegisterOutputBoundary;
import use_case.login.LoginOutputBoundary;

import javax.swing.*;
import java.awt.*;


public class AppBuilder{
    private final JPanel mainPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(mainPanel, cardLayout, viewManagerModel);


    private LoginView loginView;
    private LoginViewModel loginViewModel;

    private RegisterView registerView;
    private RegisterViewModel registerViewModel;

    public AppBuilder(){
        mainPanel.setLayout(cardLayout);
    }

    public AppBuilder addLoginView(){
        loginViewModel = new LoginViewModel();
        this.loginView = new LoginView(loginViewModel);
        mainPanel.add(loginView, LoginViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addRegisterView(){
        registerViewModel = new RegisterViewModel();
        this.registerView = new RegisterView(registerViewModel);
        mainPanel.add(registerView, RegisterViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addLoginUseCase(){
        final LoginOutputBoundary loginPresenter = new LoginPresenter(viewManagerModel, registerViewModel,  loginViewModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(loginPresenter);

        final LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }

    public AppBuilder addRegisterUseCase(){
        final RegisterOutputBoundary registerPresenter = new RegisterPresenter(registerViewModel);
        final RegisterInputBoundary registerInteractor = new RegisterInteractor(registerPresenter);
        RegisterController registerController = new RegisterController(registerInteractor);
        registerView.setRegisterController(registerController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(mainPanel);

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}