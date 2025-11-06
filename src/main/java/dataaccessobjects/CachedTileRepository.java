package dataaccessobjects;

import entity.WeatherTile;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class CachedTileRepository {
    private HashMap<String, BufferedImage> tileHash;
    private List<BufferedImage> tiles = new ArrayList<>();
    private static int tileCacheSize;

    public CachedTileRepository(){
        tileHash = new HashMap<>();
        tiles = new List<>();
    }

    public BufferedImage getImageData(WeatherTile tile){
        String tileKey = WeatherTile.getKey(tile);
        if (tileHash.containsKey(tileKey)){
            return tileHash.get(tileKey);
        }
        else {
            return getTileImageFromAPI(tile);
        }
    }

    private BufferedImage getTileImageFromAPI(WeatherTile tile){
        return null;
    }
}
