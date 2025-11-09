package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeController;
import interface_adapter.home.HomeViewModel;
import view.HomePageView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private HomePageView homePageView;
    private HomeViewModel homeViewModel;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addHomePageView() {
        homeViewModel = new HomeViewModel();
        homePageView = new HomePageView(homeViewModel);
        cardPanel.add(homePageView, homePageView.getViewName());
        return this;
    }


    public AppBuilder addHomeUseCase() {
        final HomeController homeController = new HomeController(homeViewModel);
        homePageView.setHomeController(homeController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Premier League Fantasy App");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(homePageView.getViewName());
        viewManagerModel.firePropertyChange();

        return application;
    }


}
