package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the NutritionTotals entity.
 */
class NutritionTotalsTest {

    @Test
    void testConstructorAndGetters() {
        NutritionTotals nutrition =
                new NutritionTotals(200, 10, 25, 6);

        assertEquals(200, nutrition.getCalories());
        assertEquals(10, nutrition.getProtein());
        assertEquals(25, nutrition.getCarbs());
        assertEquals(6, nutrition.getFat());
    }

    @Test
    void testAddNutrition() {
        NutritionTotals first =
                new NutritionTotals(100, 5, 15, 2);

        NutritionTotals second =
                new NutritionTotals(200, 10, 20, 4);

        NutritionTotals result = first.add(second);

        assertEquals(300, result.getCalories());
        assertEquals(15, result.getProtein());
        assertEquals(35, result.getCarbs());
        assertEquals(6, result.getFat());
    }

    @Test
    void testScaleNutrition() {
        NutritionTotals original =
                new NutritionTotals(100, 5, 20, 2);

        NutritionTotals scaled = original.scale(2.5);

        assertEquals(250, scaled.getCalories());
        assertEquals(12.5, scaled.getProtein());
        assertEquals(50, scaled.getCarbs());
        assertEquals(5, scaled.getFat());
    }

    @Test
    void testOriginalUnchangedAfterScaling() {
        NutritionTotals original =
                new NutritionTotals(100, 5, 20, 2);

        original.scale(3);

        // Scaling must not modify the original object.
        assertEquals(100, original.getCalories());
        assertEquals(5, original.getProtein());
    }

    @Test
    void testScaleByZero() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        NutritionTotals result = nutrition.scale(0);

        assertEquals(0, result.getCalories());
        assertEquals(0, result.getProtein());
    }

    @Test
    void testRejectNegativeValues() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new NutritionTotals(-1, 5, 20, 2)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new NutritionTotals(100, -1, 20, 2)
        );
    }

    @Test
    void testRejectNonFiniteValues() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new NutritionTotals(
                        Double.NaN, 5, 20, 2)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new NutritionTotals(
                        100, Double.POSITIVE_INFINITY, 20, 2)
        );
    }

    @Test
    void testRejectInvalidScaleFactor() {
        NutritionTotals nutrition =
                new NutritionTotals(100, 5, 20, 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> nutrition.scale(-1)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> nutrition.scale(Double.NaN)
        );
    }
}
