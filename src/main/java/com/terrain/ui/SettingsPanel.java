package com.terrain.ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import com.terrain.entity.model.feature.Feature;
import com.terrain.entity.model.feature.FeatureType;
import com.terrain.generation.WorldSetting;

import java.util.function.Consumer;

// A panel that displays the settings for the terrain generator, including the world size and the list of features.
// This setup allows us to keep all toolkit code out of the model, and to keep the model code free of any toolkit
// dependencies. Basically, if we change toolkit, we only have to change this file, the MapView and MainView files,
// and the rest of the code is unaffected

public class SettingsPanel extends VBox {
    private final VBox worldHolder = new VBox();
    private final VBox featureList = new VBox(8);
    private final ComboBox<FeatureType> typeDropdown = new ComboBox<>();

    // a consumer is a function that takes one argument and returns nothing. used to pass the controller's onAddFeature
    // and onRemoveFeature methods such that they can be called whenever.
    private Consumer<FeatureType> onAddFeature = type -> {
    };
    private Consumer<Feature> onRemoveFeature = feature -> {
    };
    private Runnable onGenerate = () -> {
    };
    private Runnable onWorldChanged = () -> {
    };

    public SettingsPanel() {

        setPrefWidth(300);
        setSpacing(10);
        setPadding(new Insets(10));

        Label title = new Label("Settings");

        // add all feature types to the dropdown
        typeDropdown.getItems().addAll(FeatureType.values());
        typeDropdown.getSelectionModel().selectFirst(); // default to first

        // add feature button calls the controller's onAddFeature method with the selected type
        Button addButton = new Button("Add feature");
        addButton.setOnAction(e -> {
            FeatureType type = typeDropdown.getValue();
            if (type != null) {
                onAddFeature.accept(type);
            }
        });

        HBox addRow = new HBox(8, addButton, typeDropdown);

        // setup scrolling; the world section scrolls along with the features, above them
        ScrollPane scroll = new ScrollPane(new VBox(8, worldHolder, featureList));
        scroll.setFitToWidth(true);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        // outside the scroll pane so it's always visible
        Button generateButton = new Button("Generate");
        generateButton.setMaxWidth(Double.MAX_VALUE);
        generateButton.setOnAction(e -> onGenerate.run());

        getChildren().addAll(title, addRow, scroll, generateButton);
    }

    // call controller's onAddFeature when "Add feature" is pressed, passing the selected type
    public void setOnAddFeature(Consumer<FeatureType> onAddFeature) {
        this.onAddFeature = onAddFeature;
    }

    public void setOnRemoveFeature(Consumer<Feature> onRemoveFeature) {
        this.onRemoveFeature = onRemoveFeature;
    }

    // call controller's onGenerate when "Generate" is pressed
    public void setOnGenerate(Runnable onGenerate) {
        this.onGenerate = onGenerate;
    }

    // Called whenever a world setting is edited.
    public void setOnWorldChanged(Runnable onWorldChanged) {
        this.onWorldChanged = onWorldChanged;
    }

    // Shows the always-present, non-removable world section above the features.
    public void setWorldSettings(WorldSetting world) {
        worldHolder.getChildren().setAll(
                new ParameterSection("World", world, () -> onWorldChanged.run()));
    }

    public void addFeatureSection(Feature feature) {
        featureList.getChildren().add(new ParameterSection(feature.getName(), feature, () -> {
        }, section -> {
            featureList.getChildren().remove(section);
            onRemoveFeature.accept(feature);
        }));
    }

    // TODO: reordering of features (important)
}

