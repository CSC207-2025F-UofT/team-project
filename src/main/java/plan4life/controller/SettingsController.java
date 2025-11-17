// 文件路径: src/main/java/plan4life/controller/SettingsController.java
package plan4life.controller;

import plan4life.view.SettingsView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controller for the SettingsView.
 * It listens to UI events and will (eventually) call the Use Case.
 */
public class SettingsController {

    private SettingsView settingsView;
    // (未来我们会在这里添加 use case: 
    // private SetPreferencesInputBoundary useCaseInteractor;)

    public SettingsController(SettingsView view) {
        this.settingsView = view;
        addListeners();
    }

    private void addListeners() {

        // --- Cancel Button Logic ---
        settingsView.getCancelButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Just close the dialog
                settingsView.dispose();
            }
        });

        // --- Save Button Logic (Placeholder) ---
        settingsView.getSaveButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 1. Get data from settingsView
                // TODO: 2. Create InputData object
                // TODO: 3. Call useCaseInteractor.execute(inputData)

                // For now, just print and close
                System.out.println("Settings 'Saved' (Demo)!");
                settingsView.dispose();
            }
        });
    }
}