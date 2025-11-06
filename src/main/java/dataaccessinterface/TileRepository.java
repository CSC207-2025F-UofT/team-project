package dataaccessinterface;
import entity.Tile;
import java.awt.image.BufferedImage;

public interface TileRepository {
    public BufferedImage getTileImageData(int x, int y, double zoom);
    public BufferedImage getTileImageData(Tile tile);
    public void addTile(Tile tile, BufferedImage imageData);
}
