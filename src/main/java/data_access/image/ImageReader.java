package data_access.image;

import java.io.IOException;
import java.nio.file.Files;

import entity.Photo;
import use_case.ImageReadException;
import use_case.ImageReaderInterface;

public class ImageReader implements ImageReaderInterface {
    private final ImageResolver resolver;

    public ImageReader(ImageResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public byte[] readFullImage(Photo photo) {
        try {
            return Files.readAllBytes(resolver.getImagePath(photo));
        } catch (IOException e) {
            throw new ImageReadException(photo.getId(), e.getMessage(), e);
        }
    }
}
