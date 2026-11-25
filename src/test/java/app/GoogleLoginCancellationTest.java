package app;

import org.junit.jupiter.api.Test;
import interface_adapter.login.LoginViewModel;
import view.LoginView;
import use_case.google_login.GoogleIdentityGateway;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;

class GoogleLoginCancellationTest {
    private AppBuilder builder;
    private LoginView view;
    private static Object field(Object object, String name) throws Exception {
        Field field = object.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(object);
    }
    private static JButton button(Container parent, String text) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && text.equals(((JButton) child).getText())) {
                return (JButton) child;
            }
            if (child instanceof Container) {
                JButton found = button((Container) child, text);
                if (found != null) return found;
            }
        }
        return null;
    }
    private void setup(GoogleIdentityGateway gateway) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            builder = new AppBuilder().addLoginView().addSignupView().addLoggedInView();
            builder.addGoogleLoginUseCase(gateway);
        });
        view = (LoginView) field(builder, "loginView");
    }
    private void click(String label) throws Exception {
        SwingUtilities.invokeAndWait(() -> button(view, label).doClick());
    }
    private void awaitIdle() throws Exception {
        for (int i = 0; i < 100; i++) {
            boolean[] idle = {false};
            SwingUtilities.invokeAndWait(() -> idle[0] = button(view, "Continue with Google").isEnabled());
            if (idle[0]) return;
            Thread.sleep(20);
        }
        fail("Google sign-in did not finish");
    }
    @Test
    void cancelIgnoresLateResultAndAllowsRetry() throws Exception {
        CountDownLatch started = new CountDownLatch(1);
        CountDownLatch interrupted = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        AtomicInteger calls = new AtomicInteger();
        setup(() -> {
            if (calls.incrementAndGet() == 1) {
                started.countDown();
                try { release.await(); }
                catch (InterruptedException ex) {
                    interrupted.countDown();
                    release.await(); // Simulate a provider that still returns a late result.
                }
                return "stale-user";
            }
            return "new-user";
        });
        try {
            click("Continue with Google");
            assertTrue(started.await(2, TimeUnit.SECONDS));
            click("Cancel Google sign-in");
            assertTrue(interrupted.await(2, TimeUnit.SECONDS));
            SwingUtilities.invokeAndWait(() -> {
                assertNull(builder.userDataAccessObject.getCurrentUsername());
                assertTrue(button(view, "log in").isEnabled());
                assertTrue(button(view, "Sign Up").isEnabled());
                assertFalse(button(view, "Cancel Google sign-in").isVisible());
            });
            click("Continue with Google");
            awaitIdle();
            release.countDown();
            SwingUtilities.invokeAndWait(() -> assertEquals("google:new-user",
                    builder.userDataAccessObject.getCurrentUsername()));
        } finally { release.countDown(); }
    }
    @Test
    void timeoutClearsPendingAndShowsError() throws Exception {
        setup(() -> { throw new IOException("Google sign-in timed out. Try again."); });
        click("Continue with Google");
        awaitIdle();
        LoginViewModel model = (LoginViewModel) field(builder, "loginViewModel");
        SwingUtilities.invokeAndWait(() -> {
            assertEquals("Google sign-in timed out. Try again.", model.getState().getLoginError());
            assertTrue(button(view, "Sign Up").isEnabled());
            assertNull(builder.userDataAccessObject.getCurrentUsername());
        });
    }
}
