package data_access;

import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the pure (non-network) logic of the Supabase cognitive data DAO.
 */
public class SupabaseCognitiveDataAccessObjectTest {

    @Test
    void attentionIsRecognised() {
        assertEquals("Attention", SupabaseCognitiveDataAccessObject.toTestType("Attention"));
    }

    @Test
    void memoryIsRecognised() {
        assertEquals("Memory", SupabaseCognitiveDataAccessObject.toTestType("Memory"));
    }

    @Test
    void matchingIsCaseInsensitive() {
        assertEquals("Attention", SupabaseCognitiveDataAccessObject.toTestType("ATTENTION"));
        assertEquals("Memory", SupabaseCognitiveDataAccessObject.toTestType("memory"));
    }

    @Test
    void surroundingWhitespaceIsIgnored() {
        assertEquals("Attention", SupabaseCognitiveDataAccessObject.toTestType("  attention  "));
        assertEquals("Memory", SupabaseCognitiveDataAccessObject.toTestType("\tMemory\n"));
    }

    @Test
    void unknownTestTypeThrows() {
        JSONException ex = assertThrows(JSONException.class,
                () -> SupabaseCognitiveDataAccessObject.toTestType("Reaction"));

        assertTrue(ex.getMessage().contains("Reaction"));
    }

    @Test
    void emptyTestTypeThrows() {
        assertThrows(JSONException.class, () -> SupabaseCognitiveDataAccessObject.toTestType(""));
    }

    @Test
    void testTypeConstantsHaveExpectedValues() {
        assertEquals("Attention", SupabaseCognitiveDataAccessObject.ATTENTION);
        assertEquals("Memory", SupabaseCognitiveDataAccessObject.MEMORY);
    }
}
