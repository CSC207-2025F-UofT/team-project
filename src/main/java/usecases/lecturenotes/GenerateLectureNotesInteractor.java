package usecases.lecturenotes;

import data_access.LectureNotesDataAccessException;
import data_access.LectureNotesDataAccessInterface;

import java.util.List;

public class GenerateLectureNotesInteractor implements GenerateLectureNotesInputBoundary {

    private final LectureNotesDataAccessInterface dataAccess;
    private final GenerateLectureNotesOutputBoundary presenter;

    public GenerateLectureNotesInteractor(LectureNotesDataAccessInterface dataAccess,
                                          GenerateLectureNotesOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateLectureNotesInputData inputData) {
        String courseId = inputData.getCourseId();
        List<String> filePaths = inputData.getFilePaths();
        String topic = inputData.getTopic();

        try {
            String notesText =
                    dataAccess.generateLectureNotes(courseId, filePaths, topic);

            GenerateLectureNotesOutputData outputData =
                    new GenerateLectureNotesOutputData(courseId, topic, notesText);

            presenter.prepareSuccessView(outputData);
        } catch (LectureNotesDataAccessException e) {
            // Alt flow: show generic error message to the user
            presenter.prepareFailView(
                    "Lecture notes generation failed. Please try again later.");
            // Log the real error for debugging
            e.printStackTrace();
        }
    }
}
