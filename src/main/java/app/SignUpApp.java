package app;
import entity.UserFactory;
import frameworks_and_drivers.persistence.JsonUserDataAccess;
import frameworks_and_drivers.PasswordUtils;
import interface_adapter.signup.SignUpController;
import interface_adapter.signup.SignUpPresenter;
import interface_adapter.signup.SignUpViewModel;
import use_case.signup.SignUpInteractor;
import view.SignUpView;
import javax.swing.SwingUtilities;

public class SignUpApp {
    public static void open() {

        SignUpViewModel model = new SignUpViewModel();

        SignUpPresenter presenter = new SignUpPresenter(model);

        SignUpInteractor interactor = new SignUpInteractor(new JsonUserDataAccess(),
                presenter, new PasswordUtils(), new UserFactory());

        SignUpController controller = new SignUpController(interactor);

        new SignUpView(controller, model).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SignUpApp::open);
    }
}
