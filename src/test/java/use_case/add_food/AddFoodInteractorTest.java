package use_case.add_food;

import entity.FoodItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Add Food use case interactor.
 *
 * Uses fake implementations of the data access and output
 * boundaries to test business logic independently of the
 * actual persistence layer and user interface.
 */
class AddFoodInteractorTest {

    private FakeFoodDataAccess dataAccess;
    private FakeAddFoodPresenter presenter;
    private AddFoodInteractor interactor;

    /**
     * Creates fresh dependencies before each test.
     *
     * This ensures tests do not affect one another.
     */
    @BeforeEach
    void setUp() {
        dataAccess = new FakeFoodDataAccess();
        presenter = new FakeAddFoodPresenter();

        interactor = new AddFoodInteractor(
                dataAccess,
                presenter
        );
    }

    @Test
    void testAddValidFood() {
        AddFoodInputData input = createValidInput();

        interactor.execute(input);

        assertTrue(presenter.successCalled);
        assertFalse(presenter.failureCalled);
        assertEquals(1, dataAccess.foods.size());

        AddFoodOutputData output = presenter.outputData;

        assertEquals("Cooked Rice", output.getFoodName());
        assertEquals(130, output.getCalories());
        assertEquals(2.7, output.getProtein());
        assertEquals(28.2, output.getCarbs());
        assertEquals(0.3, output.getFat());

        assertNotNull(output.getFoodId());
        assertFalse(output.getFoodId().isBlank());

        // Verify that the generated food was actually stored.
        assertTrue(dataAccess.existsById(output.getFoodId()));
    }

    @Test
    void testRejectDuplicateFoodName() {
        interactor.execute(createValidInput());

        // Attempt to add another food with the same name.
        interactor.execute(new AddFoodInputData(
                "Cooked Rice", 150, 3, 30, 1
        ));

        assertTrue(presenter.failureCalled);
        assertEquals(
                "A food with this name already exists.",
                presenter.errorMessage
        );

        // The original food should remain unchanged.
        assertEquals(1, dataAccess.foods.size());
    }

    @Test
    void testRejectDuplicateNameIgnoringCaseAndSpaces() {
        interactor.execute(createValidInput());

        interactor.execute(new AddFoodInputData(
                "  cooked rice  ", 150, 3, 30, 1
        ));

        assertTrue(presenter.failureCalled);
        assertEquals(1, dataAccess.foods.size());
    }

    @Test
    void testRejectBlankFoodName() {
        interactor.execute(new AddFoodInputData(
                "   ", 130, 2.7, 28.2, 0.3
        ));

        assertTrue(presenter.failureCalled);
        assertEquals(
                "Food name cannot be empty.",
                presenter.errorMessage
        );

        assertTrue(dataAccess.foods.isEmpty());
    }

    @Test
    void testRejectNegativeCalories() {
        interactor.execute(new AddFoodInputData(
                "Rice", -100, 2.7, 28.2, 0.3
        ));

        assertTrue(presenter.failureCalled);
        assertTrue(dataAccess.foods.isEmpty());
    }

    @Test
    void testRejectNaNNutrition() {
        interactor.execute(new AddFoodInputData(
                "Rice", Double.NaN, 2.7, 28.2, 0.3
        ));

        assertTrue(presenter.failureCalled);
        assertTrue(dataAccess.foods.isEmpty());
    }

    @Test
    void testRejectNullInput() {
        interactor.execute(null);

        assertTrue(presenter.failureCalled);
        assertEquals(
                "Food information cannot be null.",
                presenter.errorMessage
        );

        assertTrue(dataAccess.foods.isEmpty());
    }

    @Test
    void testTrimFoodNameBeforeSaving() {
        interactor.execute(new AddFoodInputData(
                "  Cooked Rice  ", 130, 2.7, 28.2, 0.3
        ));

        assertTrue(presenter.successCalled);
        assertEquals(
                "Cooked Rice",
                presenter.outputData.getFoodName()
        );
    }

    @Test
    void testGeneratedIdsAreDifferent() {
        interactor.execute(createValidInput());

        String firstId = presenter.outputData.getFoodId();

        interactor.execute(new AddFoodInputData(
                "Chicken Breast", 165, 31, 0, 3.6
        ));

        String secondId = presenter.outputData.getFoodId();

        assertNotEquals(firstId, secondId);
        assertEquals(2, dataAccess.foods.size());
    }

    @Test
    void testRepositoryRejectsSaving() {
        dataAccess.rejectSaves = true;

        interactor.execute(createValidInput());

        assertTrue(presenter.failureCalled);
        assertEquals(
                "Food could not be saved.",
                presenter.errorMessage
        );
        assertTrue(dataAccess.foods.isEmpty());
    }

    @Test
    void testRejectNullDependencies() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new AddFoodInteractor(null, presenter)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new AddFoodInteractor(dataAccess, null)
        );
    }

    /**
     * Creates standard valid input for reuse across tests.
     *
     * @return valid Add Food input data
     */
    private AddFoodInputData createValidInput() {
        return new AddFoodInputData(
                "Cooked Rice", 130, 2.7, 28.2, 0.3
        );
    }

    /**
     * Fake food repository used for interactor testing.
     *
     * It stores foods in memory and enforces unique
     * IDs and normalized food names.
     */
    private static class FakeFoodDataAccess
            implements AddFoodDataAccessInterface {

        private final Map<String, FoodItem> foods =
                new HashMap<>();

        private boolean rejectSaves = false;

        @Override
        public boolean existsById(String foodId) {
            return foods.containsKey(foodId);
        }

        @Override
        public boolean existsByName(String name) {
            String normalizedName = normalize(name);

            for (FoodItem food : foods.values()) {
                if (normalize(food.getName())
                        .equals(normalizedName)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void addFood(FoodItem food) {
            if (rejectSaves) {
                throw new IllegalArgumentException(
                        "Food could not be saved.");
            }

            if (existsById(food.getId())
                    || existsByName(food.getName())) {
                throw new IllegalArgumentException(
                        "Food ID or name already exists.");
            }

            foods.put(food.getId(), food);
        }

        /**
         * Normalizes food names for duplicate detection.
         *
         * @param name food name to normalize
         * @return normalized name
         */
        private String normalize(String name) {
            return name.trim().toLowerCase(Locale.ROOT);
        }
    }

    /**
     * Fake presenter used to capture interactor output.
     *
     * Rather than updating a Swing view model, it stores
     * the success or failure result for assertions.
     */
    private static class FakeAddFoodPresenter
            implements AddFoodOutputBoundary {

        private boolean successCalled;
        private boolean failureCalled;

        private AddFoodOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                AddFoodOutputData outputData) {
            successCalled = true;
            failureCalled = false;

            this.outputData = outputData;
            this.errorMessage = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            failureCalled = true;
            successCalled = false;

            this.errorMessage = errorMessage;
            this.outputData = null;
        }
    }
}
