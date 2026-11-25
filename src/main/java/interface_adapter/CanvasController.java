package interface_adapter;

import use_case.import_image.ImportImageInputBoundary;
import use_case.import_image.ImportImageInputData;

import java.io.File;

public class CanvasController {

    // Add more input boundaries (corresponding to other use cases) as needed
    private final ImportImageInputBoundary importImageUseCaseInteractor;

    // Add more parameters as needed
    public CanvasController(ImportImageInputBoundary importImageUseCaseInteractor) {
        this.importImageUseCaseInteractor = importImageUseCaseInteractor;
    }

    public void importImage(File selectedFile, String currentProjectId) {
        ImportImageInputData inputData = new ImportImageInputData(selectedFile, currentProjectId);
        importImageUseCaseInteractor.execute(inputData);
    }
}
