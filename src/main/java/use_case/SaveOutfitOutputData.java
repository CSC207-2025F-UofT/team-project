package use_case;

import entity.Outfit;
import java.util.List;

public class SaveOutfitOutputData {
    private final String message;
    private final List<Outfit> allOutfits;

    public SaveOutfitOutputData(String message, List<Outfit> allOutfits) {
        this.message = message;
        this.allOutfits = allOutfits;
    }

    public String getMessage() {
        return message;
    }

    public List<Outfit> getAllOutfits() {
        return allOutfits;
    }
}
