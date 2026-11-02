package terrainscene;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import terrain.Biome;
import terrain.BlockPos;
import terrain.BlockType;
import terrain.Chunk;
import terrain.OverworldTerrain;
import terrain.Terrain;

class SunShadowsTest {

  private static final int PER_LEVEL = SunShadows.LENGTH_PER_LEVEL_PX;

  /** Flat terrain at height 1 everywhere, except for heights set by the test. */
  private static final class FakeTerrain implements Terrain {
    private final Map<String, Integer> heights = new HashMap<>();

    FakeTerrain set(int blockX, int blockY, int height) {
      heights.put(blockX + "," + blockY, height);
      return this;
    }

    @Override
    public int heightAt(int blockX, int blockY) {
      return heights.getOrDefault(blockX + "," + blockY, 1);
    }

    @Override
    public BlockType blockAt(int blockX, int blockY) {
      return BlockType.GRASS;
    }

    @Override
    public Biome biomeAt(int blockX, int blockY) {
      return Biome.PLAINS;
    }

    @Override
    public Chunk chunkAt(int chunkX, int chunkY) {
      throw new UnsupportedOperationException();
    }

    @Override
    public BlockPos spawnPoint() {
      return new BlockPos(0, 0);
    }
  }

  @Test
  void flatGroundHasNoShadows() {
    SunShadows shadows = new SunShadows(new FakeTerrain());
    assertEquals(0, shadows.shadowWidth(0, 0));
  }

  @Test
  void oneLevelStepShadesTheTileToItsLeftByOneLength() {
    SunShadows shadows = new SunShadows(new FakeTerrain().set(1, 0, 2));
    assertEquals(PER_LEVEL, shadows.shadowWidth(0, 0));
    // Light comes from the right, so nothing falls on the tile to the right of the step.
    assertEquals(0, shadows.shadowWidth(2, 0));
    // Only the same row is shaded.
    assertEquals(0, shadows.shadowWidth(0, 1));
  }

  @Test
  void tallCliffShadowCoversAWholeTileAndSpillsOntoTheNext() {
    // Three levels up: a 96 px shadow covers the 64 px tile next door and 32 px of the one after.
    SunShadows shadows = new SunShadows(new FakeTerrain().set(2, 0, 4));
    assertEquals(64, shadows.shadowWidth(1, 0));
    assertEquals(3 * PER_LEVEL - 64, shadows.shadowWidth(0, 0));
    assertEquals(0, shadows.shadowWidth(-1, 0));
  }

  @Test
  void tilesAsHighAsTheCasterAreNotShaded() {
    SunShadows shadows = new SunShadows(new FakeTerrain().set(1, 0, 3).set(0, 0, 3));
    assertEquals(0, shadows.shadowWidth(0, 0));
  }

  @Test
  void widestShadowFromAnyCasterWins() {
    // A small step right next door, and a tall cliff two tiles away.
    FakeTerrain terrain = new FakeTerrain().set(1, 0, 2).set(2, 0, 6);
    SunShadows shadows = new SunShadows(terrain);
    assertEquals(64, shadows.shadowWidth(0, 0));
  }

  @Test
  void realTerrainShadowsAreAlwaysValidWidths() {
    SunShadows shadows = new SunShadows(OverworldTerrain.fromSeed(42L));
    int shaded = 0;
    for (int blockX = -300; blockX < 300; blockX += 3) {
      for (int blockY = -300; blockY < 300; blockY += 7) {
        int width = shadows.shadowWidth(blockX, blockY);
        assertTrue(width >= 0 && width <= 64, "width " + width);
        assertEquals(0, width % SunShadows.WIDTH_GRANULARITY_PX);
        if (width > 0) {
          shaded++;
        }
      }
    }
    assertTrue(shaded > 0, "hilly terrain should cast some shadows");
  }
}
