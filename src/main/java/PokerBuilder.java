
import javax.swing.*;
import java.awt.*;
import main.view.MainMenuView;
import main.interface_adapters.MainMenuViewModel;

public class PokerBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();

    public PokerBuilder() {cardPanel.setLayout(cardLayout);}

    public JFrame build() {
        final JFrame application = new JFrame("Poker");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Creates new Main Menu View and sets it to display
        MainMenuViewModel mainMenuViewModel = new MainMenuViewModel();
        MainMenuView mainMenuView = new MainMenuView(mainMenuViewModel);
        cardPanel.add(mainMenuView);
        application.add(cardPanel);


        return application;
    }
}
