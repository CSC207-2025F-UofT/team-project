package interface_adapter.login;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
public class LoginController {

    private  final LoginInputBoundary loginInteractor;

    public LoginController(LoginInputBoundary loginInteractor){
        this.loginInteractor = loginInteractor;
    }

    public void execute(String userName, String password){
        LoginInputData inputData = new LoginInputData(userName, password);
        loginInteractor.execute(inputData);
    }
}
