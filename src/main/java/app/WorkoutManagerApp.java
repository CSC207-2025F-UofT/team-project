package app;

import data_access.InMemorySessionStore;
import data_access.SupabaseAuthGateway;
import data_access.SupabaseClient;
import data_access.SupabaseProfileDao;
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
import use_case.login.LogInInteractor;
import use_case.logout.LogOutInteractor;
import use_case.register.RegisterInteractor;
import view.HomeView;
import view.LogInView;
import view.RegisterView;
import view.ViewManager;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/** Builds the Clean Architecture engine: creates every object and connects each one to its collaborators. */
public final class WorkoutManagerApp {
    private WorkoutManagerApp() { }

    public static void main(String[] args) {
        final SupabaseClient client;
        try {
            client = SupabaseClient.fromEnvironment();
        } catch (SupabaseClient.ConfigurationException failure) {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, failure.getMessage(),
                    "Account setup is unavailable", JOptionPane.ERROR_MESSAGE));
            return;
        }
        SwingUtilities.invokeLater(() -> showApp(client));
    }

    private static void showApp(SupabaseClient client) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignored) {
            // Swing's default look and feel still works.
        }

        final InMemorySessionStore session = new InMemorySessionStore();
        final SupabaseAuthGateway auth = new SupabaseAuthGateway(client);
        final SupabaseProfileDao profiles = new SupabaseProfileDao(client);

        final ViewManagerModel viewManagerModel = new ViewManagerModel(LogInViewModel.VIEW_NAME);
        final LogInViewModel logInViewModel = new LogInViewModel();
        final RegisterViewModel registerViewModel = new RegisterViewModel();
        final HomeViewModel homeViewModel = new HomeViewModel();

        final LogInController logIn = new LogInController(new LogInInteractor(auth, profiles, session,
                new LogInPresenter(viewManagerModel, logInViewModel, homeViewModel)));
        final RegisterController register = new RegisterController(new RegisterInteractor(auth, profiles, profiles,
                session, new RegisterPresenter(viewManagerModel, registerViewModel, homeViewModel)));
        final LogOutController logOut = new LogOutController(new LogOutInteractor(session, auth,
                new LogOutPresenter(viewManagerModel, logInViewModel, homeViewModel)));

        final ViewManager views = new ViewManager(viewManagerModel);
        final LogInView logInView = new LogInView(logInViewModel, logIn, viewManagerModel);
        views.addView(LogInViewModel.VIEW_NAME, logInView);
        views.addView(RegisterViewModel.VIEW_NAME, new RegisterView(registerViewModel, register, viewManagerModel));
        views.addView(HomeViewModel.VIEW_NAME, new HomeView(homeViewModel, logOut));

        final JFrame frame = new JFrame("Workout Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(views);
        frame.pack();
        frame.setMinimumSize(frame.getSize());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        logInView.focusFirstField();
    }
}
