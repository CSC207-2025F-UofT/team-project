package main.view;

import main.interface_adapters.main_menu.MainMenuController;
import main.interface_adapters.main_menu.MainMenuViewModel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuView extends JPanel {

    private final String viewName = "Main Menu";
    private final MainMenuViewModel viewModel;
    private MainMenuController mainMenuController = null;

    public final JLabel titleLabel;
    public final JButton playButton;


    public MainMenuView(MainMenuViewModel mainMenuViewModel) {
        this.viewModel = mainMenuViewModel;
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        titleLabel = new JLabel(MainMenuViewModel.TITLE_LABEL);

        playButton = new JButton(MainMenuViewModel.PLAY_LABEL);

        playButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(playButton)) {
                            mainMenuController.switchToGameView();
                        }
                    }
                }
        );

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateComponentSizes();
            }
        });

        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.insets = new Insets(0, 0, 30, 0);
        this.add(titleLabel, gbc);

        // Row 1: Button with weighty = 0 so it sits at the exact vertical and horizontal midpoint
        gbc.gridy = 1;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        this.add(playButton, gbc);

        // Row 2: Invisible spacer matching Row 0's weight to balance the bottom half
        gbc.gridy = 2;
        gbc.weighty = 1.0;
        this.add(Box.createGlue(), gbc);

    }

    private void updateComponentSizes() {
        int width = UserSystemSettings.getScreenWidth();
        int height = UserSystemSettings.getScreenHeight();

        float titleFontSize = Math.max(28f, height * 0.06f);
        titleLabel.setFont(titleLabel.getFont().deriveFont(titleFontSize));

        int buttonWidth = (int) (width * 0.10);
        int buttonHeight = (int) (height * 0.10);
        Dimension buttonSize = new Dimension(buttonWidth, buttonHeight);

        playButton.setPreferredSize(buttonSize);
        playButton.setMaximumSize(buttonSize);

        //uses floats because deriveFont requires it
        float fontSize = Math.max(14f, height * 0.05f);
        playButton.setFont(playButton.getFont().deriveFont(fontSize));

        this.revalidate();
        this.repaint();
    }
    public void setMainMenuController(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
    }
}
