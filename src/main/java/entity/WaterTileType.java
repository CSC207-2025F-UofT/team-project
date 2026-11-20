package entity;

/** Water terrain has ID 0 and does not permit walking. */
public final class WaterTileType implements TileType {
    @Override
    public int getId() {
        return 0;
    }

    @Override
    public boolean isWalkable() {
        return false;
    }
}
