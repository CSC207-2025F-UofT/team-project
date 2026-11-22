package org.example.cocktailcraft.use_case.signup;

public interface SignupDataAccessInterface {
    boolean existsByName(String username);

    void createUser(String username, String password);
}
