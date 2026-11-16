package org.example.cocktailcraft.use_case.signup;

/** Initial input shape. TODO(Jiaqi): extend with the agreed use case contract. */
public class SignupInputData {
    private final String username;
    private final String password;
    private final String confirmation;

    public SignupInputData(String username, String password, String confirmation) {
        this.username = username;
        this.password = password;
        this.confirmation = confirmation;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmation() {
        return confirmation;
    }
}
