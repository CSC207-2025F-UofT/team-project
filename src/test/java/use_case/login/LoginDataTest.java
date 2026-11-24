package use_case.login;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginDataTest {

    @Test
    void inputDataStoresUsernameAndResearcherFlag() {
        LoginInputData data = new LoginInputData("jonny", true);

        assertEquals("jonny", data.getUsername());
        assertTrue(data.isResearcher());
    }

    @Test
    void inputDataResearcherFlagFalse() {
        assertFalse(new LoginInputData("jonny", false).isResearcher());
    }

    @Test
    void inputDataAllowsNullUsername() {
        assertNull(new LoginInputData(null, false).getUsername());
    }

    @Test
    void outputDataStoresUsernameAndRole() {
        LoginOutputData data = new LoginOutputData("jonny", "Regular User");

        assertEquals("jonny", data.getUsername());
        assertEquals("Regular User", data.getRole());
    }
}
