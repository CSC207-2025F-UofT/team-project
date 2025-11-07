package interface_adapters.lecturenotes;

import usecases.lecturenotes.GenerateLectureNotesInputBoundary;
import usecases.lecturenotes.GenerateLectureNotesInputData;

import java.util.List;

public class GenerateLectureNotesController {

    private final GenerateLectureNotesInputBoundary interactor;

    public GenerateLectureNotesController(GenerateLectureNotesInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void generate(String courseId,
                         List<String> filePaths,
                         String topic) {
        GenerateLectureNotesInputData inputData =
                new GenerateLectureNotesInputData(courseId, filePaths, topic);
        interactor.execute(inputData);
    }
}
