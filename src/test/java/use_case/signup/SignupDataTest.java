package use_case.signup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SignupDataTest {

    @Test
    void inputDataStoresUsernameAndResearcherFlag() {
        SignupInputData data = new SignupInputData("dr_smith", true);

        assertEquals("dr_smith", data.getUsername());
        assertTrue(data.isResearcher());
    }

    @Test
    void inputDataResearcherFlagFalse() {
        assertFalse(new SignupInputData("jonny", false).isResearcher());
    }

    @Test
    void inputDataAllowsNullUsername() {
        assertNull(new SignupInputData(null, false).getUsername());
    }

    @Test
    void outputDataStoresUsernameAndRole() {
        SignupOutputData data = new SignupOutputData("dr_smith", "Researcher");

        assertEquals("dr_smith", data.getUsername());
        assertEquals("Researcher", data.getRole());
    }
}
