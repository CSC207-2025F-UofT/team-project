package use_case.add_clothing;

import entity.Category;
import entity.ClothingItem;
import entity.ClothingOptions;

import java.io.IOException;
import java.util.List;

/**
 * The rules for adding a clothing item: check every required field,
 * copy the picture into the app's folder, then save the item.
 */
public class AddClothingInteractor implements AddClothingInputBoundary {
    private final AddClothingDataAccessInterface dataAccess;
    private final ImageStorageInterface imageStorage;
    private final AddClothingOutputBoundary presenter;

    /**
     * Connects the interactor to its storage and presenter.
     *
     * @param dataAccess where items are saved
     * @param imageStorage where pictures are copied
     * @param presenter who shows the result
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
        }
        catch (IOException ex) {
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
                inputData.getWarmthLevels(),
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
        if (!isValidRange(input.getWarmthLevels())) {
            return "Please choose a weather range.";
        }
        if (isBlank(input.getImageSourcePath())) {
            return "Please choose a picture.";
        }
        return null;
    }

    /**
     * A range is valid if it has at least one warmth level, every level is between
     * -10 and 10, and the levels go up by one with no gaps (e.g. [2, 3, 4, 5]),
     * just like the slider produces.
     */
    private static boolean isValidRange(List<Integer> warmthLevels) {
        return warmthLevels != null && !warmthLevels.isEmpty();
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
