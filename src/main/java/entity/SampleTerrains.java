package entity;

import entity.TerrainFeature.Kind;

// Hardcoded terrains for development and tests, so nobody has to wait for the LLM.

public final class SampleTerrains {

    private SampleTerrains() {
    }

    /** Two hills, a valley between them and a ramp in one corner. */
    public static Terrain hills() {
        return new TerrainBuilder("Rolling hills")
                .addFeature(new TerrainFeature(Kind.HILL, 14, 16, 10, 14, null))
                .addFeature(new TerrainFeature(Kind.HILL, 34, 30, 12, 18, null))
                .addFeature(new TerrainFeature(Kind.VALLEY, 26, 22, 8, 6, null))
                .addFeature(new TerrainFeature(Kind.RAMP, 10, 38, 10, 8, "EAST"))
                .build();
    }

    /** Flat streets with a grid of blocks. */
    public static Terrain city() {
        TerrainBuilder builder = new TerrainBuilder("Simple city");
        for (int x = 8; x < 48; x += 12) {
            for (int y = 8; y < 48; y += 12) {
                builder.addFeature(new TerrainFeature(Kind.BLOCK, x, y, 6, 12, null));
            }
        }
        return builder.build();
    }
}
