package integration;

import data_access.InMemoryFoodDataAccessObject;
import entity.FoodItem;
import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.add_food.AddFoodInputBoundary;
import use_case.add_food.AddFoodInteractor;
import use_case.add_food.AddFoodOutputBoundary;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the Add Food use case.
 *
 * Verifies that the controller, interactor, in-memory DAO,
 * presenter, and view model work correctly together.
 *
 * Each test uses a fresh in-memory repository to ensure
 * independence between test cases.
 */
public class AddFoodIntegrationTest {

    private InMemoryFoodDataAccessObject foodDataAccess;
    private AddFoodViewModel viewModel;
    private AddFoodController controller;

    /**
     * Initializes the real Add Food components before each test.
     */
    @BeforeEach
    void setUp() {

        foodDataAccess = new InMemoryFoodDataAccessObject();

        viewModel = new AddFoodViewModel();

        AddFoodOutputBoundary presenter =
                new AddFoodPresenter(viewModel);

        AddFoodInputBoundary interactor =
                new AddFoodInteractor(foodDataAccess, presenter);

        controller = new AddFoodController(interactor);
    }

    /**
     * Verifies that a valid food is saved in the repository
     * and that the view model receives the successful result.
     */
    @Test
    void testAddFoodSuccessfully() {

        controller.execute(
                "Cooked Rice",
                130.0,
                2.7,
                28.2,
                0.3
        );

        assertTrue(viewModel.isSuccess());
        assertEquals("Cooked Rice", viewModel.getFoodName());
        assertEquals("Cooked Rice added successfully.",
                viewModel.getMessage());

        String foodId = viewModel.getFoodId();

        assertNotNull(foodId);
        assertFalse(foodId.isEmpty());

        assertTrue(foodDataAccess.existsById(foodId));
        assertTrue(foodDataAccess.existsByName("Cooked Rice"));

        FoodItem savedFood = foodDataAccess.getFoodById(foodId);

        assertNotNull(savedFood);
        assertEquals("Cooked Rice", savedFood.getName());

        assertEquals(130.0,
                savedFood.getNutritionPer100g().getCalories(), 0.0001);
        assertEquals(2.7,
                savedFood.getNutritionPer100g().getProtein(), 0.0001);
        assertEquals(28.2,
                savedFood.getNutritionPer100g().getCarbs(), 0.0001);
        assertEquals(0.3,
                savedFood.getNutritionPer100g().getFat(), 0.0001);
    }

    /**
     * Verifies that adding a duplicate food name is rejected.
     *
     * The rejected operation must not change the repository.
     */
    @Test
    void testDuplicateFoodNameRejected() {

        controller.execute("Banana", 89, 1.1, 22.8, 0.3);

        assertTrue(viewModel.isSuccess());

        int originalSize = foodDataAccess.getAllFoods().size();

        controller.execute("Banana", 100, 2.0, 25.0, 1.0);

        assertFalse(viewModel.isSuccess());
        assertEquals(
                "A food with this name already exists.",
                viewModel.getMessage()
        );

        assertEquals(
                originalSize,
                foodDataAccess.getAllFoods().size()
        );

        // A failed operation must not expose stale success data.
        assertNull(viewModel.getFoodId());
        assertNull(viewModel.getFoodName());
    }

    /**
     * Verifies that duplicate-name checking ignores case
     * and surrounding whitespace.
     */
    @Test
    void testDuplicateFoodNameCaseInsensitive() {

        controller.execute("Chicken Breast", 165, 31, 0, 3.6);

        controller.execute(
                "  CHICKEN BREAST  ",
                200,
                30,
                0,
                5
        );

        assertFalse(viewModel.isSuccess());
        assertEquals(1, foodDataAccess.getAllFoods().size());
    }

    /**
     * Verifies that the food name is trimmed before storage.
     */
    @Test
    void testFoodNameTrimmedBeforeSaving() {

        controller.execute(
                "  Apple  ",
                52,
                0.3,
                13.8,
                0.2
        );

        assertTrue(viewModel.isSuccess());
        assertEquals("Apple", viewModel.getFoodName());

        FoodItem savedFood =
                foodDataAccess.getFoodById(viewModel.getFoodId());

        assertEquals("Apple", savedFood.getName());
    }

    /**
     * Verifies that negative nutritional values are rejected.
     */
    @Test
    void testNegativeNutritionRejected() {

        controller.execute("Invalid Food", -100, 5, 10, 2);

        assertFalse(viewModel.isSuccess());
        assertTrue(foodDataAccess.getAllFoods().isEmpty());
        assertNotNull(viewModel.getMessage());
        assertFalse(viewModel.getMessage().isEmpty());
    }

    /**
     * Verifies that an empty food name is rejected.
     */
    @Test
    void testEmptyFoodNameRejected() {

        controller.execute("   ", 100, 5, 10, 2);

        assertFalse(viewModel.isSuccess());
        assertEquals(
                "Food name cannot be empty.",
                viewModel.getMessage()
        );

        assertTrue(foodDataAccess.getAllFoods().isEmpty());
    }

    /**
     * Verifies that different foods receive different IDs
     * and are stored independently.
     */
    @Test
    void testMultipleFoodsStoredWithUniqueIds() {

        controller.execute("Apple", 52, 0.3, 13.8, 0.2);
        String firstId = viewModel.getFoodId();

        controller.execute("Banana", 89, 1.1, 22.8, 0.3);
        String secondId = viewModel.getFoodId();

        assertNotEquals(firstId, secondId);

        assertEquals(2, foodDataAccess.getAllFoods().size());

        assertNotNull(foodDataAccess.getFoodById(firstId));
        assertNotNull(foodDataAccess.getFoodById(secondId));
    }

    /**
     * Verifies that the ViewModel notifies its listeners
     * whenever an Add Food operation finishes.
     */
    @Test
    void testViewModelNotifiesListeners() {

        AtomicInteger notificationCount = new AtomicInteger();

        viewModel.addPropertyChangeListener(event -> {
            if ("addFoodResult".equals(event.getPropertyName())) {
                notificationCount.incrementAndGet();
            }
        });

        controller.execute("Apple", 52, 0.3, 13.8, 0.2);

        assertEquals(1, notificationCount.get());

        controller.execute("Apple", 52, 0.3, 13.8, 0.2);

        assertEquals(2, notificationCount.get());
    }
}
