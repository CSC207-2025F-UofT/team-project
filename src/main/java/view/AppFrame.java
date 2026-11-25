package view;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame implements Navigator {
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    public AppFrame() {
        super("AACorn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(480, 720);
        setLocationRelativeTo(null);

        root.add(new MainBoardPanel(this), ScreenNames.MAIN);
        root.add(new PinPanel(this), ScreenNames.PIN);
        root.add(new CaregiverMenuPanel(this), ScreenNames.CAREGIVER_MENU);
        root.add(new CustomizeBoardPanel(this), ScreenNames.CUSTOMIZE);
        root.add(new SearchPanel(this), ScreenNames.SEARCH);

        add(root);
        show(ScreenNames.MAIN);
    }

    /**
     * Adds a screen that needs use cases connected to it.
     * Screens like this are built in app.Main.
     */
    public void addScreen(JPanel screen, String screenName) {
        root.add(screen, screenName);
    }

    @Override

    public void show(String screenName) {
        cards.show(root, screenName);
    }

}