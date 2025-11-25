package use_case.seeRegionPokedex;

import java.io.IOException;

public interface RegionPokedexInputBoundary {
    void execute (RegionPokedexInputData regionPokedexInputData) throws IOException;
}
