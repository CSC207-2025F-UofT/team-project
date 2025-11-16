package use_case;

import java.util.List;

public class SaveOutfitInputData {
    private final String outfitName;
    private final List<String> items;
    private final String weatherProfile;
    private final String location;
    private final boolean overwrite;

    public SaveOutfitInputData(String outfitName,
                               List<String> items,
                               String weatherProfile,
                               String location,
                               boolean overwrite) {
        this.outfitName = outfitName;
        this.items = items;
        this.weatherProfile = weatherProfile;
        this.location = location;
        this.overwrite = overwrite;
    }

    public String getOutfitName() { return outfitName; }
    public List<String> getItems() { return items; }
    public String getWeatherProfile() { return weatherProfile; }
    public String getLocation() { return location; }
    public boolean isOverwrite() { return overwrite; }
}
