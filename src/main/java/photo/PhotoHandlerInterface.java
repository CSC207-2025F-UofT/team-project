package photo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;

import entity.Photo;

public interface PhotoHandlerInterface {
    void importImages(Path sourceDirectory, Consumer<Photo> onPhotoProcessed);
    BufferedImage loadFullImage(String photoId);
}
