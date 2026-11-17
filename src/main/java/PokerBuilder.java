
import javax.swing.*;
import java.awt.*;

import main.interface_adapters.ViewManagerModel;
import main.view.MainMenuView;
import main.view.GameView;
import main.interface_adapters.MainMenuViewModel;
import main.interface_adapters.GameViewModel;
import main.view.ViewManager;


public class PokerBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private MainMenuView mainMenuView;
    private MainMenuViewModel mainMenuViewModel;

    public PokerBuilder() {cardPanel.setLayout(cardLayout);}

    public PokerBuilder addMainMenuView() {
        // Creates new Main Menu View and sets it to display
        this.mainMenuViewModel = new MainMenuViewModel();
        this.mainMenuView = new MainMenuView(mainMenuViewModel);

        cardPanel.add(mainMenuView);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Poker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        viewManagerModel.setActiveView(MainMenuViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
        application.add(cardPanel);

        return application;
    }
}
