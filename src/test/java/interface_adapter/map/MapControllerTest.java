package interface_adapter.map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import use_case.load_map.LoadMapInputBoundary;
import use_case.load_map.LoadMapInputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/** Checks that the controller forwards requests and leaves validation to the use case. */
class MapControllerTest {
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"level1.txt", " \t", " map with spaces.txt "})
    void forwardsTheSuppliedNameExactlyOnce(String name) {
        List<LoadMapInputData> requests = new ArrayList<>();
        LoadMapInputBoundary inputBoundary = inputData -> requests.add(inputData);
        MapController controller = new MapController(inputBoundary);

        controller.load(name);

        assertEquals(1, requests.size());
        assertEquals(name, requests.get(0).getMapName());
    }
}
