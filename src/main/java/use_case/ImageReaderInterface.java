package use_case;

import entity.Photo;

public interface ImageReaderInterface {
    /** The encoded image file (JPEG/PNG/WebP bytes). Throws ImageReadException if unavailable. */
    byte[] readFullImage(Photo photo);
}
