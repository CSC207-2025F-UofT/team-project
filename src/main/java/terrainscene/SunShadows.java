package terrainscene;

import terrain.Terrain;
import terrain.TerrainCoordinates;

/**
 * Works out the shadows that raised terrain casts, for a sun shining from the right of the
 * screen.
 *
 * <p>Light comes from the right, so every tile casts its shadow to the left, onto any lower tiles
 * there. Each level of height difference throws the shadow {@link #LENGTH_PER_LEVEL_PX} pixels
 * further. A shadow can reach past the tile next door onto tiles further left. This class only
 * does the geometry; it draws nothing.
 */
public final class SunShadows {

  /**
   * How far the shadow of one level of height reaches, in pixels. Equal to a terrace step's
   * height, which is a sun 45 degrees above the horizon. A later time-of-day feature can vary it.
   */
  static final int LENGTH_PER_LEVEL_PX = TileRenderable.STEP_HEIGHT_PX;

  /** Shadow widths are rounded to this many pixels, so a few strip images cover every case. */
  static final int WIDTH_GRANULARITY_PX = 8;

  /** How many tiles to the right to look for something tall enough to shade a tile. */
  static final int MAX_CASTER_DISTANCE = 8;

  private static final int BLOCK = TerrainCoordinates.BLOCK_SIZE_PX;

  private final Terrain terrain;

  /**
   * Creates shadow geometry for some terrain.
   *
   * @param terrain the terrain whose heights cast the shadows
   */
  public SunShadows(Terrain terrain) {
    this.terrain = terrain;
  }

  /**
   * Returns how much of a tile's top is in shadow, measured from its right edge.
   *
   * <p>Checks each tile to the right, up to {@link #MAX_CASTER_DISTANCE} away. A tile {@code k}
   * places to the right that stands {@code d} levels higher throws a shadow {@code d *
   * LENGTH_PER_LEVEL_PX} pixels long from its own left edge, which covers this tile by that
   * length minus the {@code k - 1} whole tiles in between. The widest coverage wins.
   *
   * @param blockX the tile's x coordinate, in blocks
   * @param blockY the tile's y coordinate, in blocks
   * @return the shadow's width in pixels: 0 for none, at most a full tile, and otherwise a
   *     multiple of {@link #WIDTH_GRANULARITY_PX}
   */
  public int shadowWidth(int blockX, int blockY) {
    int height = terrain.heightAt(blockX, blockY);
    int widest = 0;
    for (int distance = 1; distance <= MAX_CASTER_DISTANCE; distance++) {
      int rise = terrain.heightAt(blockX + distance, blockY) - height;
      int reach = rise * LENGTH_PER_LEVEL_PX - (distance - 1) * BLOCK;
      widest = Math.max(widest, reach);
    }
    return roundWidth(Math.min(widest, BLOCK));
  }

  /** Rounds a width to the nearest multiple of the granularity, keeping any shadow visible. */
  private static int roundWidth(int width) {
    if (width <= 0) {
      return 0;
    }
    int rounded =
        Math.round((float) width / WIDTH_GRANULARITY_PX) * WIDTH_GRANULARITY_PX;
    return Math.max(WIDTH_GRANULARITY_PX, rounded);
  }
}
