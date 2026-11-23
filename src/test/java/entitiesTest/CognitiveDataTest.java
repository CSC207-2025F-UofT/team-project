package entitiesTest;
import entities.CognitiveData;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CognitiveDataTest {
    CognitiveData data;

    @BeforeEach
    void setUp() {
        this.data = new CognitiveData.CogBuilder(new Date(0)).build();
    }

    @Test
    void correctBuildTestEmpty() {
        Date date = new Date(0);
        Hashtable<Date, Object[]> expected = new Hashtable<Date, Object[]> ();
        Object[] cogTest = {"blank", 0.0};
        Object[] put = {"none", cogTest, 0.0};
        expected.put(date, put);

        assertEquals(expected, data.getCognitiveResults());
    }

    /**
     * test the getters
     */
    @Test
    void builderSetsFields() {
        Date d = new Date();
        CognitiveData data = new CognitiveData.CogBuilder(d)
                .mood("happy")
                .cogTest("CogSci", 85.0)
                .productivityRating(7.0)
                .build();

        assertEquals(d, data.getDate());
        assertEquals("happy", data.getMood());
        assertEquals(85.0, data.getCogTestResult());
        assertEquals(7.0, data.getProductivityRating());
    }

    @Test
    void builderUsesDefaults() {
        CognitiveData data = new CognitiveData.CogBuilder(new Date()).build();
        assertEquals("none", data.getMood());
        assertEquals(0.0, data.getProductivityRating());
    }
}
