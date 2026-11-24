package use_case.view_food_bank;

import entity.FoodItem;
import entity.NutritionTotals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the View Food Bank interactor.
 *
 * Uses fake data access and output boundary implementations
 * to verify the use case independently of concrete storage
 * and presentation components.
 */
public class ViewFoodBankInteractorTest {

    private FakeFoodDataAccess foodDataAccess;
    private FakeOutputBoundary outputBoundary;
    private ViewFoodBankInteractor interactor;

    /**
     * Creates fresh fake dependencies before each test.
     */
    @BeforeEach
    void setUp() {
        foodDataAccess = new FakeFoodDataAccess();
        outputBoundary = new FakeOutputBoundary();

        interactor = new ViewFoodBankInteractor(
                foodDataAccess,
                outputBoundary
        );
    }

    /**
     * Verifies that retrieved foods are passed to the
     * output boundary in alphabetical order.
     */
    @Test
    void testFoodsSortedAlphabetically() {

        foodDataAccess.foods.add(createFood("1", "Banana"));
        foodDataAccess.foods.add(createFood("2", "Apple"));
        foodDataAccess.foods.add(createFood("3", "Carrot"));

        interactor.execute();

        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failureCalled);
        assertNotNull(outputBoundary.outputData);

        List<FoodItem> result = outputBoundary.outputData.getFoods();

        assertEquals(3, result.size());
        assertEquals("Apple", result.get(0).getName());
        assertEquals("Banana", result.get(1).getName());
        assertEquals("Carrot", result.get(2).getName());
    }

    /**
     * Verifies that alphabetical sorting ignores capitalization.
     */
    @Test
    void testSortingIsCaseInsensitive() {

        foodDataAccess.foods.add(createFood("1", "banana"));
        foodDataAccess.foods.add(createFood("2", "Apple"));
        foodDataAccess.foods.add(createFood("3", "carrot"));

        interactor.execute();

        List<FoodItem> result = outputBoundary.outputData.getFoods();

        assertEquals("Apple", result.get(0).getName());
        assertEquals("banana", result.get(1).getName());
        assertEquals("carrot", result.get(2).getName());
    }

    /**
     * Verifies that an empty repository produces a
     * successful result containing an empty list.
     */
    @Test
    void testEmptyFoodBankIsSuccessful() {

        interactor.execute();

        assertTrue(outputBoundary.successCalled);
        assertFalse(outputBoundary.failureCalled);
        assertNotNull(outputBoundary.outputData);
        assertTrue(outputBoundary.outputData.getFoods().isEmpty());
    }

    /**
     * Verifies that the interactor does not modify
     * the repository's original list when sorting.
     */
    @Test
    void testOriginalRepositoryOrderUnchanged() {

        foodDataAccess.foods.add(createFood("1", "Carrot"));
        foodDataAccess.foods.add(createFood("2", "Apple"));
        foodDataAccess.foods.add(createFood("3", "Banana"));

        interactor.execute();

        assertEquals("Carrot", foodDataAccess.foods.get(0).getName());
        assertEquals("Apple", foodDataAccess.foods.get(1).getName());
        assertEquals("Banana", foodDataAccess.foods.get(2).getName());

        List<FoodItem> result = outputBoundary.outputData.getFoods();

        assertEquals("Apple", result.get(0).getName());
        assertEquals("Banana", result.get(1).getName());
        assertEquals("Carrot", result.get(2).getName());
    }

    /**
     * Verifies that the interactor works when the DAO
     * returns an unmodifiable list.
     */
    @Test
    void testUnmodifiableRepositoryList() {

        foodDataAccess.foods.add(createFood("1", "Banana"));
        foodDataAccess.foods.add(createFood("2", "Apple"));

        foodDataAccess.returnUnmodifiableList = true;

        assertDoesNotThrow(() -> interactor.execute());

        assertTrue(outputBoundary.successCalled);

        List<FoodItem> result = outputBoundary.outputData.getFoods();

        assertEquals("Apple", result.get(0).getName());
        assertEquals("Banana", result.get(1).getName());
    }

    /**
     * Verifies that a repository failure is reported
     * through the output boundary.
     */
    @Test
    void testRepositoryFailure() {

        foodDataAccess.throwException = true;

        interactor.execute();

        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failureCalled);
        assertEquals(
                "Unable to retrieve the food bank.",
                outputBoundary.errorMessage
        );
    }

    /**
     * Verifies that a null list returned by the
     * repository is treated as a failure.
     */
    @Test
    void testNullRepositoryResult() {

        foodDataAccess.returnNull = true;

        interactor.execute();

        assertFalse(outputBoundary.successCalled);
        assertTrue(outputBoundary.failureCalled);
        assertEquals(
                "Unable to retrieve the food bank.",
                outputBoundary.errorMessage
        );
    }

    /**
     * Verifies that missing constructor dependencies
     * are rejected.
     */
    @Test
    void testNullDependenciesRejected() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ViewFoodBankInteractor(
                        null, outputBoundary)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ViewFoodBankInteractor(
                        foodDataAccess, null)
        );
    }

    /**
     * Verifies that a successful operation invokes the
     * presenter exactly once.
     */
    @Test
    void testPresenterCalledExactlyOnce() {

        foodDataAccess.foods.add(createFood("1", "Apple"));

        interactor.execute();

        assertEquals(1, outputBoundary.successCount);
        assertEquals(0, outputBoundary.failureCount);
    }

    /**
     * Verifies that the result remains independent of
     * subsequent modifications to the repository list.
     */
    @Test
    void testOutputDataIsDefensiveCopy() {

        foodDataAccess.foods.add(createFood("1", "Apple"));

        interactor.execute();

        foodDataAccess.foods.add(createFood("2", "Banana"));

        List<FoodItem> result = outputBoundary.outputData.getFoods();

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());
    }

    /**
     * Creates a valid food item for testing.
     *
     * @param id unique food ID
     * @param name food name
     * @return new food item
     */
    private FoodItem createFood(String id, String name) {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 15, 2);

        return new FoodItem(id, name, nutrition);
    }

    /**
     * Fake repository for View Food Bank tests.
     *
     * Allows each test to control the returned data
     * and simulate different repository behaviors.
     */
    private static class FakeFoodDataAccess
            implements ViewFoodBankDataAccessInterface {

        private final List<FoodItem> foods = new ArrayList<>();

        private boolean throwException = false;
        private boolean returnNull = false;
        private boolean returnUnmodifiableList = false;

        @Override
        public List<FoodItem> getAllFoods() {

            if (throwException) {
                throw new IllegalStateException(
                        "Simulated repository failure.");
            }

            if (returnNull) {
                return null;
            }

            if (returnUnmodifiableList) {
                return Collections.unmodifiableList(foods);
            }

            return foods;
        }
    }

    /**
     * Fake presenter for View Food Bank tests.
     *
     * Records the information passed by the interactor,
     * allowing tests to verify success and failure calls.
     */
    private static class FakeOutputBoundary
            implements ViewFoodBankOutputBoundary {

        private boolean successCalled = false;
        private boolean failureCalled = false;

        private int successCount = 0;
        private int failureCount = 0;

        private ViewFoodBankOutputData outputData;
        private String errorMessage;

        @Override
        public void prepareSuccessView(
                ViewFoodBankOutputData outputData) {

            successCalled = true;
            successCount++;
            this.outputData = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {

            failureCalled = true;
            failureCount++;
            this.errorMessage = errorMessage;
        }
    }
}
