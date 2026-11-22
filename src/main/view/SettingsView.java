package main.view;

import main.interface_adapters.settings.SettingsController;
import main.interface_adapters.settings.SettingsPresenter;
import main.interface_adapters.settings.SettingsViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsView extends JPanel implements ActionListener {

    private final SettingsViewModel settingsViewModel;
    private SettingsController settingsController;

    private final JButton returnButton;

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
        this.settingsController = null;
        this.setLayout(null);

        JLabel statsTitle = new JLabel("STATS");
        ViewUtils.setFont(statsTitle, "Figtree Black", Font.PLAIN, 28, 0.08);
        ViewUtils.setRelativeBounds(statsTitle, 0.02, 0.02, 1, 0.1);
        statsTitle.setForeground(Color.white);

        JLabel settingsTitle = new JLabel("SETTINGS", SwingConstants.CENTER);
        ViewUtils.setFont(settingsTitle, "Figtree Black", Font.PLAIN, 28, 0.08);
        ViewUtils.setRelativeBounds(settingsTitle, 0.0, 0.02, 1, 0.1);
        settingsTitle.setForeground(Color.white);

        JPanel settingsPanel = new JPanel();
        ViewUtils.setRelativeBounds(settingsPanel, 0.65, 0, 0.35, 1);
        settingsPanel.setBackground(new Color(5, 5, 5));
        settingsPanel.add(settingsTitle);

        JPanel statsPanel = new JPanel();
        ViewUtils.setRelativeBounds(statsPanel, 0, 0, 0.65, 1);
        statsPanel.setBackground(new Color(5, 5, 10));


        JPanel subStatMoneyPanel = new JPanel();
        ViewUtils.setRelativeBounds(subStatMoneyPanel, 0.05, 0.37, 0.5, 0.1);
        subStatMoneyPanel.setBackground(new Color(0, 0, 0, 128));
        subStatMoneyPanel.setLayout(new BorderLayout());

        JLabel subStatMoneyLabel = new JLabel("MONEY:");
        ViewUtils.setFont(subStatMoneyLabel, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(subStatMoneyLabel, 0.06, 0.38, 0.5, 0.08);
        subStatMoneyLabel.setForeground(Color.WHITE);

        JLabel subStatMoneyAmountLabel = new JLabel(SettingsViewModel.USER_MONEY);
        ViewUtils.setFont(subStatMoneyAmountLabel, "Figtree SemiBold", Font.PLAIN,8, 0.05);
        ViewUtils.setRelativeBounds(subStatMoneyAmountLabel, 0.18, 0.38, 0.4, 0.08);
        subStatMoneyAmountLabel.setForeground(new Color(171, 221, 255));


        JPanel subStatBigWinPanel = new JPanel();
        ViewUtils.setRelativeBounds(subStatBigWinPanel, 0.05, 0.48, 0.5, 0.1);
        subStatBigWinPanel.setBackground(new Color(0, 0, 0, 128));
        subStatBigWinPanel.setLayout(new BorderLayout());

        JLabel subStatBigWinLabel = new JLabel("BIGGEST WIN:");
        ViewUtils.setFont(subStatBigWinLabel, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(subStatBigWinLabel, 0.06, 0.49, 0.5, 0.08);
        subStatBigWinLabel.setForeground(Color.WHITE);

        JLabel subStatBigWinAmountLabel = new JLabel(SettingsViewModel.USER_BIGGEST_WIN);
        ViewUtils.setFont(subStatBigWinAmountLabel, "Figtree SemiBold", Font.PLAIN,8, 0.05);
        ViewUtils.setRelativeBounds(subStatBigWinAmountLabel, 0.255, 0.49, 0.4, 0.08);
        subStatBigWinAmountLabel.setForeground(new Color(171, 221, 255));


        JPanel subStatWinRatePanel = new JPanel();
        ViewUtils.setRelativeBounds(subStatWinRatePanel, 0.05, 0.59, 0.5, 0.1);
        subStatWinRatePanel.setBackground(new Color(0, 0, 0, 128));
        subStatWinRatePanel.setLayout(new BorderLayout());

        JLabel subStatWinRateLabel = new JLabel("WIN RATE:");
        ViewUtils.setFont(subStatWinRateLabel, "Figtree Black", Font.PLAIN, 28, 0.05);
        ViewUtils.setRelativeBounds(subStatWinRateLabel, 0.06, 0.6, 0.5, 0.08);
        subStatWinRateLabel.setForeground(Color.WHITE);

        JLabel subStatWinRateAmountLabel = new JLabel(SettingsViewModel.USER_WIN_RATE);
        ViewUtils.setFont(subStatWinRateAmountLabel, "Figtree SemiBold", Font.PLAIN,8, 0.05);
        ViewUtils.setRelativeBounds(subStatWinRateAmountLabel, 0.215, 0.6, 0.4, 0.08);
        subStatWinRateAmountLabel.setForeground(new Color(171, 221, 255));


        returnButton = new JButton("<- Return");
        ViewUtils.setFont(returnButton, "Figtree Black", Font.BOLD, 28, 0.04);
        ViewUtils.setRelativeBounds(returnButton, 0.02, 0.88, 0.2, 0.1);
        returnButton.setFocusPainted(false);
        returnButton.setForeground(Color.white);
        returnButton.setBackground(new Color(196, 43, 64));
        returnButton.addActionListener(this);
        returnButton.setBorder(BorderFactory.createLineBorder(Color.white, 2));

        JLabel profilePicture = ViewUtils.createImage(SettingsViewModel.USER_PFP, 0.04, 0.15, 0.1);
        profilePicture.setBorder(BorderFactory.createLineBorder(Color.white, 6));

        JLabel username = new JLabel(SettingsViewModel.USER_NAME);
        ViewUtils.setFont(username, "Figtree SemiBold", Font.PLAIN, 28, 0.06);
        ViewUtils.setRelativeBounds(username, 0.15, 0.18, 0.4, 0.1);
        username.setForeground(new Color(171, 221, 255));


        this.add(username);
        this.add(returnButton);

        this.add(subStatMoneyLabel);
        this.add(subStatMoneyAmountLabel);
        this.add(subStatMoneyPanel);

        this.add(subStatBigWinLabel);
        this.add(subStatBigWinAmountLabel);
        this.add(subStatBigWinPanel);

        this.add(subStatWinRateLabel);
        this.add(subStatWinRateAmountLabel);
        this.add(subStatWinRatePanel);

        this.add(profilePicture);
        this.add(statsTitle);
        this.add(settingsPanel);
        this.add(statsPanel);
    }

    public void setController(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource().equals(returnButton)) {
            settingsController.switchToMainMenuView();
        }
    }
}
