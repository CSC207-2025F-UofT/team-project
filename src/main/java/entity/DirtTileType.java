package entity;

/** Dirt terrain has ID 1 and permits walking. */
public final class DirtTileType implements TileType {
    @Override
    public int getId() {
        return 1;
    }

    @Override
    public boolean isWalkable() {
        return true;
    }
}
