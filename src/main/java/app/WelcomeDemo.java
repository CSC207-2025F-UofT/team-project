package app;

import java.awt.CardLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import view.NewPlayerView;
import view.ViewManager;
import view.WelcomeView;
import data_access.PlayerDataAccessObject;
import entity.PlayerFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.makeNewPlayer.NewPlayerController;
import interface_adapter.makeNewPlayer.NewPlayerPresenter;
import interface_adapter.makeNewPlayer.NewPlayerViewModel;
import interface_adapter.welcomeMenu.WelcomeViewModel;
import use_case.makeNewPlayer.NewPlayerInteractor;

public class WelcomeDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // View models
            final ViewManagerModel viewManagerModel = new ViewManagerModel();
            final NewPlayerViewModel newPlayerViewModel = new NewPlayerViewModel();
            final WelcomeViewModel welcomeViewModel = new WelcomeViewModel();

            // Views
            final NewPlayerView newPlayerView = new NewPlayerView(newPlayerViewModel);
            final WelcomeView welcomeView = new WelcomeView(welcomeViewModel);

            // Use case wiring
            final PlayerFactory playerFactory = new PlayerFactory();
            final PlayerDataAccessObject dao = new PlayerDataAccessObject("players.csv", playerFactory);
            final NewPlayerPresenter presenter =
                    new NewPlayerPresenter(viewManagerModel, newPlayerViewModel, welcomeViewModel);
            final NewPlayerInteractor interactor =
                    new NewPlayerInteractor(dao, presenter, playerFactory, dao);
            newPlayerView.setNewPlayerController(new NewPlayerController(interactor));

            // Card layout holding both views
            final CardLayout cardLayout = new CardLayout();
            final JPanel views = new JPanel(cardLayout);
            views.add(newPlayerView, newPlayerView.getViewName());
            views.add(welcomeView, welcomeView.getViewName());

            new ViewManager(views, cardLayout, viewManagerModel);

            viewManagerModel.setState(newPlayerView.getViewName());
            viewManagerModel.notifyViewChanged();

            final JFrame frame = new JFrame("CityGuesser");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(views);
            frame.setMinimumSize(new java.awt.Dimension(300, 200));
            frame.pack();
            frame.setVisible(true);
        });
    }
}
