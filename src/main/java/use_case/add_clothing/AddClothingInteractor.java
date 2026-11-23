package use_case.add_clothing;

import entity.Category;
import entity.ClothingItem;

import java.io.IOException;

// check every required field, copy the picture into the app's folder, then save the item.

public class AddClothingInteractor implements AddClothingInputBoundary {
    private final AddClothingDataAccessInterface dataAccess;
    private final ImageStorageInterface imageStorage;
    private final AddClothingOutputBoundary presenter;

    /**
     * dataAccess where items are saved
     * imageStorage where pictures are copied
     * presenter who shows the result
     */
    public AddClothingInteractor(AddClothingDataAccessInterface dataAccess,
                                 ImageStorageInterface imageStorage,
                                 AddClothingOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.imageStorage = imageStorage;
        this.presenter = presenter;
    }

    @Override
    public void execute(AddClothingInputData inputData) {
        final String error = findError(inputData);
        if (error != null) {
            presenter.prepareFailView(error);
            return;
        }

        final String username = dataAccess.getCurrentUsername();
        final String itemId = dataAccess.generateItemId(username);

        // Copy the picture first, so we never save an item whose picture is missing.
        final String imagePath;
        try {
            imagePath = imageStorage.saveImage(inputData.getImageSourcePath(), username, itemId);
        } catch (IOException ex) {
            presenter.prepareFailView("Couldn't save the picture. Try choosing it again.");
            return;
        }

        final ClothingItem item = new ClothingItem(
                itemId,
                inputData.getName().trim(),
                Category.valueOf(inputData.getCategory()),
                inputData.getSubCategory(),
                inputData.getColor(),
                inputData.getStyle(),
                inputData.isLayer(),
                inputData.getWeatherRating(),
                imagePath);
        dataAccess.saveItem(username, item);

        presenter.prepareSuccessView(new AddClothingOutputData(itemId, item.getName(), imagePath));
    }

    @Override
    public void switchToWelcomeView() {
        presenter.switchToWelcomeView();
    }

    /**
     * Returns the first problem with the input, or null if everything is fine.
     */
    private String findError(AddClothingInputData input) {
        if (isBlank(input.getName())) {
            return "Please enter a name.";
        }
        if (!isCategory(input.getCategory())) {
            return "Please choose a category.";
        }
        if (isBlank(input.getSubCategory())) {
            return "Please choose a type.";
        }
        if (isBlank(input.getColor())) {
            return "Please choose a colour.";
        }
        if (isBlank(input.getStyle())) {
            return "Please choose a style.";
        }
        if (input.getWeatherRating() < ClothingItem.MIN_WEATHER_RATING
                || input.getWeatherRating() > ClothingItem.MAX_WEATHER_RATING) {
            return "Weather must be between -10 and 10.";
        }
        if (isBlank(input.getImageSourcePath())) {
            return "Please choose a picture.";
        }
        return null;
    }

    private static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    private static boolean isCategory(String text) {
        if (text == null) {
            return false;
        }
        for (Category category : Category.values()) {
            if (category.name().equals(text)) {
                return true;
            }
        }
        return false;
    }
}
