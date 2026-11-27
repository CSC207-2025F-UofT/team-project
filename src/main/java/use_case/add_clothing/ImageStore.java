package use_case.add_clothing;

/** Port for keeping a private copy of the user's chosen image. */
public interface ImageStore {
    /**
     * Copies the image at sourcePath into app storage and returns the stored path.
     * @throws IllegalArgumentException if the image cannot be read
     */
    String store(String sourcePath);
}
