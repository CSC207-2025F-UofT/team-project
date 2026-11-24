package com.terrain.data_access;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.Tile;
import com.terrain.entity.model.TileType;
import com.terrain.use_case.save_data.DataAccessInterface;

public class TerrainBinaryStorage implements DataAccessInterface {
    private static final byte[] MAGIC = {'T', 'M', 'A', 'P'};
    private static final int VERSION = 1;

    @Override
    public void save(TerrainMap map, File file) throws IOException {
        try (DataOutputStream out = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(file)))) {

            // 1. Header
            out.write(MAGIC);
            out.writeInt(VERSION);

            // 2. Map metadata
            int width = map.getWidth();
            int height = map.getHeight();
            out.writeInt(width);
            out.writeInt(height);

            // 3. Tiles in row-major order
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Tile tile = map.getTile(x, y);
                    out.writeInt(tile.getType().ordinal()); // or a custom ID
                    out.writeDouble(tile.getElevation());
                }
            }
        }
    }

    @Override
    public TerrainMap load(File file) throws IOException {
        try (DataInputStream in = new DataInputStream(
                new BufferedInputStream(new FileInputStream(file)))) {

            // 1. Verify header
            byte[] magic = new byte[4];
            in.readFully(magic);
            if (magic[0] != 'T' || magic[1] != 'M' || magic[2] != 'A' || magic[3] != 'P') {
                throw new IOException("Invalid terrain map file header");
            }

            int version = in.readInt();
            if (version != VERSION) {
                throw new IOException("Unsupported terrain map version: " + version);
            }

            // 2. Map metadata
            int width = in.readInt();
            int height = in.readInt();

            TerrainMap map = new TerrainMap(width, height);
            // 3. Reconstruct tiles
            TileType[] types = TileType.values();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int typeOrdinal = in.readInt();
                    if (typeOrdinal < 0 || typeOrdinal >= types.length) {
                        throw new IOException("Unknown tile type ordinal: " + typeOrdinal);
                    }
                    double elevation = in.readDouble();
                    map.getTile(x,y).setElevation(elevation);
                    map.getTile(x,y).setType(types[typeOrdinal]);
                }
            }
            return map;
        }

    }
}