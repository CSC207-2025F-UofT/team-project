package interface_adapters.lecturenotes;

import usecases.lecturenotes.GenerateLectureNotesOutputBoundary;
import usecases.lecturenotes.GenerateLectureNotesOutputData;

public class GenerateLectureNotesPresenter implements GenerateLectureNotesOutputBoundary {

    private final LectureNotesViewModel viewModel;

    public GenerateLectureNotesPresenter(LectureNotesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(GenerateLectureNotesOutputData outputData) {
        viewModel.setNotesText(outputData.getNotesText());
        viewModel.setErrorMessage("");
    }

    @Override
    public void prepareFailView(String errorMessage) {
        viewModel.setNotesText("");
        viewModel.setErrorMessage(errorMessage);
    }
}
