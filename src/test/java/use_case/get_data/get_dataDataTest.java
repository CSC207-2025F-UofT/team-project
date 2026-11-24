package use_case.get_data;

import entities.CognitiveData;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class get_dataDataTest {

    @Test
    void inputDataStoresUsername() {
        assertEquals("jonny", new get_dataInputData("jonny").getUsername());
    }

    @Test
    void inputDataAllowsNullUsername() {
        assertNull(new get_dataInputData(null).getUsername());
    }

    @Test
    void outputDataReturnsTheSameEntries() {
        CognitiveData d1 = new CognitiveData.CogBuilder(new Date(0L)).mood("happy").build();
        CognitiveData d2 = new CognitiveData.CogBuilder(new Date(1L)).mood("tired").build();
        List<CognitiveData> entries = List.of(d1, d2);

        get_dataOutputData data = new get_dataOutputData(entries);

        assertEquals(entries, data.getEntries());
        assertSame(d1, data.getEntries().get(0));
    }

    @Test
    void outputDataWithEmptyList() {
        assertTrue(new get_dataOutputData(new ArrayList<>()).getEntries().isEmpty());
    }
}
