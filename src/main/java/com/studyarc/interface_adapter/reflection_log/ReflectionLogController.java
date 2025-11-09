package com.studyarc.interface_adapter.reflection_log;

import com.studyarc.use_case.reflection_log.ReflectionLogInputBoundary;
import com.studyarc.use_case.reflection_log.ReflectionLogInputData;
import java.time.LocalDate;

public class ReflectionLogController {
    private ReflectionLogInputBoundary reflectionLogInteractor;

    public ReflectionLogController(ReflectionLogInputBoundary reflectionLogUseCaseInteractor) {
        this.reflectionLogInteractor = reflectionLogUseCaseInteractor;
    }

    public void execute(String milestone, String contents, LocalDate date) {
        final ReflectionLogInputData inputData = new ReflectionLogInputData(
                milestone, contents, date);
        reflectionLogInteractor.execute(inputData);
    }

}
