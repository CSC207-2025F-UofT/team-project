package entity;
import java.util.ArrayList;

public class Canvas {
    private int height;
    private int width;
    private String backgroundColor;
    private final ArrayList<Layer> layers = new ArrayList<>();

    public Canvas() {
        this(800, 600, "#FFFFFF"); //default values and color of canvas
    }

    public Canvas(int width, int height, String backgroundColor){
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Canvas dimensions must be positive");
        }
        if (backgroundColor == null || backgroundColor.isEmpty() || !backgroundColor.matches("#[0-9A-Fa-f]{6}")){
            throw new IllegalArgumentException("Canvas background color must be a valid hex code.");
        }
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public String getBackgroundColor(){
        return backgroundColor;
    }

    public void addLayer(Layer layer){
        if (layer == null){
            throw new IllegalArgumentException("Layer must exist.");
        }
        this.layers.add(layer);
    }

    public boolean removeLayer(Layer layer){
        return layers.remove(layer);
    }

    public boolean isEmpty(){
        return layers.size() == 0;
    }

    public void resize(int width, int height){
        if (width <= 0 || height <= 0) {
           throw new IllegalArgumentException("Canvas dimensions must be positive");
        }
        this.width = width;
        this.height = height;
    }

    public void changeBackgroundColor(String backgroundColor){
        if (backgroundColor == null || backgroundColor.isEmpty() || !backgroundColor.matches("#[0-9A-Fa-f]{6}")){
            throw new IllegalArgumentException("Canvas background color must be a valid hex code.");
        }
        this.backgroundColor = backgroundColor;
    }

    public ArrayList<Layer> getLayers(){
        return new ArrayList<>(layers);
    }

    public void moveLayer(int fromIndex, int toIndex){
        if (fromIndex > layers.size() -1 || fromIndex < 0 || toIndex > layers.size() -1 || toIndex < 0){
            throw new IndexOutOfBoundsException("Layer index out of range.");
        }
        Layer layer = layers.remove(fromIndex);
        layers.add(toIndex, layer);
    }
}
