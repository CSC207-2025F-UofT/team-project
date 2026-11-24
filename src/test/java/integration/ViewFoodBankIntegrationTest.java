package integration;

import data_access.InMemoryFoodDataAccessObject;
import entity.FoodItem;
import entity.NutritionTotals;
import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodPresenter;
import interface_adapter.add_food.AddFoodViewModel;
import interface_adapter.view_food_bank.ViewFoodBankController;
import interface_adapter.view_food_bank.ViewFoodBankPresenter;
import interface_adapter.view_food_bank.ViewFoodBankViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.add_food.AddFoodInteractor;
import use_case.view_food_bank.ViewFoodBankInteractor;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the View Food Bank use case.
 *
 * Tests the interaction between Add Food and View Food Bank
 * using a shared in-memory repository.
 *
 * Uses real controllers, interactors, presenters, and
 * ViewModels rather than fake implementations.
 */
public class ViewFoodBankIntegrationTest {

    private InMemoryFoodDataAccessObject foodDataAccess;

    private AddFoodController addFoodController;
    private AddFoodViewModel addFoodViewModel;

    private ViewFoodBankController viewFoodBankController;
    private ViewFoodBankViewModel viewFoodBankViewModel;

    /**
     * Constructs the application components before each test.
     *
     * Both use cases receive the same DAO instance.
     */
    @BeforeEach
    void setUp() {

        foodDataAccess = new InMemoryFoodDataAccessObject();

        // Wire the Add Food use case.
        addFoodViewModel = new AddFoodViewModel();

        AddFoodPresenter addFoodPresenter =
                new AddFoodPresenter(addFoodViewModel);

        AddFoodInteractor addFoodInteractor =
                new AddFoodInteractor(
                        foodDataAccess,
                        addFoodPresenter
                );

        addFoodController =
                new AddFoodController(addFoodInteractor);

        // Wire the View Food Bank use case.
        viewFoodBankViewModel = new ViewFoodBankViewModel();

        ViewFoodBankPresenter viewFoodBankPresenter =
                new ViewFoodBankPresenter(viewFoodBankViewModel);

        ViewFoodBankInteractor viewFoodBankInteractor =
                new ViewFoodBankInteractor(
                        foodDataAccess,
                        viewFoodBankPresenter
                );

        viewFoodBankController =
                new ViewFoodBankController(viewFoodBankInteractor);
    }

    /**
     * Verifies that retrieving an empty food bank succeeds.
     */
    @Test
    void testEmptyFoodBank() {

        viewFoodBankController.execute();

        assertTrue(viewFoodBankViewModel.isSuccess());
        assertTrue(viewFoodBankViewModel.getFoods().isEmpty());

        assertEquals(
                "No foods have been added yet.",
                viewFoodBankViewModel.getMessage()
        );
    }

    /**
     * Verifies that a food created through Add Food
     * can be retrieved through View Food Bank.
     */
    @Test
    void testAddedFoodAppearsInFoodBank() {

        addFoodController.execute(
                "Cooked Rice", 130, 2.7, 28.2, 0.3
        );

        assertTrue(addFoodViewModel.isSuccess());

        viewFoodBankController.execute();

        assertTrue(viewFoodBankViewModel.isSuccess());

        List<FoodItem> foods = viewFoodBankViewModel.getFoods();

        assertEquals(1, foods.size());

        FoodItem rice = foods.get(0);

        assertEquals("Cooked Rice", rice.getName());
        assertEquals(
                addFoodViewModel.getFoodId(),
                rice.getId()
        );

        NutritionTotals nutrition = rice.getNutritionPer100g();

        assertEquals(130, nutrition.getCalories(), 0.0001);
        assertEquals(2.7, nutrition.getProtein(), 0.0001);
        assertEquals(28.2, nutrition.getCarbs(), 0.0001);
        assertEquals(0.3, nutrition.getFat(), 0.0001);
    }

    /**
     * Verifies that multiple foods are displayed
     * alphabetically rather than by insertion order.
     */
    @Test
    void testFoodsSortedAlphabetically() {

        addFoodController.execute(
                "Cooked Rice", 130, 2.7, 28.2, 0.3
        );

        addFoodController.execute(
                "Banana", 89, 1.1, 22.8, 0.3
        );

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        viewFoodBankController.execute();

        List<FoodItem> foods = viewFoodBankViewModel.getFoods();

        assertEquals(3, foods.size());

        assertEquals("Apple", foods.get(0).getName());
        assertEquals("Banana", foods.get(1).getName());
        assertEquals("Cooked Rice", foods.get(2).getName());

        assertEquals(
                "3 foods found.",
                viewFoodBankViewModel.getMessage()
        );
    }

    /**
     * Verifies that the Food Bank reflects new entries
     * when the retrieval use case is executed again.
     */
    @Test
    void testFoodBankUpdatesAfterAddingFood() {

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        viewFoodBankController.execute();

        assertEquals(1, viewFoodBankViewModel.getFoods().size());

        addFoodController.execute(
                "Banana", 89, 1.1, 22.8, 0.3
        );

        // Simulate refreshing or reopening the Food Bank tab.
        viewFoodBankController.execute();

        assertEquals(2, viewFoodBankViewModel.getFoods().size());
        assertEquals(
                "2 foods found.",
                viewFoodBankViewModel.getMessage()
        );
    }

    /**
     * Verifies that repeated retrieval does not create
     * duplicate food records in the repository or ViewModel.
     */
    @Test
    void testRepeatedRetrievalDoesNotDuplicateFoods() {

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        viewFoodBankController.execute();
        viewFoodBankController.execute();
        viewFoodBankController.execute();

        assertEquals(1, viewFoodBankViewModel.getFoods().size());
        assertEquals(1, foodDataAccess.getAllFoods().size());
    }

    /**
     * Verifies that duplicate Add Food requests do not
     * create extra entries in the Food Bank.
     */
    @Test
    void testDuplicateFoodIsNotDisplayedTwice() {

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        addFoodController.execute(
                "  APPLE  ", 60, 1, 15, 0.5
        );

        assertFalse(addFoodViewModel.isSuccess());

        viewFoodBankController.execute();

        assertTrue(viewFoodBankViewModel.isSuccess());
        assertEquals(1, viewFoodBankViewModel.getFoods().size());
    }

    /**
     * Verifies that the ViewModel notifies registered
     * listeners after each successful retrieval.
     */
    @Test
    void testViewModelNotifiesListeners() {

        AtomicInteger notifications = new AtomicInteger();

        viewFoodBankViewModel.addPropertyChangeListener(event -> {
            if (ViewFoodBankViewModel.FOOD_BANK_PROPERTY
                    .equals(event.getPropertyName())) {

                notifications.incrementAndGet();
            }
        });

        viewFoodBankController.execute();

        assertEquals(1, notifications.get());

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        viewFoodBankController.execute();

        assertEquals(2, notifications.get());
    }

    /**
     * Verifies that callers cannot directly modify the
     * food list exposed by the ViewModel.
     */
    @Test
    void testViewModelFoodListIsUnmodifiable() {

        addFoodController.execute(
                "Apple", 52, 0.3, 13.8, 0.2
        );

        viewFoodBankController.execute();

        List<FoodItem> foods = viewFoodBankViewModel.getFoods();

        assertThrows(
                UnsupportedOperationException.class,
                () -> foods.clear()
        );

        assertEquals(1, foodDataAccess.getAllFoods().size());
    }
}