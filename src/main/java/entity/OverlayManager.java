package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class OverlayManager {
    private final ArrayList<String> types;
    private final ArrayList<Float> opacity;
    private String selected;
    private BufferedImage overlay;

    public OverlayManager(int x, int y){
        this.types = new ArrayList<String>();
        this.opacity = new ArrayList<Float>();
        this.selected = null;
        this.overlay = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }

    //TODO use observers to automatically update this when the viewport changes size
    public void changeSize(int x, int y){
        this.overlay = new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
    }

    public void addOverlayType(String type){
        this.types.add(type);
        this.opacity.add((float)0.5);
    }

    public void setSelected(String selection) throws LayerNotFoundException {
        // Change the selected layer. Raise LayerNotFoundException if selection is not an added overlay type.
        if(this.types.contains(selection)) {
            this.selected = selection;
        }
        else {throw new LayerNotFoundException(selection);}
    }

    public float getSelectedOpacity(){
        if (this.selected == null) {return 0;}
        return this.opacity.get(this.types.indexOf(this.selected));
    }

    public void setSelectedOpacity(float opacity){
        if (this.selected == null) {return;}
        this.opacity.set(this.types.indexOf(this.selected), opacity);
    }

    public BufferedImage getOverlay() {
        return overlay;
    }

    //TODO implement updateOverlayUseCase
    public void updateOverlay(BoundingBox bBox, ArrayList<Tile> tileList){
        for (Tile tile: tileList){
            drawTileToOverlay(bBox, tile);
        }
    }

    private void drawTileToOverlay(BoundingBox bBox, Tile tile){
        //Draw given tile onto the overlay based on the viewport bounding box.

        //convert tile location to Vector for convenience.
        Vector tileCoord = new Vector(tile.getX(),tile.getY());

        //lat lon as bounding box, convert lat lon to 0-1.
        double bBoxLX = convertLatitude(bBox.getTopLeft().getLatitude());
        double bBoxRX = convertLatitude(bBox.getBottomRight().getLatitude());
        double bBoxLY = convertLongitude(bBox.getTopLeft().getLongitude());
        double bBoxRY = convertLongitude(bBox.getBottomRight().getLongitude());

        Vector topLeft = new Vector(bBoxLX, bBoxLY);
        Vector botRight = new Vector(bBoxRX, bBoxRY);

        //convert bounding box vecs to tile grid coords based on zoom (0-6, dimension are 2^z)
        topLeft.scale(Math.pow(2, tile.getZoom()));
        botRight.scale(Math.pow(2, tile.getZoom()));

        //based on the bBox vecs' width and height, figure out where the tile should go.
        //1. move viewport and tile to top left of tile grid. i.e. subtract br, tilec by tl
        tileCoord.sub(topLeft);
        botRight.sub(topLeft);

        //2. find a value s s.t. viewport * s = overlay. Assume same porportion, so br.x * s should = overlay.getWidth() and y*s = height.
        double scaleToOvl = (double)this.overlay.getWidth() / botRight.x ;

        //3. tc * c, top left of tile image is now aligned with the overlay image.
        tileCoord.scale(scaleToOvl);

        //4. scale the tile image to fit onto a tile of the overlay.
        // each tile png is 256x256.
        // (theoratical) "scale" the image to fit onto the UV tile grid, then scale by c.
        // same as c * "scale" = c / (2^zoom * 256)
        double pngToTileFactor = scaleToOvl / (Math.pow(2,tile.getZoom()) * 256) ;

        //apply scale to the tile image.
        BufferedImage tilePng= new BufferedImage((int)(256 * pngToTileFactor), (int)(256 * pngToTileFactor), BufferedImage.TYPE_3BYTE_BGR);
        //tile.getImage().getScaledInstance((int)(256*pngToTileFactor), -1, image.SCALE_FAST);

        //draw tile image onto overlay with selected layer's opacity.
        Graphics2D g = this.overlay.createGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getSelectedOpacity());
        g.setComposite(alphaComposite);
        g.drawImage(tilePng, (int)tileCoord.x, (int)tileCoord.y, null);
        g.dispose();

    }

    // convert both lat and lon to a value between 0-1, 0 being -180 or 90, 1 being 180 or -90.
    // for lon, the direction is reversed as in image processing the Y axis goes from top to bottom.
    private double convertLatitude(double lat){
        return (lat + 180) / 360;
    }

    private double convertLongitude(double lon){
        return (-1 * lon + 90) / 180;
    }
}

class LayerNotFoundException extends Exception {
    public LayerNotFoundException(String layer) {
        super("Layer " + layer + " not found!");
    }
}
