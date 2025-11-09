package com.studyarc.use_case.reflection_log;

public class ReflectionLogInteractor implements ReflectionLogInputBoundary {
    private final ReflectionLogOutputBoundary reflectionLogPresenter;
    private final ReflectionLogDataAccessInterface reflectionLogDataAccess;

    public ReflectionLogInteractor(ReflectionLogOutputBoundary reflectionLogPresenter,
                                   ReflectionLogDataAccessInterface reflectionLogDataAccess) {
        this.reflectionLogPresenter = reflectionLogPresenter;
        this.reflectionLogDataAccess = reflectionLogDataAccess;
    }

    @Override
    public void execute(ReflectionLogInputData inputData) {
    }

}
