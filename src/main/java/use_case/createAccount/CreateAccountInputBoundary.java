package use_case.createAccount;

public interface CreateAccountInputBoundary {

    void execute(CreateAccountInputData inputData);

    void switchToHomePage();
}
