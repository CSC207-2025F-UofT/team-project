package com.terrain.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

// Main application window and layout
public class MainView extends SplitPane {
//    Controller controller = new Controller(settingsPanel, mapView);

    private final SettingsPanel settingsPanel;
    private final MapView mapView;

    public MainView() {
        this.settingsPanel = new SettingsPanel();
        this.mapView = new MapView();
        Controller controller = new Controller(settingsPanel, mapView);
        settingsPanel.setOnAddFeature(controller::onNewFeature);
        settingsPanel.setOnRemoveFeature(controller::onRemoveFeature);
        settingsPanel.setOnGenerate(controller::onGenerate);

        // implicit SplitPane.getItems() ...
        getItems().addAll(settingsPanel, mapView);
        setDividerPositions(0.25);
    }
}
