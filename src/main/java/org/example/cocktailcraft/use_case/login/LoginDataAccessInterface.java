package org.example.cocktailcraft.use_case.login;

public interface LoginDataAccessInterface {

    boolean existsByName(String username);

    boolean validateCredentials(String username, String password);

}
