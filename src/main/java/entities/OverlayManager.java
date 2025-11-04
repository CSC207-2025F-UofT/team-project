package entities;

import java.util.ArrayList;
import java.util.HashMap;

public class OverlayManager {
    private final ArrayList<Overlay> overlays; //
    private final HashMap<String, Integer> typeToIndex;
    private String selected;

    public OverlayManager(){
        this.overlays = new ArrayList<Overlay>();
        this.typeToIndex = new HashMap<>();
        this.selected = null;
    }

    public void addOverlay(Overlay ovl){
        this.overlays.add(ovl);
        this.typeToIndex.put(ovl.getType(), this.overlays.size() - 1);
    }

    public void setSelected(String selection) throws LayerNotFoundException {
        // Change the selected layer. Raise LayerNotFoundException if selection is not an added overlay type.
        if(this.typeToIndex.containsKey(selection)) {
            this.selected = selection;
        }
        else {throw new LayerNotFoundException(selection);}
    }

    public double getSelectedOpacity(){
        // Return the selected layer's opacity
        return this.overlays.get(this.typeToIndex.get(this.selected)).getOpactiy();
    }

}
class LayerNotFoundException extends Exception {
    public LayerNotFoundException(String layer) {
        super("Layer " + layer + " not found!");
    }
}

