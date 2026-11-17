package com.terrain.ui;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.terrain.entity.model.feature.parameter.BooleanParameter;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;
import com.terrain.entity.model.feature.parameter.Parameter;

// Builds the editing widgets for a Parameter. Because Parameter is sealed, the compiler flags this switch if a new
// parameter type is added without a matching editor.
final class ParamInputCreator {
    private ParamInputCreator() {}

    // onChange runs after the user edits a value (the parameter is already updated).
    static Node create(Parameter parameter, Runnable onChange) {
        return switch (parameter) {
            // will run the relevant method based on the type of parameter
            case NumberParameter p -> numberParam(p, onChange);
            case CoordinateParameter p -> coordinateParam(p, onChange);
            case BooleanParameter p -> booleanParam(p, onChange);
        };
    }

    private static String format(double value) {
        return String.format("%.2f", value);
    }


    private static void applyValue(TextField field, Runnable apply) {
        // when the user presses enter...
        field.setOnAction(e -> apply.run());
        // ...or when the field loses focus, apply the value
        field.focusedProperty().addListener((obs, old, focused) -> {
            if (!focused) apply.run();
        });
    }

    private static Node numberParam(NumberParameter p, Runnable onChange) {
        // create a slider and a text field for the number parameter, and tie them together
        Slider slider = new Slider(p.min, p.max, p.getValue());
        TextField input = new TextField(format(p.getValue()));
        input.setPrefColumnCount(6); // preferred width of the text field

        // when slider changes, update the parameter and the text field
        slider.valueProperty().addListener((obs, old, v) -> {
            p.setValue(v.doubleValue());
            input.setText(format(p.getValue()));
            onChange.run(); // typed values also land here, since applyNumber moves the slider
        });

        // create apply, pass to applyValue so that it runs when the user presses enter or leaves the field
        Runnable apply = () -> applyNumber(p, slider, input);
        applyValue(input, apply);

        return new VBox(2, new HBox(8, new Label(p.label), input), slider);
    }

    // parses the field into the parameter, then syncs the slider and field to the (possibly clamped) result.
    private static void applyNumber(NumberParameter p, Slider slider, TextField input) {
        try { // try to change the parameter to the new value, but if invalid...
            p.setValue(Double.parseDouble(input.getText()));
        } catch (NumberFormatException ignored) {
            // ... keep the previous value
        }

        // apply to both so that they're in sync
        slider.setValue(p.getValue());
        input.setText(format(p.getValue()));
    }

    // creates a pair of text fields for the x and y coordinates, and ties them to the parameter.
    private static Node coordinateParam(CoordinateParameter p, Runnable onChange) {
        TextField x = new TextField(String.valueOf(p.getX()));
        TextField y = new TextField(String.valueOf(p.getY()));
        x.setPrefColumnCount(5);
        y.setPrefColumnCount(5);

        applyValue(x, () -> {
            applyCoordinate(p, x, y);
            onChange.run();
        });
        applyValue(y, () -> {
            applyCoordinate(p, x, y);
            onChange.run();
        });

        return new HBox(8, new Label(p.label), new Label("X:"), x, new Label("Y:"), y);
    }

    // parses both fields into the parameter, then shows the (possibly clamped) result.
    private static void applyCoordinate(CoordinateParameter p, TextField x, TextField y) {
        try { // try to change the parameter to the new value, but if invalid...
            p.set(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()));
        } catch (NumberFormatException ignored) {
            // ... keep the previous value
        }

        x.setText(String.valueOf(p.getX()));
        y.setText(String.valueOf(p.getY()));
    }

    private static Node booleanParam(BooleanParameter p, Runnable onChange) {
        // booleanParam doesn't need applyValue() because it's easy to apply immediately when the user toggles
        CheckBox box = new CheckBox(p.label);
        box.setSelected(p.getValue());
        // when the user toggles the checkbox, update the parameter
        box.selectedProperty().addListener(
                (obs, old, v) -> {
                    p.setValue(v);
                    onChange.run();
                }
        );

        return box;
    }
}
