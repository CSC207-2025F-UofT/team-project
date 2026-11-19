package use_case;

import java.util.List;
import java.util.function.Consumer;

import entity.Photo;
import use_case.import_photos.ImageImportProgressData;

public interface ImageImporterInterface {
    List<Photo> importImages(List<String> paths, Consumer<ImageImportProgressData> onImageProcessed);
    void cleanupImage(Photo photo);
}
