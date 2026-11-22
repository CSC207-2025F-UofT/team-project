package org.example.cocktailcraft.data_access;

import org.example.cocktailcraft.use_case.signup.SignupDataAccessInterface;

public class InMemoryUserDataAccessObject implements SignupDataAccessInterface {

    @Override
    public boolean existsByName(String username) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void createUser(String username, String password) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
