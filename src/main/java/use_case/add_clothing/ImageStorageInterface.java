package use_case.add_clothing;

import java.io.IOException;

/**
 * Saves a clothing picture the user picked and says where it was stored.
 */
public interface ImageStorageInterface {
    String saveImage(String sourcePath, String username, String itemId) throws IOException;
}
