package usecase;

import entity.*;

import java.util.ArrayList;

public final class UpdateOverlayUseCase {
    private final OverlayManager overlayManager;
    //cache
    //info on time

    //TODO add cache and time info
    public UpdateOverlayUseCase(OverlayManager om){this.overlayManager = om;}

    //TODO add viewport
    public void update(){
        double zoom = 1.0; //view.getZoom();
        Location tl = new Location(-10, -10);
        Location br = new Location(10, 10);
        BoundingBox bBox = new BoundingBox(tl, br);

        //Convert to tile coords,
        //lat lon as bounding box, convert lat lon to 0-1. //Move this to boundingbox entity?
        double bBoxLX = convertLatitude(bBox.getTopLeft().getLatitude());
        double bBoxRX = convertLatitude(bBox.getBottomRight().getLatitude());
        double bBoxLY = convertLongitude(bBox.getTopLeft().getLongitude());
        double bBoxRY = convertLongitude(bBox.getBottomRight().getLongitude());

        Vector topLeft = new Vector(bBoxLX, bBoxLY);
        Vector botRight = new Vector(bBoxRX, bBoxRY);

        //convert bounding box vecs to tile grid coords based on zoom (0-6, dimension are 2^z)
        topLeft.scale(Math.pow(2, zoom));
        botRight.scale(Math.pow(2, zoom));

        //get amount of visible tiles in both direction (vp might not be a square)
        int visibleTilesX = (int)botRight.x - (int)topLeft.x + 1; //(15.1 to 15.6, sill 1 tile visible)
        int visibleTilesY = (int)topLeft.y- (int)botRight.y + 1;
        ArrayList<Tile> tileList = new ArrayList<>();

        for(int i = 0; i < visibleTilesX; i++){
            for(int j = 0; j < visibleTilesY; j++){
                //Tile tile = cache.fetchTile(this.overayManager.getSelected(), (int)topLeft.x + i, (int)topLeft.y + j)
                //tileList.add(tile)
            }
        }
        this.overlayManager.updateOverlay(topLeft, botRight, tileList);
        //output.setoverlay(this.overlayManager.getOverlay());
    }

    // convert both lat and lon to a value between 0-1, 0 being -180 or 90, 1 being 180 or -90.
    // for lon, the direction is reversed as in image processing the Y axis goes from top to bottom.
    //Move this to boundingbox entity?
    private double convertLatitude(double lat){
        return (lat + 180) / 360;
    }

    private double convertLongitude(double lon){
        return (-1 * lon + 90) / 180;
    }
}
