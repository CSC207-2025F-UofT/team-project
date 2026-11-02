package terrainscene;

import game.gameModel.Renderable;
import jswing.Texture;
import terrain.TerrainCoordinates;

/**
 * A shadow lying on the right-hand side of a tile's top, cast by taller terrain to its right.
 *
 * <p>The image is a dark, semi-transparent strip, so the tile underneath still shows through.
 */
public final class ShadowRenderable implements Renderable {

  private static final long serialVersionUID = 1L;

  /** Image folder, under the textures root, holding one strip image per shadow width. */
  static final String TEXTURE_FOLDER = TileRenderable.TEXTURE_FOLDER + "shadow/";

  private final int blockX;
  private final int blockY;
  private final int height;
  private final int widthPx;

  /** Loaded from image files, so never saved; shadows are rebuilt from the terrain instead. */
  private final transient Texture texture;

  /**
   * Creates a shadow and loads its strip image.
   *
   * @param blockX the x coordinate of the shaded tile, in blocks
   * @param blockY the y coordinate of the shaded tile, in blocks
   * @param height the shaded tile's terrace height, so the shadow sits on its top
   * @param widthPx how much of the tile is shaded, from its right edge; a multiple of {@value
   *     SunShadows#WIDTH_GRANULARITY_PX} from 8 to 64
   */
  public ShadowRenderable(int blockX, int blockY, int height, int widthPx) {
    this.blockX = blockX;
    this.blockY = blockY;
    this.height = height;
    this.widthPx = widthPx;
    this.texture = new Texture(TEXTURE_FOLDER + String.format("%02d", widthPx));
  }

  @Override
  public Texture getTexture() {
    return texture;
  }

  /** Returns the strip's centre: it hugs the tile's right edge. */
  @Override
  public int getX() {
    int tileRight = TerrainCoordinates.blockToPixel(blockX) + TerrainCoordinates.BLOCK_SIZE_PX;
    return tileRight - widthPx / 2;
  }

  /** Returns the bottom edge of the shaded tile's top, so the shadow lies on it. */
  @Override
  public int getY() {
    return TerrainCoordinates.blockToPixel(blockY) + height * TileRenderable.STEP_HEIGHT_PX;
  }

  @Override
  public int getWidth() {
    return widthPx;
  }

  @Override
  public int getHeight() {
    return TerrainCoordinates.BLOCK_SIZE_PX;
  }
}
