package build;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonDependencyTest {
    @Test
    void jsonRoundTrip() {
        Gson gson = new Gson();
        JsonObject data = new JsonObject();
        data.addProperty("name", "Alex");
        data.addProperty("energy", 85);

        assertEquals(data, gson.fromJson(gson.toJson(data), JsonObject.class));
    }
}
