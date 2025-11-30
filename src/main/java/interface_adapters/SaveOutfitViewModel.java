package interface_adapters;

import entities.Outfit;
import java.util.List;

/**
 * Holds UI-ready data for the view.
 */
public class SaveOutfitViewModel {
    private List<Outfit> outfits;
    private String message = "";
    private String error = "";

    public List<Outfit> getOutfits() { return outfits; }
    public void setOutfits(List<Outfit> outfits) { this.outfits = outfits; }

    public String getMessage() { return message; }
    public void setMessage(String m) { this.message = m; this.error = ""; }

    public String getError() { return error; }
    public void setError(String e) { this.error = e; this.message = ""; }
}
