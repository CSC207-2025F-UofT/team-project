package main.view;

import main.interface_adapters.settings.SettingsViewModel;

import javax.swing.*;
import java.awt.*;

public class SettingsView extends JPanel {

    private final SettingsViewModel settingsViewModel;

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
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

        JLabel profilePicture = ViewUtils.createImage(SettingsViewModel.USER_PFP, 0.04, 0.15, 0.1);
        profilePicture.setBorder(BorderFactory.createLineBorder(Color.white, 6));

        this.add(profilePicture);
        this.add(statsTitle);
        this.add(settingsPanel);
        this.add(statsPanel);
    }
}
