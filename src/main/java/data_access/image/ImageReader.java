package data_access.image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import entity.Photo;
import use_case.ImageReadException;
import use_case.ImageReaderInterface;

public class ImageReader implements ImageReaderInterface {
    private final ImageResolver resolver;

    public ImageReader(ImageResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public InputStream readFullImage(Photo photo) {
        try {
            Path imagePath = resolver.getImagePath(photo);

            return Files.newInputStream(imagePath);

        } catch (IOException e) {
            throw new ImageReadException(photo.getId(), e.getMessage(), e);
        }
    }
}
