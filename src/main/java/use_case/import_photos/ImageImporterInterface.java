package use_case.import_photos;

import java.util.List;
import java.util.function.Consumer;

import entity.Photo;

public interface ImageImporterInterface {
    List<Photo> importImages(List<String> paths, Consumer<ImageImportProgressData> onImageProcessed);
    void cleanupImage(Photo photo);
}
