package main.view;

import main.interface_adapters.main_menu.MainMenuController;
import main.interface_adapters.main_menu.MainMenuViewModel;
import javax.swing.*;
import java.awt.event.*;

public class MainMenuView extends JPanel {

    private final String viewName = "Main Menu";
    private final MainMenuViewModel viewModel;
    private MainMenuController mainMenuController = null;

    public final JButton play;


    public MainMenuView(MainMenuViewModel mainMenuViewModel) {
        this.viewModel = mainMenuViewModel;

        final JLabel title = new JLabel(MainMenuViewModel.TITLE_LABEL);
        title.setAlignmentX(CENTER_ALIGNMENT);

        final JPanel button = new JPanel();
        this.play = new JButton(MainMenuViewModel.PLAY_LABEL);
        button.add(this.play);

        this.play.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(play)) {
                            mainMenuController.switchToGameView();
                        }
                    }
                }
        );

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(button);

    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
}
