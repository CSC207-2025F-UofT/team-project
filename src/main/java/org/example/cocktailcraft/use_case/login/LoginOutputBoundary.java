package org.example.cocktailcraft.use_case.login;

public interface LoginOutputBoundary {
    void prepareSuccessView(LoginOutputData outputData);

    void prepareFailView(String message);
}
