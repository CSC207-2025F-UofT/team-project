package app;
import data_access.TempUserDataAccessObject;
import interface_adaptor.BlankViewModel;
import interface_adaptor.Login.LoginController;
import interface_adaptor.Login.LoginPresenter;
import interface_adaptor.Login.LoginViewModel;
import interface_adaptor.ViewManagerModel;
import log_in.LoginInputBoundary;
import log_in.LoginInteractor;
import log_in.LoginOutputBoundary;
import view.BlankView;
import view.LoginView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // Data Access Object Temp User Data:
    final TempUserDataAccessObject userDataAccessObject = new TempUserDataAccessObject();

    private BlankView blankView;
    private LoginView loginView;
    private BlankViewModel blankViewModel;
    private LoginViewModel loginViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addBlankView(){
        blankViewModel = new BlankViewModel();
        blankView = new BlankView();
        cardPanel.add(blankView, blankView.getViewName());
        return this;
    }
    public AppBuilder addLoginView(){
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }
    public AppBuilder addLoginUseCase(){
        final LoginOutputBoundary loginOutputBoundary = new LoginPresenter(
                blankViewModel, loginViewModel, viewManagerModel);
        final LoginInputBoundary loginInteractor = new LoginInteractor(
                userDataAccessObject, loginOutputBoundary);
        LoginController loginController = new LoginController(loginInteractor);
        loginView.setLoginController(loginController);
        return this;
    }
    public JFrame build() {
        final JFrame application = new JFrame("User Login Example");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(loginView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
}
