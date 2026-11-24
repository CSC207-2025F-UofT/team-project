package interface_adapter.import_photos;

import java.io.File;
import java.util.Arrays;

import use_case.import_photos.ImportPhotosInputBoundary;
import use_case.import_photos.ImportPhotosInputData;

public class ImportPhotosController {
    private final ImportPhotosInputBoundary interactor;

    public ImportPhotosController(ImportPhotosInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String exLocation, int exDistance, File[] paths) {
        ImportPhotosInputData inputData = new ImportPhotosInputData(
            exLocation, exDistance, 
            Arrays.stream(paths).map(File::getAbsolutePath).toList()
        );

        Thread worker = new Thread(
            () -> interactor.execute(inputData),
            "photo-import-worker"
        );
        worker.setDaemon(true);
        worker.start();
    }
}
