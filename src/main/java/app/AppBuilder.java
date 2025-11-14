package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeController;
import interface_adapter.home.HomeViewModel;
import interface_adapter.open_team_entry.OpenTeamEntryController;
import interface_adapter.open_team_entry.OpenTeamEntryPresenter;
import interface_adapter.open_team_entry.OpenTeamEntryViewModel;
import use_case.open_team_entry.OpenTeamEntryInputBoundary;
import use_case.open_team_entry.OpenTeamEntryInteractor;
import view.HomePageView;
import view.TeamEntryView;
import view.ViewManager;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private HomePageView homePageView;
    private HomeViewModel homeViewModel;
    private TeamEntryView teamEntryView;
    private OpenTeamEntryViewModel teamEntryViewModel;
    private OpenTeamEntryController openTeamEntryController;
    private OpenTeamEntryInputBoundary openTeamEntryInputBoundary;


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
        final HomeController homeController = new HomeController(homeViewModel, openTeamEntryController, openTeamEntryInputBoundary);
        homePageView.setHomeController(homeController);
        return this;
    }

    public AppBuilder addTeamEntryView() {
        teamEntryViewModel = new OpenTeamEntryViewModel();
        teamEntryView = new TeamEntryView(teamEntryViewModel);
        cardPanel.add(teamEntryView, teamEntryView.getViewName());
        return this;
    }

    public AppBuilder addOpenTeamEntryViewUseCase() {
        final OpenTeamEntryPresenter presenter =
                new OpenTeamEntryPresenter(viewManagerModel, teamEntryViewModel, homeViewModel);

        final OpenTeamEntryInteractor interactor =
                new OpenTeamEntryInteractor(presenter);

        openTeamEntryController = new OpenTeamEntryController(interactor);

        teamEntryView.setTeamEntryController(openTeamEntryController);
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
