package interface_adapter.load_photos;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoadPhotoControllerTest {
    @Test
    void wrapsIdInInputData() {
        List<String> received = new ArrayList<>();
        new LoadPhotoController(input -> received.add(input.getPhotoId())).execute("p1");

        assertEquals(List.of("p1"), received);
    }
}
