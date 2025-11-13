package entity;

public class LandmarkInfo {
    /// All these pieces of info are 'extra,' not required for the program to function
    /// These are for the user's convenience, to be obtained via Places API

    private final String address;
    private final String description;
    private final String openHours;
    /// private final String imageUrl;
    ///  Images are not a priority right now, but will we include
    /// photos in a folder for the program to simply load, or pull images from internet via an API?
    private final String type;

    public LandmarkInfo(String address, String description, String openHours, String type) {
        this.address = address;
        this.description = description;
        this.openHours = openHours;
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getOpenHours() {
        return openHours;
    }

    public String getType() {
        return type;
    }


}
