package view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.junit.jupiter.api.Test;

class AccountBarTest {
    private final List<String> clicks = new ArrayList<>();
    private final AccountBar bar = new AccountBar(() -> clicks.add("login"), () -> clicks.add("profile"),
            () -> clicks.add("applications"), () -> clicks.add("signout"));

    private JButton button(String text) {
        for (java.awt.Component component : bar.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals(text)) {
                return (JButton) component;
            }
        }
        throw new AssertionError("No button " + text);
    }

    private String greeting() {
        for (java.awt.Component component : bar.getComponents()) {
            if (component instanceof JLabel) {
                return ((JLabel) component).getText();
            }
        }
        throw new AssertionError("No greeting");
    }

    @Test
    void aVisitorOnlySeesLogIn() {
        assertTrue(button("Log in").isVisible());
        assertFalse(button("My profile").isVisible());
        assertFalse(button("My applications").isVisible());
        assertFalse(button("Sign out").isVisible());
        assertEquals("Browsing as a guest", greeting());
    }

    @Test
    void aSignedInAdopterSeesTheirNameAndPersonalPages() {
        bar.showSignedIn("Audrey");

        assertFalse(button("Log in").isVisible());
        assertTrue(button("My profile").isVisible());
        assertTrue(button("My applications").isVisible());
        assertTrue(button("Sign out").isVisible());
        assertEquals("Signed in as Audrey", greeting());
    }

    @Test
    void showingGuestAgainHidesThePersonalPages() {
        bar.showSignedIn("Audrey");

        bar.showGuest();

        assertTrue(button("Log in").isVisible());
        assertFalse(button("My profile").isVisible());
    }

    @Test
    void buttonsRunTheirActions() {
        bar.showSignedIn("Audrey");

        button("Log in").doClick();
        button("My profile").doClick();
        button("My applications").doClick();
        button("Sign out").doClick();

        assertEquals(List.of("login", "profile", "applications", "signout"), clicks);
    }
}
