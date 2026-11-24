package interface_adapter.Loggedin;

import entities.CognitiveData;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoggedinStateTest {

    @Test
    void defaults() {
        LoggedinState state = new LoggedinState();

        assertEquals("", state.getUsername());
        assertEquals("", state.getRole());
        assertNotNull(state.getEntries());
        assertTrue(state.getEntries().isEmpty());
        assertFalse(state.isDataLoaded());
        assertEquals("", state.getDataError());
    }

    @Test
    void settersUpdateGetters() {
        LoggedinState state = new LoggedinState();
        CognitiveData d = new CognitiveData.CogBuilder(new Date(0L)).build();
        List<CognitiveData> entries = List.of(d);

        state.setUsername("jonny");
        state.setRole("Regular User");
        state.setEntries(entries);
        state.setDataLoaded(true);
        state.setDataError("oops");

        assertEquals("jonny", state.getUsername());
        assertEquals("Regular User", state.getRole());
        assertEquals(entries, state.getEntries());
        assertTrue(state.isDataLoaded());
        assertEquals("oops", state.getDataError());
    }
}
