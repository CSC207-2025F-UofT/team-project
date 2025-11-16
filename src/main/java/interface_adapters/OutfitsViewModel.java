package interface_adapters;

import entity.Outfit;
import java.util.ArrayList;
import java.util.List;

public class SaveOutfitViewModel {

    private List<Outfit> savedOutfits = new ArrayList<>();
    private String message = "";
    private String errorMessage = "";

    public List<Outfit> getSavedOutfits() {
        return savedOutfits;
    }

    public void setSavedOutfits(List<Outfit> savedOutfits) {
        this.savedOutfits = savedOutfits;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        this.errorMessage = "";
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        this.message = "";
    }
}
