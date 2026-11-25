package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.register.RegisterViewModel;
import view.RegisterView;
import view.ViewManager;
import view.LoginView;
import interface_adapter.login.LoginViewModel;

import javax.swing.*;
import java.awt.*;


public class AppBuilder{
    private final JPanel mainPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(mainPanel, cardLayout, viewManagerModel);


    private LoginView loginView;
    private LoginViewModel loginViewModel;

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
        RegisterViewModel registerViewModel = new RegisterViewModel();
        RegisterView registerView = new RegisterView(registerViewModel);
        mainPanel.add(registerView, RegisterViewModel.VIEW_NAME);
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