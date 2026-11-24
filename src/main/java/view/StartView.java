package view;

import interface_adapter.new_game.NewGameController;
import interface_adapter.persistence.PersistenceController;
import interface_adapter.persistence.PersistenceViewModel;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * Temporary first screen until Story 1's title screen is available.
 */
public class StartView extends JPanel {

    public static final String VIEW_NAME = "start";
    private final JButton newGameButton = new JButton("New Game");
    private final JPanel actions = new JPanel();

    public StartView(NewGameController newGameController) {
        setLayout(new BorderLayout());
        newGameButton.addActionListener(e -> newGameController.execute());
        actions.add(newGameButton);
        JPanel center = new JPanel(new GridBagLayout());
        center.add(actions);
        add(center, BorderLayout.CENTER);
    }

    public void addLoadGame(PersistenceController controller, PersistenceViewModel feedback) {
        JButton loadButton = new JButton("Load Game");
        loadButton.addActionListener(e -> controller.load());
        actions.add(loadButton);
        JLabel message = new JLabel(" ", SwingConstants.CENTER);
        add(message, BorderLayout.SOUTH);
        feedback.addPropertyChangeListener(event -> {
            var state = feedback.getState();
            newGameButton.setEnabled(!state.busy());
            loadButton.setEnabled(!state.busy());
            message.setText(state.message());
        });
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
