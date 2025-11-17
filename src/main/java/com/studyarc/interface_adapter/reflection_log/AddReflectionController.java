package com.studyarc.interface_adapter.reflection_log;

import com.studyarc.use_case.add_reflection.AddReflectionInputBoundary;
import com.studyarc.use_case.add_reflection.AddReflectionInputData;
import java.time.LocalDate;

public class AddReflectionController {
    private AddReflectionInputBoundary reflectionLogInteractor;

    public AddReflectionController(AddReflectionInputBoundary reflectionLogUseCaseInteractor) {
        this.reflectionLogInteractor = reflectionLogUseCaseInteractor;
    }

    public void execute(String planName, String contents, LocalDate date) {
        final AddReflectionInputData inputData = new AddReflectionInputData(
                planName, contents, date);
        reflectionLogInteractor.execute(inputData);
    }

}
