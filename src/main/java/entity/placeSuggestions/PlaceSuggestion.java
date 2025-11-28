package entity.placeSuggestions;

public class PlaceSuggestion {
    private String placeId;
    private String mainText;
    private String secondaryText;

    // Constructor
    public PlaceSuggestion(String placeId, String mainText, String secondaryText) {
        this.placeId = placeId;
        this.mainText = mainText;
        this.secondaryText = secondaryText;
    }

    // Getters
    public String getPlaceId() { return placeId; }
    public String getMainText() { return mainText; }
    public String getSecondaryText() { return secondaryText; }

    @Override
    public String toString() {
        return mainText + " - " + secondaryText + " (" + placeId + ")";
    }
}