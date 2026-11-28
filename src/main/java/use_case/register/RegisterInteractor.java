package use_case.register;

public class RegisterInteractor implements RegisterInputBoundary{

    private final RegisterOutputBoundary presenter;

    public RegisterInteractor(RegisterOutputBoundary presenter) {
        this.presenter = presenter;
    }

    public void execute(RegisterInputData registerInputData) {
        presenter.prepareFailView("OK");
    }

    private boolean userExists(String username){
        //need to implement with DAO
        return false;
    }

    private boolean emailExists(String email){
        //need to implement with DAO
        return false;
    }

    private boolean passwordValid(String password){
        return password.length() >= 6;
    }
    private boolean passwordMatches(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }

    private boolean emailValid(String email){
        String[] parts = email.split("@", -1);
        String localPart = parts[0];
        String[] domainPart = parts[1].split("\\.", -1);

        if(localPart.isEmpty())
            return false;
        for(String domain : domainPart){
            if(domain.isEmpty())
                return false;
        }

        return true;
    }

    public void switchToLoginView(){}
}
