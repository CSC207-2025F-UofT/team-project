package app;

import frameworks_and_drivers.persistence.JsonUserDataAccess;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import use_case.login.LoginInteractor;
import view.LoginView;

public class LoginApp {
    public static LoginViewModel open() {

        LoginViewModel model = new LoginViewModel();

        LoginPresenter presenter = new LoginPresenter(model);

        LoginInteractor interactor = new LoginInteractor(new JsonUserDataAccess(),
                        presenter);

        LoginController controller = new LoginController(interactor);

        new LoginView(controller, model).setVisible(true);

        return model;
    }
}
