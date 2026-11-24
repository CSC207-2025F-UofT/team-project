package use_case.get_pet_picture;

import java.io.IOException;

public interface PetImageDataAccessInterface {

    /**
     * Returns the URL of a specific picture for the given species.
     *
     * @param species "DOG" or "CAT"
     * @return the selected picture's URL
     * @throws IOException if the picture request fails
     */
    String fetchImageUrl(String species) throws IOException;
}