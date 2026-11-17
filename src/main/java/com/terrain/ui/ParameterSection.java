package com.terrain.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

import com.terrain.entity.model.feature.parameter.Parameter;
import com.terrain.entity.model.feature.parameter.ParameterSet;

// A collapsible section showing an editor for each parameter. Used for both the world settings (not removable) and
// each feature (removable, with an x button).
class ParameterSection extends TitledPane {
    // non-removable section: no x button. (world settings)
    ParameterSection(String title, ParameterSet source, Runnable onChange) {
        this(title, source, onChange, null);
    }

    // removable section when onRemove is non-null. (feature settings)
    ParameterSection(String title, ParameterSet source, Runnable onChange,
                     Consumer<ParameterSection> onRemove) {
        VBox content = new VBox(8);
        content.setPadding(new Insets(8));
        for (Parameter parameter : source.getParameters()) {
            content.getChildren().add(ParamInputCreator.create(parameter, onChange));
        }
        setContent(content);
        setExpanded(true); // default to expanded

        if (onRemove == null) {
            setText(title);
            return;
        }

        Button remove = new Button("✕");
        remove.setOnAction(e -> onRemove.accept(this));

        // spacer pushes the button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(new Label(title), spacer, remove);
        header.setAlignment(Pos.CENTER_LEFT);
        header.prefWidthProperty().bind(widthProperty().subtract(40));
        setGraphic(header);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY); // show only the graphic, not the title text
    }
}
