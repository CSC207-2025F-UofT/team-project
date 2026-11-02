package terrainscene;

import game.gameModel.Renderable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import terrain.BlockPos;
import terrain.BlockType;
import terrain.Terrain;
import terrain.TerrainCoordinates;

/**
 * The terrain around the player, ready for the renderer to draw: tile tops, the terrace faces
 * that make hills look raised, the shadows they cast, dark rims along every drop-off, and ambient
 * occlusion at the foot of every wall.
 *
 * <p>Call {@link #update} with the player's position before each frame. Tiles are rebuilt only
 * when the player moves into a different block, and tiles that stay on screen are reused, so
 * each placeholder image is loaded once while it is visible.
 *
 * <p>The renderer draws the first sprite it receives on top. Iteration therefore starts with the
 * row nearest the camera (lowest y), so nearer tiles cover the ones behind them. Within a tile,
 * its rims come first, then its shadow, then its occlusion (all three lie over the tile), then its
 * top, then its faces, each step preceded by the overlay that darkens it.
 */
public final class TerrainScene implements Iterable<Renderable> {

  /** Creates the drawable pieces of the terrain; lets tests avoid loading images. */
  public interface TileFactory {

    /**
     * Creates a tile's top surface.
     *
     * @param blockX the tile's x coordinate, in blocks
     * @param blockY the tile's y coordinate, in blocks
     * @param type the block type at that position
     * @param height the tile's terrace height
     * @return a renderable for the tile's top
     */
    Renderable top(int blockX, int blockY, BlockType type, int height);

    /**
     * Creates one step of a tile's front face.
     *
     * @param blockX the tile's x coordinate, in blocks
     * @param blockY the tile's y coordinate, in blocks
     * @param tileType the block type on top of the tile
     * @param level the height level where this step starts
     * @param topStep whether this is the highest step, directly under the tile
     * @return a renderable for that step of the face
     */
    Renderable face(int blockX, int blockY, BlockType tileType, int level, boolean topStep);

    /**
     * Creates a shadow lying on a tile's top.
     *
     * @param blockX the shaded tile's x coordinate, in blocks
     * @param blockY the shaded tile's y coordinate, in blocks
     * @param height the shaded tile's terrace height
     * @param widthPx how much of the tile is shaded, from its right edge, in pixels
     * @return a renderable for the shadow
     */
    Renderable shadow(int blockX, int blockY, int height, int widthPx);

    /**
     * Creates a rim along one edge of a tile's top.
     *
     * @param blockX the tile's x coordinate, in blocks
     * @param blockY the tile's y coordinate, in blocks
     * @param type the tile's block type, which picks the rim colour
     * @param height the tile's terrace height
     * @param side which edge the rim runs along
     * @return a renderable for the rim
     */
    Renderable rim(int blockX, int blockY, BlockType type, int height, Edge side);

    /**
     * Creates an ambient-occlusion gradient along one edge of a tile's top.
     *
     * @param blockX the tile's x coordinate, in blocks
     * @param blockY the tile's y coordinate, in blocks
     * @param height the tile's terrace height
     * @param edge the edge where the tile meets higher ground: back, left or right
     * @return a renderable for the gradient
     */
    Renderable occlusion(int blockX, int blockY, int height, Edge edge);

    /**
     * Creates the overlay that darkens one face step, so sides look darker than tops.
     *
     * @param blockX the tile's x coordinate, in blocks
     * @param blockY the tile's y coordinate, in blocks
     * @param level which step it covers
     * @return a renderable for the overlay
     */
    Renderable faceShade(int blockX, int blockY, int level);
  }

  /** Extra blocks drawn past each screen edge, so no gap shows while moving. */
  static final int MARGIN_BLOCKS = 1;

  /**
   * Extra rows drawn below the screen, because raised tiles there are lifted up into view. Covers
   * heights up to {@code ROWS_BELOW_FOR_HILLS * BLOCK_SIZE_PX / STEP_HEIGHT_PX} (16 levels).
   */
  static final int ROWS_BELOW_FOR_HILLS = 8;

  private final Terrain terrain;
  private final SunShadows shadows;
  private final TileFactory factory;
  private final int halfWidthBlocks;
  private final int halfHeightBlocks;

  private Map<BlockPos, List<Renderable>> tiles = new LinkedHashMap<>();
  private BlockPos centre;
  private int pieceCount;

  /**
   * Creates a scene for a screen of the given size.
   *
   * @param terrain the terrain to show
   * @param viewWidthPx the screen width, in pixels
   * @param viewHeightPx the screen height, in pixels
   * @param factory creates the drawable pieces for each visible tile
   */
  public TerrainScene(Terrain terrain, int viewWidthPx, int viewHeightPx, TileFactory factory) {
    this.terrain = terrain;
    this.shadows = new SunShadows(terrain);
    this.factory = factory;
    this.halfWidthBlocks = blocksToCover(viewWidthPx / 2.0) + MARGIN_BLOCKS;
    this.halfHeightBlocks = blocksToCover(viewHeightPx / 2.0) + MARGIN_BLOCKS;
  }

  /**
   * Creates a scene that shows the image for each block type, terrace faces, shadows, rims and
   * ambient occlusion.
   *
   * @param terrain the terrain to show
   * @param viewWidthPx the screen width, in pixels
   * @param viewHeightPx the screen height, in pixels
   * @return a scene drawing {@link TileRenderable} tops, {@link FaceRenderable} faces and {@link
   *     ShadowRenderable} shadows, {@link RimRenderable} rims and {@link OcclusionRenderable}
   *     occlusion, with each face step darkened by a {@link FaceShadeRenderable}
   */
  public static TerrainScene withPlaceholderTiles(
      Terrain terrain, int viewWidthPx, int viewHeightPx) {
    TileFactory placeholders =
        new TileFactory() {
          @Override
          public Renderable top(int blockX, int blockY, BlockType type, int height) {
            return new TileRenderable(blockX, blockY, type, height);
          }

          @Override
          public Renderable face(
              int blockX, int blockY, BlockType tileType, int level, boolean topStep) {
            return new FaceRenderable(blockX, blockY, tileType, level, topStep);
          }

          @Override
          public Renderable shadow(int blockX, int blockY, int height, int widthPx) {
            return new ShadowRenderable(blockX, blockY, height, widthPx);
          }

          @Override
          public Renderable rim(
              int blockX, int blockY, BlockType type, int height, Edge side) {
            return new RimRenderable(blockX, blockY, type, height, side);
          }

          @Override
          public Renderable occlusion(int blockX, int blockY, int height, Edge edge) {
            return new OcclusionRenderable(blockX, blockY, height, edge);
          }

          @Override
          public Renderable faceShade(int blockX, int blockY, int level) {
            return new FaceShadeRenderable(blockX, blockY, level);
          }
        };
    return new TerrainScene(terrain, viewWidthPx, viewHeightPx, placeholders);
  }

  /**
   * Moves the scene to be centred on the player. Does nothing if the player is still in the
   * same block as last time.
   *
   * @param playerX the player's x position, in pixels
   * @param playerY the player's y position, in pixels
   */
  public void update(double playerX, double playerY) {
    BlockPos newCentre =
        new BlockPos(
            TerrainCoordinates.pixelToBlock(playerX), TerrainCoordinates.pixelToBlock(playerY));
    if (newCentre.equals(centre)) {
      return;
    }
    Map<BlockPos, List<Renderable>> oldTiles = tiles;
    Map<BlockPos, List<Renderable>> newTiles = new LinkedHashMap<>();
    int count = 0;
    // Nearest row (lowest y) first, so it is drawn on top of the rows behind it.
    for (int blockY = newCentre.blockY() - halfHeightBlocks - ROWS_BELOW_FOR_HILLS;
        blockY <= newCentre.blockY() + halfHeightBlocks;
        blockY++) {
      for (int blockX = newCentre.blockX() - halfWidthBlocks;
          blockX <= newCentre.blockX() + halfWidthBlocks;
          blockX++) {
        BlockPos position = new BlockPos(blockX, blockY);
        List<Renderable> pieces = oldTiles.get(position);
        if (pieces == null) {
          pieces = buildPieces(blockX, blockY);
        }
        newTiles.put(position, pieces);
        count += pieces.size();
      }
    }
    tiles = newTiles;
    centre = newCentre;
    pieceCount = count;
  }

  /**
   * Returns how many pieces (tops, faces, shadows, rims and occlusion) the scene currently holds.
   *
   * @return the number of renderables, or 0 before the first {@link #update}
   */
  public int size() {
    return pieceCount;
  }

  /**
   * Returns how many tiles the scene currently covers.
   *
   * @return the number of tile positions, or 0 before the first {@link #update}
   */
  public int tileCount() {
    return tiles.size();
  }

  @Override
  public Iterator<Renderable> iterator() {
    List<Renderable> all = new ArrayList<>(pieceCount);
    for (List<Renderable> pieces : tiles.values()) {
      all.addAll(pieces);
    }
    return Collections.unmodifiableList(all).iterator();
  }

  /**
   * Returns a tile's rims (one per back, front, left or right edge where the neighbour is
   * lower), its shadow (if taller terrain to the right shades it), its occlusion (one per back,
   * left or right edge where the neighbour is higher), its top, then the face steps visible below
   * it: one per level that the tile stands above the tile in front of it, from the top step down,
   * each preceded by the overlay that darkens it.
   */
  private List<Renderable> buildPieces(int blockX, int blockY) {
    int height = terrain.heightAt(blockX, blockY);
    BlockType type = terrain.blockAt(blockX, blockY);
    List<Renderable> pieces = new ArrayList<>();
    addRimIfLower(pieces, blockX, blockY, type, height, Edge.BACK, 0, 1);
    addRimIfLower(pieces, blockX, blockY, type, height, Edge.FRONT, 0, -1);
    addRimIfLower(pieces, blockX, blockY, type, height, Edge.LEFT, -1, 0);
    addRimIfLower(pieces, blockX, blockY, type, height, Edge.RIGHT, 1, 0);
    int shadowWidth = shadows.shadowWidth(blockX, blockY);
    if (shadowWidth > 0) {
      pieces.add(factory.shadow(blockX, blockY, height, shadowWidth));
    }
    addOcclusionIfHigher(pieces, blockX, blockY, height, Edge.BACK, 0, 1);
    addOcclusionIfHigher(pieces, blockX, blockY, height, Edge.LEFT, -1, 0);
    addOcclusionIfHigher(pieces, blockX, blockY, height, Edge.RIGHT, 1, 0);
    pieces.add(factory.top(blockX, blockY, type, height));
    int frontHeight = terrain.heightAt(blockX, blockY - 1);
    for (int level = height - 1; level >= frontHeight; level--) {
      pieces.add(factory.faceShade(blockX, blockY, level));
      pieces.add(factory.face(blockX, blockY, type, level, level == height - 1));
    }
    return Collections.unmodifiableList(pieces);
  }

  /** Adds a rim on one side if the neighbouring tile in that direction is lower. */
  private void addRimIfLower(
      List<Renderable> pieces,
      int blockX,
      int blockY,
      BlockType type,
      int height,
      Edge side,
      int stepX,
      int stepY) {
    if (terrain.heightAt(blockX + stepX, blockY + stepY) < height) {
      pieces.add(factory.rim(blockX, blockY, type, height, side));
    }
  }

  /** Adds an occlusion gradient on one edge if the neighbouring tile that way is higher. */
  private void addOcclusionIfHigher(
      List<Renderable> pieces,
      int blockX,
      int blockY,
      int height,
      Edge edge,
      int stepX,
      int stepY) {
    if (terrain.heightAt(blockX + stepX, blockY + stepY) > height) {
      pieces.add(factory.occlusion(blockX, blockY, height, edge));
    }
  }

  /** Returns how many whole blocks it takes to cover a distance in pixels. */
  private static int blocksToCover(double pixels) {
    return (int) Math.ceil(pixels / TerrainCoordinates.BLOCK_SIZE_PX);
  }
}
