package main.view;

import main.interface_adapters.settings.SettingsViewModel;

import javax.swing.*;

public class SettingsView extends JPanel {

    private final SettingsViewModel settingsViewModel;

    public SettingsView(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;

        JLabel title = new JLabel("uh dis da settings n stuff");

        this.add(title);
    }
}
