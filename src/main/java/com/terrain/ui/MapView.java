package com.terrain.ui;

import com.terrain.entity.model.TerrainMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.StackPane;

// Wraps the JavaFX Canvas and provides a view of the terrain map.
public class MapView extends StackPane {

    private final Canvas canvas;

    public MapView() {
        canvas = new Canvas(800, 800);

        getChildren().add(canvas);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void display(TerrainMap map) {
        // TODO: Logic to render the terrain map on the canvas
    }
}
