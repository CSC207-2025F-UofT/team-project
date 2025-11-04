package entities;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class Overlay {
    private final String type;
    private double opacity;
    private BufferedImage ovl;
    private final Dimension size;

    public Overlay(String type){
        this.type = type;
        this.opacity = 0.5;
        this.size = new Dimension(800,800);
        this.ovl = new BufferedImage(this.size.width, this.size.height, BufferedImage.TYPE_INT_ARGB);
    }

    public Overlay(String type, double opacity, int x, int y){
        this.type = type;
        this.opacity = opacity;
        this.size = new Dimension(x,y);
        this.ovl = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }

    public void setOpacity(double opacity){
        // change this layer's opacity
        this.opacity = opacity;
    }

    public void setDimension(int x, int y){
        // Change the overlay's size. This will reset the overlay image.
        this.size.width = x;
        this.size.height = y;
        this.ovl = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }

    public String getType(){return this.type;}

    public double getOpactiy(){return this.opacity;}

    public BufferedImage getOvl(){return this.ovl;}

    public void updateImage(){
        //TODO. Potnetial arguments:
        //      x  -  this tile's x pos  (stored in tile?)
        //      y  -  this tile's y pos
        //      zoom  -  the current zoom level  (represent using viewport?)
        //      center  -  the center of the map
        //      tile - the tile entity containing the tile image

        //      Draw given tile onto image based on given arguments.



    }
}
