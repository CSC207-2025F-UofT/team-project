package interface_adapter.submit_application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class SubmitApplicationStateTest {

    private static final String MESSAGE = "Submitted";

    @Test
    void choosingAnotherPetClearsOldMessages() {
        final SubmitApplicationState state = new SubmitApplicationState();
        state.setPetId("mochi");
        state.setSuccessMessage(MESSAGE);
        state.setError(MESSAGE);

        state.setPetId("mochi");
        assertEquals(MESSAGE, state.getSuccessMessage());

        state.setPetId("luna");
        assertEquals("", state.getSuccessMessage());
        assertEquals("", state.getError());
    }

    @Test
    void anotherAdopterDoesNotSeeOldMessages() {
        final SubmitApplicationState state = new SubmitApplicationState();
        state.setAdopterId("bob");
        state.setSuccessMessage(MESSAGE);
        state.setError(MESSAGE);

        state.setAdopterId("bob");
        assertEquals(MESSAGE, state.getSuccessMessage());

        state.setAdopterId("alice");
        assertEquals("", state.getSuccessMessage());
        assertEquals("", state.getError());
    }
}
