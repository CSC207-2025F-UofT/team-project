package com.studyarc.use_case.add_reflection;

public interface AddReflectionOutputBoundary {
    void prepareSuccessView(AddReflectionOutputData outputData);

    void prepareFailView(String errorMessage);
}
