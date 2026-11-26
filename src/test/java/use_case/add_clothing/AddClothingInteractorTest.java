package use_case.add_clothing;

import entity.Category;
import entity.ClothingItem;
import entity.ClothingOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests AddClothingInteractor with fake storage and a fake presenter,
 * so no window opens and no real files are touched.
 */
class AddClothingInteractorTest {
    private static final List<Integer> ALL_YEAR = ClothingOptions.WARMTH_LEVELS;
    private static final List<Integer> SPRING = Arrays.asList(1, 2, 3, 4, 5, 6);

    private FakeCloset closet;
    private FakeImages images;
    private FakePresenter presenter;
    private AddClothingInteractor interactor;

    @BeforeEach
    void setUp() {
        closet = new FakeCloset();
        images = new FakeImages();
        presenter = new FakePresenter();
        interactor = new AddClothingInteractor(closet, images, presenter);
    }

    private static AddClothingInputData validInput() {
        return new AddClothingInputData("Pink Dress Shirt", "TOP", "Long-sleeved shirt",
                "Pink", "Feminine", false, SPRING, "/Users/ginni/Downloads/shirt.png");
    }

    @Test
    void validItemIsSavedWithCopiedPicture() {
        interactor.execute(validInput());

        assertEquals(1, closet.saved.size());
        final ClothingItem item = closet.saved.get(0);
        assertEquals("c001", item.getId());
        assertEquals("Pink Dress Shirt", item.getName());
        assertEquals(Category.TOP, item.getCategory());
        assertEquals(SPRING, item.getWarmthLevels());
        assertEquals("data/images/ginni/c001.png", item.getImagePath());
        assertEquals("Pink Dress Shirt", presenter.successName);
        assertEquals(null, presenter.error);
    }

    @Test
    void nameIsTrimmed() {
        interactor.execute(new AddClothingInputData("  Grey Hoodie  ", "TOP", "Sweater",
                "Grey", "Casual", true, ALL_YEAR, "/tmp/hoodie.jpg"));
        assertEquals("Grey Hoodie", closet.saved.get(0).getName());
    }

    @Test
    void emptyNameFails() {
        interactor.execute(new AddClothingInputData("   ", "TOP", "Tank", "Black", "Goth",
                false, ALL_YEAR, "/tmp/a.png"));
        assertNothingSaved("Please enter a name.");
    }

    @Test
    void missingCategoryFails() {
        interactor.execute(new AddClothingInputData("Jeans", "", "Baggy jeans", "Blue",
                "Casual", false, ALL_YEAR, "/tmp/a.png"));
        assertNothingSaved("Please choose a category.");
    }

    @Test
    void missingTypeFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "", "Blue",
                "Casual", false, ALL_YEAR, "/tmp/a.png"));
        assertNothingSaved("Please choose a type.");
    }

    @Test
    void missingColourFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "",
                "Casual", false, ALL_YEAR, "/tmp/a.png"));
        assertNothingSaved("Please choose a colour.");
    }

    @Test
    void missingStyleFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "", false, ALL_YEAR, "/tmp/a.png"));
        assertNothingSaved("Please choose a style.");
    }

    @Test
    void emptyWeatherRangeFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "Casual", false, Collections.emptyList(), "/tmp/a.png"));
        assertNothingSaved("Please choose a weather range.");
    }

    @Test
    void levelAbove10Fails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "Casual", false, Arrays.asList(9, 10, 11), "/tmp/a.png"));
        assertNothingSaved("Please choose a weather range.");
    }

    @Test
    void rangeWithAGapFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "Casual", false, Arrays.asList(0, 6, 7), "/tmp/a.png"));
        assertNothingSaved("Please choose a weather range.");
    }

    @Test
    void rangeOutOfOrderFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "Casual", false, Arrays.asList(5, 1), "/tmp/a.png"));
        assertNothingSaved("Please choose a weather range.");
    }

    @Test
    void missingPictureFails() {
        interactor.execute(new AddClothingInputData("Jeans", "BOTTOM", "Baggy jeans", "Blue",
                "Casual", false, ALL_YEAR, ""));
        assertNothingSaved("Please choose a picture.");
    }

    @Test
    void pictureCopyFailureSavesNothing() {
        images.fail = true;
        interactor.execute(validInput());
        assertNothingSaved("Couldn't save the picture. Try choosing it again.");
    }

    @Test
    void cancelGoesToWelcome() {
        interactor.switchToWelcomeView();
        assertTrue(presenter.switchedToWelcome);
    }

    private void assertNothingSaved(String expectedError) {
        assertTrue(closet.saved.isEmpty());
        assertFalse(images.copied);
        assertEquals(expectedError, presenter.error);
        assertEquals(null, presenter.successName);
    }

    // ---------- fakes ----------

    private static class FakeCloset implements AddClothingDataAccessInterface {
        private final List<ClothingItem> saved = new ArrayList<>();

        @Override
        public String getCurrentUsername() {
            return "ginni";
        }

        @Override
        public String generateItemId(String username) {
            return String.format("c%03d", saved.size() + 1);
        }

        @Override
        public void saveItem(String username, ClothingItem item) {
            saved.add(item);
        }
    }

    private static class FakeImages implements ImageStorageInterface {
        private boolean fail;
        private boolean copied;

        @Override
        public String saveImage(String sourcePath, String username, String itemId) throws IOException {
            if (fail) {
                throw new IOException("disk full");
            }
            copied = true;
            return "data/images/" + username + "/" + itemId + ".png";
        }
    }

    private static class FakePresenter implements AddClothingOutputBoundary {
        private String successName;
        private String error;
        private boolean switchedToWelcome;

        @Override
        public void prepareSuccessView(AddClothingOutputData outputData) {
            successName = outputData.getItemName();
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }

        @Override
        public void switchToWelcomeView() {
            switchedToWelcome = true;
        }
    }
}
