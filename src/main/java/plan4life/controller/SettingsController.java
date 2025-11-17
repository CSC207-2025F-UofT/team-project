// File path: src/main/java/plan4life/controller/SettingsController.java
package plan4life.controller;

import plan4life.view.SettingsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Handles the logic for the SettingsView.
 * This class connects the user's actions (like button clicks)
 * in the SettingsView to the application's logic.
 *
 * This is currently a "UI Placeholder" implementation.
 */
public class SettingsController {

    private SettingsView settingsView;
    // TODO (Future): Add a field for the UserPreferences model
    // private UserPreferences userPreferences;

    /**
     * Constructor for the SettingsController.
     * @param view The SettingsView instance that this controller will manage.
     */
    public SettingsController(SettingsView view) {
        this.settingsView = view;
        // (When the model is added, it would be passed in here too)

        // Attach the event listeners to the view's components
        addListeners();
    }

    /**
     * Private helper method to add all necessary ActionListeners
     * to the components in SettingsView.
     */
    private void addListeners() {

        // --- Cancel Button Logic ---
        settingsView.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When "Cancel" is clicked, just close the dialog.
                // dispose() hides the window and releases its resources.
                settingsView.dispose();
            }
        });

        // --- Save Button Logic ---
        settingsView.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // This is a placeholder for now.

                // TODO (Future):
                // 1. Get all the selected values from the settingsView getters
                //    String selectedLang = (String) settingsView.getLanguageCombo().getSelectedItem();
                //    boolean isDarkMode = settingsView.getDarkModeRadio().isSelected();

                // 2. Save these values to the UserPreferences model
                //    userPreferences.setLanguage(selectedLang);
                //    userPreferences.setTheme(isDarkMode ? "dark" : "light");

                // 3. For now, just print a message to the console
                System.out.println("Settings 'Saved' (Demo)!");

                // 4. Close the dialog
                settingsView.dispose();
            }
        });
    }
}