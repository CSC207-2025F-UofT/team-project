package interface_adapter;

import java.awt.*;

public class CanvasState {
    private long projectId;
    private String projectTitle;
    private int canvasWidth;
    private int canvasHeight;
    private Color canvasColour;


    public long getProjectId() { return this.projectId; }
    public void setProjectId(long projectId) {this.projectId = projectId;}

    public String getTitle() {return projectTitle;}
    public void setTitle(String title) {this.projectTitle = title;}

    public int getCanvasWidth() {return canvasWidth;}
    public void setCanvasWidth(int width) {this.canvasWidth = width;}

    public int getCanvasHeight() {return canvasHeight;}
    public void setCanvasHeight(int height) {this.canvasHeight = height;}

    public Color getCanvasColour() {return canvasColour;}
    public void setCanvasColour(Color colour) {this.canvasColour = colour;}

}
