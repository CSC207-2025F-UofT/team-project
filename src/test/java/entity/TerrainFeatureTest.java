package entity;

import entity.TerrainFeature.Kind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TerrainFeatureTest {

    private static final double DELTA = 1e-9;

    @Test
    void storesFields() {
        TerrainFeature ramp = new TerrainFeature(Kind.RAMP, 5, 6, 4, 10, "EAST");
        assertEquals(Kind.RAMP, ramp.getKind());
        assertEquals(5, ramp.getX(), DELTA);
        assertEquals(6, ramp.getY(), DELTA);
        assertEquals(4, ramp.getSize(), DELTA);
        assertEquals(10, ramp.getHeight(), DELTA);
        assertEquals("EAST", ramp.getDirection());
    }

    @Test
    void nonRampCanHaveNoDirection() {
        assertNull(new TerrainFeature(Kind.HILL, 0, 0, 1, 1, null).getDirection());
    }

    @Test
    void hillFallsFromCentreToEdge() {
        TerrainFeature hill = new TerrainFeature(Kind.HILL, 10, 10, 8, 20, null);
        assertEquals(20, hill.heightAt(10, 10), DELTA);
        assertEquals(10, hill.heightAt(14, 10), DELTA);
        assertEquals(0, hill.heightAt(18, 10), DELTA);
        assertEquals(0, hill.heightAt(30, 30), DELTA);
    }

    @Test
    void valleyIsNegativeHill() {
        TerrainFeature valley = new TerrainFeature(Kind.VALLEY, 10, 10, 10, 12, null);
        assertEquals(-12, valley.heightAt(10, 10), DELTA);
        assertEquals(-6, valley.heightAt(15, 10), DELTA);
        assertEquals(0, valley.heightAt(20, 10), DELTA);
    }

    @Test
    void blockIsFlatInsideSquareAndZeroOutside() {
        TerrainFeature block = new TerrainFeature(Kind.BLOCK, 10, 10, 4, 25, null);
        assertEquals(25, block.heightAt(10, 10), DELTA);
        assertEquals(25, block.heightAt(12, 8), DELTA);
        assertEquals(0, block.heightAt(12.1, 10), DELTA);
    }

    @Test
    void eastRampRisesTowardsHigherX() {
        TerrainFeature ramp = new TerrainFeature(Kind.RAMP, 10, 10, 4, 8, "EAST");
        assertEquals(0, ramp.heightAt(8, 10), DELTA);
        assertEquals(4, ramp.heightAt(10, 10), DELTA);
        assertEquals(8, ramp.heightAt(12, 10), DELTA);
        assertEquals(0, ramp.heightAt(13, 10), DELTA);
    }

    @Test
    void rampRisesInEachDirection() {
        assertEquals(8, new TerrainFeature(Kind.RAMP, 10, 10, 4, 8, "NORTH").heightAt(10, 8), DELTA);
        assertEquals(8, new TerrainFeature(Kind.RAMP, 10, 10, 4, 8, "SOUTH").heightAt(10, 12), DELTA);
        assertEquals(8, new TerrainFeature(Kind.RAMP, 10, 10, 4, 8, "WEST").heightAt(8, 10), DELTA);
    }

    @Test
    void rampWithUnknownDirectionIsFlat() {
        TerrainFeature ramp = new TerrainFeature(Kind.RAMP, 10, 10, 4, 8, "UP");
        assertEquals(0, ramp.heightAt(12, 10), DELTA);
    }
}
