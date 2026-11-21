package main.view;

import main.interface_adapters.main_menu.MainMenuController;
import main.interface_adapters.main_menu.MainMenuViewModel;
import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.*;

public class MainMenuView extends JPanel implements ActionListener {

    private final String viewName = "Main Menu";
    private final MainMenuViewModel viewModel;
    private MainMenuController mainMenuController = null;

    public final JLabel titleLabel;
    public final JButton playButton;
    public final JButton settingsButton;


    public MainMenuView(MainMenuViewModel mainMenuViewModel) {
        this.viewModel = mainMenuViewModel;
        this.setLayout(null);

        //Game title setup
        titleLabel = new JLabel(MainMenuViewModel.TITLE_LABEL, SwingConstants.CENTER);
        titleLabel.setForeground(Color.white);
        ViewUtils.setFont(titleLabel, "figtree", Font.BOLD, 28, 0.06);
        ViewUtils.setRelativeBounds(titleLabel, 0.0, 0.3, 1.0, 0.15);

        //Play Button set up
        playButton = new JButton(MainMenuViewModel.PLAY_LABEL);
        ViewUtils.setFont(playButton, "figtree", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(playButton, 0.34, 0.5, 0.15, 0.1);
        playButton.setBackground(new Color(9, 150, 14));
        playButton.setForeground(Color.white);
        playButton.setFocusPainted(false);
        playButton.addActionListener(this);

        settingsButton = new JButton(MainMenuViewModel.SETTINGS_LABEL);
        ViewUtils.setFont(settingsButton, "figtree", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(settingsButton, 0.51, 0.5, 0.15, 0.1);
        settingsButton.setBackground(new Color(73, 109, 140));
        settingsButton.setForeground(Color.white);
        settingsButton.setFocusPainted(false);
        settingsButton.addActionListener(this);

        //colors
        this.setBackground(new Color(5,5,5));
        titleLabel.setForeground(Color.WHITE);

        this.add(playButton);
        this.add(titleLabel);
        this.add(settingsButton);

    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(playButton)) {
            mainMenuController.switchToGameView();
        }
        if (event.getSource().equals(settingsButton)) {
            mainMenuController.switchToSettingsView();
        }
    }

    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
}
