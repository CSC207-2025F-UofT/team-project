import javax.swing.*;
import java.awt.*;

public class AppController {
    private CardLayout layout;
    private JPanel container;

    public static final String SEARCH = "SEARCH";
    public static final String RESULTS = "RESULTS";
    public static final String FAVORITES = "FAVORITES";
    public static final String ADVANCED = "ADVANCED";

    public AppController() {
        layout = new CardLayout();
        container = new JPanel(layout);
    }

    public JPanel getContainer() {
        return container;
    }

    public void addScreen(String name, JPanel panel) {
        container.add(panel, name);
    }

    public void show(String name) {
        layout.show(container, name);
    }
}
