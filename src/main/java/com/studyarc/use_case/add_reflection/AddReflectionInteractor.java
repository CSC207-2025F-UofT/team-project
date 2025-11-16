package com.studyarc.use_case.add_reflection;

import java.time.LocalDate;

public class AddReflectionInteractor implements AddReflectionInputBoundary {
    private final AddReflectionOutputBoundary reflectionLogPresenter;
    private final AddReflectionDataAccessInterface reflectionLogDataAccess;

    public AddReflectionInteractor(AddReflectionOutputBoundary reflectionLogPresenter,
                                   AddReflectionDataAccessInterface reflectionLogDataAccess) {
        this.reflectionLogPresenter = reflectionLogPresenter;
        this.reflectionLogDataAccess = reflectionLogDataAccess;
    }

    @Override
    public void execute(AddReflectionInputData inputData) {
        final String contents = inputData.getContents();
        final LocalDate date = inputData.getDate() ;
        if (contents.isEmpty()) {
            reflectionLogPresenter.prepareFailView("Please enter a valid contents");
        }
        else {
            AddReflectionOutputData output = new AddReflectionOutputData();
            reflectionLogPresenter.prepareSuccessView(output);

        }
    }

}
