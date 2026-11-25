package interface_adapter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasViewModel {

    private String projectTitle;
    private int canvasWidth;
    private int canvasHeight;
    private Color canvasColour;

    public static final String VIEW_NAME = "canvas";
    private final List<CanvasViewModelListener> listeners = new ArrayList<>();


    private CanvasState state = new CanvasState();
    public CanvasState getState() {
        return this.state;
    }
    public void setState(CanvasState state) {
        this.state = state;
    }

    public void addListener(CanvasViewModelListener listener) {
        listeners.add(listener);
    }
    public void notifyStateChanged() {
        for (CanvasViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    public String getTitle() {return projectTitle;}
    public void setTitle(String title) {this.projectTitle = title;}

    public int getCanvasWidth() {return canvasWidth;}
    public void setCanvasWidth(int width) {this.canvasWidth = width;}

    public int getCanvasHeight() {return canvasHeight;}
    public void setCanvasHeight(int height) {this.canvasHeight = height;}

    public Color getCanvasColour() {return canvasColour;}
    public void setCanvasColour(Color colour) {this.canvasColour = colour;}
}
