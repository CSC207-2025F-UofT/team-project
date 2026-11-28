package interface_adapter.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class CheckCompatibilityControllerTest {

    @Test
    void checkPassesTheCatAndTheAdopterToTheUseCase() {
        final List<String> seen = new ArrayList<>();

        new CheckCompatibilityController(input -> seen.add(input.getPetId() + "/" + input.getAdopterId()))
                .check("pet1", "adopter1");

        assertEquals(List.of("pet1/adopter1"), seen);
    }
}
