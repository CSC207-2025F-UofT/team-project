package interface_adapter.Login;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginStateTest {

    @Test
    void defaultsAreEmptyStrings() {
        LoginState state = new LoginState();

        assertEquals("", state.getUsername());
        assertEquals("", state.getLoginError());
        assertEquals("", state.getWelcomeMessage());
    }

    @Test
    void settersUpdateGetters() {
        LoginState state = new LoginState();
        state.setUsername("jonny");
        state.setLoginError("bad");
        state.setWelcomeMessage("hi");

        assertEquals("jonny", state.getUsername());
        assertEquals("bad", state.getLoginError());
        assertEquals("hi", state.getWelcomeMessage());
    }
}
