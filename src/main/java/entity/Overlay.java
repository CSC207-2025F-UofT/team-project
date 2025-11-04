package entity;

import java.awt.*;
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

    public void drawOverlay(BufferedImage overlay){
        Graphics2D g = overlay.createGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) this.opacity);
        g.setComposite(alphaComposite);
        g.drawImage(this.ovl, 0, 0, null);
    }

    public void updateImage(BoundingBox bBox, double zoom){
        //TODO. Potnetial arguments:
        //      x  -  this tile's x pos  (stored in tile?) (assume top left of tile for now, can add offset later)
        //      y  -  this tile's y pos
        //      zoom  -  the current zoom level  (represent using viewport?)
        //      center  -  the center of the map
        //      tile - the tile entity containing the tile image

        //      Draw given tile onto image based on given arguments.

        //convert tile location to SimpleVector
        SimpleVector tileCoord = new SimpleVector(0,0); //TODO CHANGE VALS AFTER TILE LOCL IS IMPLEMENTED

        //lat lon as bounding box, convert lat lon to 0-1. Vector for convenience.
        double bBoxLX = convertLatitude(bBox.getTopLeft().getLatitude());
        double bBoxRX = convertLatitude(bBox.getBottomRight().getLatitude());
        double bBoxLY = convertLongitude(bBox.getTopLeft().getLongitude());
        double bBoxRY = convertLongitude(bBox.getBottomRight().getLongitude());

        SimpleVector topLeft = new SimpleVector(bBoxLX, bBoxLY);
        SimpleVector botRight = new SimpleVector(bBoxRX, bBoxRY);

        //convert the to tile grid coords based on zoom (0-6, dimension are 2^z)
        topLeft.scale(Math.pow(2, zoom));
        botRight.scale(Math.pow(2, zoom));

        //based on the bBox's width and height, figure out where the tile should go.
        //1. move viewport to top left of overlay i.e. subtract tl, br, tc by tc
        tileCoord.sub(topLeft);
        botRight.sub(topLeft);

        //2. find a value s s.t. br.x * c = this.size.x Assume same porportion, so br.y * c should = this.size.y
        double scaleToOvl = (double)this.size.width / botRight.x ;

        //3. tc * c, top left of tile image is now aligned with the overlay image.
        tileCoord.scale(scaleToOvl);

        //4. scale the tile image to fit onto a tile of the overlay.
        // each tile png is 256x256.
        // (theoratical) "scale" the image to fit onto the UV tile grid, then scale by c.
        // same as c * "scale" = c / (2^zoom * 256)
        double pngToTileFactor = scaleToOvl / (Math.pow(2,zoom) * 256) ;

        BufferedImage tilePng= new BufferedImage((int)(256 * pngToTileFactor), (int)(256 * pngToTileFactor), BufferedImage.TYPE_3BYTE_BGR);//tile.image.getScaledInstance((int)(256*pngToTileFactor), -1, image.SCALE_FAST);

        Graphics2D g = this.ovl.createGraphics();
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

    // implmentation of a vector with basic vector operations.
    class SimpleVector{
        public double x;
        public double y;

        public SimpleVector(double x, double y){
            this.x = x;
            this.y = y;
        }

        public void add(SimpleVector vec){
            this.x += vec.x;
            this.y += vec.y;
        }

        public void sub(SimpleVector vec){
            this.x -= vec.x;
            this.y -= vec.y;
        }

        public void scale(double s){
            this.x *= s;
            this.y *= s;
        }
    }
}
