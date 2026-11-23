package use_case.log_in;

import use_case.log_in.LoginInputData;

public interface LoginInputBoundary {
    void execute(LoginInputData inputData);
}
