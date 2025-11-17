package view;
import javax.swing.*;
import java.awt.*;

public class BaseView extends JPanel {
    private final String viewName = "BaseView";

    public BaseView() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome to the Base View!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(welcomeLabel, BorderLayout.CENTER);
    }

    public String getViewName() {
        return viewName;
    }
}
