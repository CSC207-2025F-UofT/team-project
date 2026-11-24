package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.home.HomeViewModel;
import interface_adapter.login.LogInController;
import interface_adapter.login.LogInPresenter;
import interface_adapter.login.LogInViewModel;
import interface_adapter.logout.LogOutController;
import interface_adapter.logout.LogOutPresenter;
import interface_adapter.register.RegisterController;
import interface_adapter.register.RegisterPresenter;
import interface_adapter.register.RegisterViewModel;
import org.junit.jupiter.api.Test;
import use_case.account.AccountProfile;
import use_case.login.LogInResponse;
import use_case.register.RegisterResponse;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Drives the real views, view models and presenters, with the use cases replaced by scripted responses. */
class AccountViewsTest {
    private static final AccountProfile SAM = new AccountProfile(UUID.randomUUID(), "sam_1", "Sam");

    private final ViewManagerModel viewManagerModel = new ViewManagerModel(LogInViewModel.VIEW_NAME);
    private final LogInViewModel logInViewModel = new LogInViewModel();
    private final RegisterViewModel registerViewModel = new RegisterViewModel();
    private final HomeViewModel homeViewModel = new HomeViewModel();
    private final LogInPresenter logInPresenter = new LogInPresenter(viewManagerModel, logInViewModel, homeViewModel);
    private final RegisterPresenter registerPresenter =
            new RegisterPresenter(viewManagerModel, registerViewModel, homeViewModel);
    private final LogOutPresenter logOutPresenter =
            new LogOutPresenter(viewManagerModel, logInViewModel, homeViewModel);

    private LogInView logIn;
    private RegisterView register;
    private HomeView home;

    @Test
    void rejectedRegistrationKeepsTheDetailsButNotThePasswords() throws Exception {
        build(request -> logInPresenter.present(LogInResponse.Failure.RETRYABLE_FAILURE),
                request -> registerPresenter.present(RegisterResponse.Failure.USERNAME_TAKEN));
        onEdt(() -> {
            button(logIn, "Create an account").doClick(0);
            assertTrue(register.isVisible());
            field(register, "Email").setText("sam@example.test");
            field(register, "Username").setText("sam_1");
            field(register, "Display name").setText("Sam");
            field(register, "Password").setText("long enough");
            field(register, "Confirm password").setText("long enough");
            button(register, "Register").doClick(0);
        });

        await(() -> button(register, "Register").isEnabled());

        onEdt(() -> {
            assertEquals("Error: That username is taken.", label(register, "Username error").getText());
            assertEquals("sam@example.test", field(register, "Email").getText());
            assertEquals("sam_1", field(register, "Username").getText());
            assertEquals("", field(register, "Password").getText());
            assertEquals("", field(register, "Confirm password").getText());
        });
    }

    @Test
    void logInOpensHomeAndLogOutReturnsToAClearedLogInForm() throws Exception {
        build(request -> logInPresenter.present(new LogInResponse.Success(SAM)), request -> { });
        onEdt(() -> {
            field(logIn, "Email").setText("sam@example.test");
            field(logIn, "Password").setText("long enough");
            button(logIn, "Log In").doClick(0);
        });

        await(home::isVisible);
        await(() -> field(logIn, "Email").getText().isEmpty());

        onEdt(() -> {
            assertEquals("Signed in as Sam", label(home, "Signed in as").getText());
            assertEquals("@sam_1", label(home, "Handle").getText());
            button(home, "Log Out").doClick(0);
        });

        await(logIn::isVisible);
        await(() -> status(logIn).equals("You’re signed out."));
        onEdt(() -> assertEquals("", label(home, "Signed in as").getText()));
    }

    @Test
    void unexpectedFailureReEnablesTheFormWithAnError() throws Exception {
        build(request -> {
            throw new IllegalStateException("fixture failure");
        }, request -> { });
        onEdt(() -> {
            field(logIn, "Email").setText("sam@example.test");
            field(logIn, "Password").setText("long enough");
            button(logIn, "Log In").doClick(0);
        });

        await(() -> button(logIn, "Log In").isEnabled());

        onEdt(() -> {
            assertTrue(logIn.isVisible());
            assertTrue(status(logIn).startsWith("Error: "));
        });
    }

    private void build(use_case.login.LogInInputBoundary logInUseCase,
                       use_case.register.RegisterInputBoundary registerUseCase) throws Exception {
        onEdt(() -> {
            logIn = new LogInView(logInViewModel, new LogInController(logInUseCase), viewManagerModel);
            register = new RegisterView(registerViewModel, new RegisterController(registerUseCase), viewManagerModel);
            home = new HomeView(homeViewModel, new LogOutController(logOutPresenter::signedOut), viewManagerModel);
            final ViewManager views = new ViewManager(viewManagerModel);
            views.addView(LogInViewModel.VIEW_NAME, logIn);
            views.addView(RegisterViewModel.VIEW_NAME, register);
            views.addView(HomeViewModel.VIEW_NAME, home);
        });
    }

    private static JTextField field(Container form, String labelText) {
        return (JTextField) find(form, JLabel.class, label -> labelText.equals(label.getText())).getLabelFor();
    }

    private static JLabel label(Container container, String name) {
        return find(container, JLabel.class, label -> name.equals(label.getName()));
    }

    private static JButton button(Container container, String text) {
        return find(container, JButton.class, button -> text.equals(button.getText()));
    }

    private static String status(Container form) {
        return find(form, JTextArea.class, area -> "Form status".equals(area.getName())).getText();
    }

    private static <T extends JComponent> T find(Container container, Class<T> type, Predicate<T> match) {
        for (Component child : container.getComponents()) {
            if (type.isInstance(child) && match.test(type.cast(child))) {
                return type.cast(child);
            }
            if (child instanceof Container nested) {
                final T found = findOrNull(nested, type, match);
                if (found != null) {
                    return found;
                }
            }
        }
        throw new AssertionError("No matching " + type.getSimpleName());
    }

    private static <T extends JComponent> T findOrNull(Container container, Class<T> type, Predicate<T> match) {
        try {
            return find(container, type, match);
        } catch (AssertionError ignored) {
            return null;
        }
    }

    private static void onEdt(Runnable action) throws Exception {
        final FutureTask<Void> task = new FutureTask<>(action, null);
        SwingUtilities.invokeAndWait(task);
        task.get();
    }

    private static void await(Callable<Boolean> condition) throws Exception {
        final long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (System.nanoTime() < deadline) {
            final FutureTask<Boolean> check = new FutureTask<>(condition);
            SwingUtilities.invokeAndWait(check);
            if (check.get()) {
                return;
            }
            Thread.sleep(20);
        }
        throw new AssertionError("Timed out waiting for the UI");
    }
}
