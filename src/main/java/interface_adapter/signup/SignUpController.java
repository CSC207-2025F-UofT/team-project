package interface_adapter.signup;
import use_case.signup.SignUpInputBoundary;
import use_case.signup.SignUpInputData;
public final class SignUpController {
    private final SignUpInputBoundary inputBoundary;
    public SignUpController(SignUpInputBoundary inputBoundary){this.inputBoundary=inputBoundary;}
    public void signUp(String name,String email,String password,String confirmation){
        inputBoundary.execute(new SignUpInputData(name,email,password,confirmation));
    }
}
