package entity;

import java.awt.image.BufferedImage;

public interface Tile {
    //Tile coords at the current zoom level
    int getX();
    int getY();

    int getZoom();
    BufferedImage getTileImage();
    //getTime?
    //getKey (for caching? (or implement in updateoverlay usecase?)
}
