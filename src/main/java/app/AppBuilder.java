package app;

import data_access.DBUserDataAccessObject;

import interface_adapter.Submit.SubmitController;
import interface_adapter.Submit.SubmitPresenter;
import interface_adapter.Submit.SubmitViewModel;
import interface_adapter.ViewManagerModel;

import usecase.Submit.SubmitInputBoundary;
import usecase.Submit.SubmitInteractor;
import usecase.Submit.SubmitOutputBoundary;

import view.SubmitView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    // TODO: UserFactory
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    // TODO: Add instance variables

    private SubmitView submitView;
    private SubmitViewModel submitViewModel;
    private final DBUserDataAccessObject userDataAccessObject =  new DBUserDataAccessObject();

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addSubmitView() {
        submitViewModel = new SubmitViewModel();
        submitView = new SubmitView(submitViewModel);
        cardPanel.add(submitView, submitView.getViewName());
        return this;
    }
    public AppBuilder addSubmitUseCase() {
        final SubmitOutputBoundary submitOutputBoundary = new SubmitPresenter(submitViewModel);
        final SubmitInputBoundary SubmitInteractor = new SubmitInteractor(
                userDataAccessObject, submitOutputBoundary);

        SubmitController submitController = new SubmitController(SubmitInteractor);
        submitView.setSubmitController(submitController);
        return this;
    }
    // TODO: Implement builder methods

    public JFrame build() {
        final JFrame application = new JFrame("This is a title");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(submitView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }
    // Here's an example method from the CA Lab:
    /*
    public AppBuilder addLoginView() {
        loginViewModel = new LoginViewModel();
        loginView = new LoginView(loginViewModel);
        cardPanel.add(loginView, loginView.getViewName());
        return this;
    }
     */
}
