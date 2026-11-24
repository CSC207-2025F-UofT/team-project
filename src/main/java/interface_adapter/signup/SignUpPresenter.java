package interface_adapter.signup;
import use_case.signup.SignUpOutputBoundary;
import use_case.signup.SignUpOutputData;
public final class SignUpPresenter implements SignUpOutputBoundary {
    private final SignUpViewModel viewModel;
    public SignUpPresenter(SignUpViewModel viewModel){this.viewModel=viewModel;}
    @Override public void prepareSuccessView(SignUpOutputData data){viewModel.setMessage(data.getMessage(),true);}
    @Override public void prepareFailView(String message){viewModel.setMessage(message,false);}
}
