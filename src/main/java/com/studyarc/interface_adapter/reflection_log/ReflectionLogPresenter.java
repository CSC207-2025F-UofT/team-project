package com.studyarc.interface_adapter.reflection_log;

import com.studyarc.use_case.reflection_log.ReflectionLogOutputBoundary;
import com.studyarc.use_case.reflection_log.ReflectionLogOutputData;

public class ReflectionLogPresenter implements ReflectionLogOutputBoundary {
    private final ReflectionLogViewModel reflectionViewModel;

    public ReflectionLogPresenter(ReflectionLogViewModel reflectionViewModel) {
        this.reflectionViewModel = reflectionViewModel;
    }

    @Override
    public void prepareSuccessView(ReflectionLogOutputData outputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
