package entitiesTest;
import entities.CognitiveData;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

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

    @Test
    void cogTestTypeGetterReturnsBuilderValue() {
        CognitiveData data = new CognitiveData.CogBuilder(new Date(0)).cogTest("Memory", 42.5).build();

        assertEquals("Memory", data.getCogTestType());
        assertEquals(42.5, data.getCogTestResult());
    }

    @Test
    void cogTestDefaultsAreBlankAndZero() {
        assertEquals("blank", data.getCogTestType());
        assertEquals(0.0, data.getCogTestResult());
    }

    @Test
    void getCognitiveResultsReflectsAllBuilderFields() {
        Date d = new Date(1000L);
        CognitiveData full = new CognitiveData.CogBuilder(d)
                .mood("happy")
                .cogTest("Attention", 8.0)
                .productivityRating(7.0)
                .build();

        Hashtable<Date, Object[]> results = full.getCognitiveResults();

        assertEquals(1, results.size());
        Object[] row = results.get(d);
        assertEquals("happy", row[0]);
        assertArrayEquals(new Object[]{"Attention", 8.0}, (Object[]) row[1]);
        assertEquals(7.0, row[2]);
    }

    @Test
    void laterBuilderCallsOverrideEarlierOnes() {
        CognitiveData overridden = new CognitiveData.CogBuilder(new Date(0))
                .mood("sad").mood("happy")
                .productivityRating(1.0).productivityRating(9.0)
                .build();

        assertEquals("happy", overridden.getMood());
        assertEquals(9.0, overridden.getProductivityRating());
    }
}
