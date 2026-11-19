package use_case;

import java.io.InputStream;

import entity.Photo;

public interface ImageReaderInterface {
    InputStream readFullImage(Photo photo);
}
