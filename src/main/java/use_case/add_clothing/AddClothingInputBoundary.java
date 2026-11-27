package use_case.add_clothing;

public interface AddClothingInputBoundary {
    /** Publish the current wardrobe to the presenter. */
    void load();

    /** Validate, store the image, and save a new clothing item (UC1). */
    void addClothing(String name, String type, String sourceImagePath);
}
