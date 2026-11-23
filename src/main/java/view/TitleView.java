package view;

import interface_adapter.continue_game.ContinueGameController;
import interface_adapter.continue_game.TitleViewModel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * Title screen: start a new game or continue the saved one.
 */
public class TitleView extends JPanel {
    /**
     * @param onNewGame what to do when the player starts a new game
     */
    public TitleView(TitleViewModel viewModel, ContinueGameController continueController, Runnable onNewGame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Stressdew Valley");
        JButton newGameButton = new JButton("New Game");
        JButton continueButton = new JButton("Continue");
        JLabel messageLabel = new JLabel();

        newGameButton.addActionListener(e -> onNewGame.run());
        continueButton.addActionListener(e -> continueController.execute());

        for (JComponent c : new JComponent[]{title, newGameButton, continueButton, messageLabel}) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(c);
        }

        viewModel.addPropertyChangeListener(evt -> messageLabel.setText(viewModel.getMessage()));
    }
}
