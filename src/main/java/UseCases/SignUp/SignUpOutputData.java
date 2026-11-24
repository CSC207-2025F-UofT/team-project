package UseCase.SignUp;

import BoundaryData.OutputData;

public class SignUpOutputData extends OutputData{

    private final String username;

    public SignUpOutputData(String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

}
