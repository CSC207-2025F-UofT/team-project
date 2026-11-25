package interface_adapter.login;

import use_case.login.LoginOutputBoudary;
import use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoudary {
    private LoginViewModel viewModel;

    public LoginPresenter(LoginViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData outputData){
        viewModel.setModel(outputData.getUserId(),outputData.getUserName(),"Login Successfully");
    }

    @Override
    public  void prepareFailureView(String message){
        viewModel.setModel(0, "", message);
    }
}
