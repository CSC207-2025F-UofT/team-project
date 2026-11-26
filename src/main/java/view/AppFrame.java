package view;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

/**
 * Main application window and screen navigator.
 */
public class AppFrame extends JFrame implements Navigator {

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    public AppFrame() {
        super("AACorn");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 720);
        setLocationRelativeTo(null);

        root.add(new PinPanel(this), ScreenNames.PIN);
        root.add(new CaregiverMenuPanel(this), ScreenNames.CAREGIVER_MENU);
        root.add(new CustomizeBoardPanel(this), ScreenNames.CUSTOMIZE);
        root.add(new SearchPanel(this), ScreenNames.SEARCH);

        add(root);
    }

    /**
     * Adds a screen whose dependencies are built in app.Main.
     */
    public void addScreen(JPanel screen, String screenName) {
        root.add(screen, screenName);
    }

    @Override
    public void show(String screenName) {
        cards.show(root, screenName);
    }
}