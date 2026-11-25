package interface_adapter;

import view.CanvasView;

import java.awt.*;

public class CanvasViewModel {

    private String projectTitle;
    private int canvasWidth;
    private int canvasHeight;
    private Color canvasColour;


    public String getTitle() {
        return projectTitle;
    }

    public void setTitle(String title) {
        this.projectTitle = title;

    }

    public int getCanvasWidth() {
        return canvasWidth;
    }

    public void setCanvasWidth(int width) {
        this.canvasWidth = width;
    }

    public int getCanvasHeight() {
        return canvasHeight;
    }

    public void setCanvasHeight(int height) {
        this.canvasHeight = height;
    }

    public Color getCanvasColour() {
        return canvasColour;
    }

    public void setCanvasColour(Color colour) {
        this.canvasColour = colour;
    }

    public void addListener(CanvasView canvasView) {
    }
}
