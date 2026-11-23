package use_case.check_compatibility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RuleResultTest {

    @Test
    void gettersReturnWhatWasGiven() {
        final RuleResult result = new RuleResult("Medical needs", RuleResult.Status.CONFLICT, "Cannot take on.");

        assertEquals("Medical needs", result.getTrait());
        assertEquals(RuleResult.Status.CONFLICT, result.getStatus());
        assertEquals("Cannot take on.", result.getExplanation());
    }
}
