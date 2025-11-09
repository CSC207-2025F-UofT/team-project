package com.studyarc.use_case.reflection_log;

public interface ReflectionLogOutputBoundary {
    void prepareSuccessView(ReflectionLogOutputData outputData);

    void prepareFailView(String errorMessage);
}
