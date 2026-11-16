package org.example.cocktailcraft.use_case.signup;

public interface SignupOutputBoundary {
    void prepareSuccessView(SignupOutputData outputData);

    void prepareFailView(String message);
}
