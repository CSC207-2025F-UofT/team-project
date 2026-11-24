package com.terrain.ui;

import com.terrain.entity.model.TerrainMap;

import com.terrain.entity.model.TileType;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

// wraps the JavaFX Canvas and provides a view of the terrain map
public class MapView extends StackPane {

    private static final double ZOOM_RATE = 0.0015;
    private static final double MIN_ZOOM = 0.01;
    private static final double MAX_ZOOM = 100;

    private final Canvas canvas;
    private boolean fitPending; // whether the canvas needs to be fit to the view
    private double fitScale; // the scale factor for fitting
    private Point2D dragPosition; // position of the mouse during dragging

    public MapView() {
        canvas = new Canvas();
        canvas.setManaged(false);

        // ensures the canvas is contained by the MapView
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(widthProperty());
        clip.heightProperty().bind(heightProperty());
        setClip(clip);
        setMinSize(0, 0);

        // adds the canvas to the MapView, and sets up event handlers for zooming and dragging
        getChildren().add(canvas);
        addEventHandler(ScrollEvent.SCROLL, this::zoom);
        addEventHandler(MouseEvent.MOUSE_PRESSED, this::startDrag);
        addEventHandler(MouseEvent.MOUSE_DRAGGED, this::drag);
        addEventHandler(MouseEvent.MOUSE_RELEASED, this::endDrag);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    // converts a TileType.Rgb to a JavaFX Color
    public Color toColor(TileType.Rgb rgb) {
        return Color.color(rgb.red(), rgb.green(), rgb.blue());
    }

    // displays the given TerrainMap on the canvas, scaling it to fit the view
    public void display(TerrainMap map) {
        int width = map.getWidth();
        int height = map.getHeight();

        // init canvas size and clear it
        canvas.setWidth(width);
        canvas.setHeight(height);
        canvas.getGraphicsContext2D().clearRect(0, 0, width, height);

        // get the elevation bounds for color mapping
        map.getElevationBounds();
        double maxElevation = map.getMaxElevation();
        double minElevation = map.getMinElevation();


        // draw the terrain map on the canvas
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                var tile = map.getTile(x, y);

                double elevation = tile.getElevation();
                var color = tile.getType().colorAt(elevation, minElevation, maxElevation);
                canvas.getGraphicsContext2D().setFill(toColor(color));
                canvas.getGraphicsContext2D().fillRect(x, y, 1, 1);
            }
        }

        // reset the view to fit the canvas
        canvas.setTranslateX(0);
        canvas.setTranslateY(0);
        dragPosition = null;
        fitPending = true;
        fitCanvas();
        requestLayout();
    }

    private void fitCanvas() {
        // fits the canvas to the available space
        Insets insets = getInsets();
        double availableWidth = getWidth() - insets.getLeft() - insets.getRight();
        double availableHeight = getHeight() - insets.getTop() - insets.getBottom();
        if (fitPending && availableWidth > 0 && availableHeight > 0) {
            fitScale = Math.min(availableWidth / canvas.getWidth(), availableHeight / canvas.getHeight());
            canvas.setScaleX(fitScale);
            canvas.setScaleY(fitScale);
            fitPending = false;
        }
    }

    // zooms the canvas based on the scroll event
    private void zoom(ScrollEvent event) {
        if (fitPending || fitScale == 0 || event.getDeltaY() == 0) {
            return;
        }

        double oldScale = canvas.getScaleX();
        double scale = Math.max(fitScale * MIN_ZOOM,
                Math.min(fitScale * MAX_ZOOM, oldScale * Math.exp(event.getDeltaY() * ZOOM_RATE)));
        double ratio = scale / oldScale;
        Point2D cursor = sceneToLocal(event.getSceneX(), event.getSceneY());
        Point2D center = canvas.localToParent(canvas.getWidth() / 2, canvas.getHeight() / 2);
        canvas.setTranslateX(canvas.getTranslateX() + (1 - ratio) * (cursor.getX() - center.getX()));
        canvas.setTranslateY(canvas.getTranslateY() + (1 - ratio) * (cursor.getY() - center.getY()));
        canvas.setScaleX(scale);
        canvas.setScaleY(scale);
        event.consume();
    }

    // starts dragging the canvas
    private void startDrag(MouseEvent event) {
        if (!fitPending && fitScale > 0 && event.getButton() == MouseButton.PRIMARY) {
            dragPosition = sceneToLocal(event.getSceneX(), event.getSceneY());
            event.consume();
        }
    }

    // drags the canvas
    private void drag(MouseEvent event) {
        if (dragPosition != null && event.isPrimaryButtonDown()) {
            Point2D position = sceneToLocal(event.getSceneX(), event.getSceneY());
            canvas.setTranslateX(canvas.getTranslateX() + position.getX() - dragPosition.getX());
            canvas.setTranslateY(canvas.getTranslateY() + position.getY() - dragPosition.getY());
            dragPosition = position;
            event.consume();
        }
    }

    // ends dragging the canvas
    private void endDrag(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            dragPosition = null;
        }
    }

    // overrides StackPane's layoutChildren to fit the canvas to the view and center it
    // this is called whenever the MapView is resized
    @Override
    protected void layoutChildren() {
        fitCanvas();
        Insets insets = getInsets();
        double availableWidth = getWidth() - insets.getLeft() - insets.getRight();
        double availableHeight = getHeight() - insets.getTop() - insets.getBottom();
        canvas.relocate(insets.getLeft() + (availableWidth - canvas.getWidth()) / 2,
                insets.getTop() + (availableHeight - canvas.getHeight()) / 2);
    }
}
