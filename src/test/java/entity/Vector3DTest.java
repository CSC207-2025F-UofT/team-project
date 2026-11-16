package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class Vector3DTest {

    private static final double DELTA = 1e-9;

    private static void assertVector(double x, double y, double z, Vector3D actual) {
        assertEquals(x, actual.getX(), DELTA, "x");
        assertEquals(y, actual.getY(), DELTA, "y");
        assertEquals(z, actual.getZ(), DELTA, "z");
    }

    @Test
    void storesComponents() {
        assertVector(1, 2, 3, new Vector3D(1, 2, 3));
    }

    @Test
    void zeroConstantIsAllZeros() {
        assertVector(0, 0, 0, Vector3D.ZERO);
    }

    @Test
    void addAddsEachComponent() {
        Vector3D sum = new Vector3D(1, 2, 3).add(new Vector3D(4, -5, 6));
        assertVector(5, -3, 9, sum);
    }

    @Test
    void subtractSubtractsEachComponent() {
        Vector3D diff = new Vector3D(1, 2, 3).subtract(new Vector3D(4, -5, 6));
        assertVector(-3, 7, -3, diff);
    }

    @Test
    void scaleMultipliesEachComponent() {
        assertVector(2, -4, 6, new Vector3D(1, -2, 3).scale(2));
    }

    @Test
    void dotOfPerpendicularVectorsIsZero() {
        assertEquals(0, new Vector3D(1, 0, 0).dot(new Vector3D(0, 1, 0)), DELTA);
    }

    @Test
    void dotMultipliesAndSumsComponents() {
        assertEquals(32, new Vector3D(1, 2, 3).dot(new Vector3D(4, 5, 6)), DELTA);
    }

    @Test
    void lengthOfThreeFourZeroIsFive() {
        assertEquals(5, new Vector3D(3, 4, 0).length(), DELTA);
    }

    @Test
    void lengthUsesAllThreeComponents() {
        assertEquals(3, new Vector3D(1, 2, 2).length(), DELTA);
    }

    @Test
    void normalizeKeepsDirectionWithLengthOne() {
        Vector3D unit = new Vector3D(3, 4, 0).normalize();
        assertVector(0.6, 0.8, 0, unit);
        assertEquals(1, unit.length(), DELTA);
    }

    @Test
    void normalizeOfZeroVectorIsZero() {
        // Flat ground has no slope direction; physics must not crash on it.
        assertVector(0, 0, 0, Vector3D.ZERO.normalize());
    }

    @Test
    void operationsReturnNewVectorAndLeaveOriginalUnchanged() {
        Vector3D original = new Vector3D(1, 2, 3);
        Vector3D moved = original.add(new Vector3D(1, 1, 1));
        assertNotSame(original, moved);
        assertVector(1, 2, 3, original);
    }
}
