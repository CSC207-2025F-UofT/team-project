package use_case;

import java.util.List;

import entity.Photo;

public interface PhotoAccessInterface {
    /**
     * Retrieves a photo by its identifier.
     *
     * @param id the photo identifier
     * @return the matching {@link Photo} or {@code null} if no photo with the given ID exists
     * @throws PhotoAccessException if accessing photo storage fails
     */
    Photo getPhotoById(String id);

    /**
     * Retrieves a list of all photos.
     *
     * @return the matching list of {@link Photo} entities
     * @throws PhotoAccessException if accessing photo storage fails
     */
    List<Photo> getPhotos();

    /**
     * Retrieves a list of all photos associated with a trip.
     *
     * @param tripId the trip identifier
     * @return the matching list of {@link Photo} entities or an empty list if no trip with the given ID exists
     * @throws PhotoAccessException if accessing photo storage fails
     */
    List<Photo> getPhotosByTripId(String tripId);

    /**
     * Writes a photo.
     *
     * @param photo the {@link Photo} being written
     * @throws PhotoAccessException if accessing photo storage fails
     */
    void writePhoto(Photo photo);

    /**
     * Writes a list of photos.
     *
     * @param photos the list of {@link Photo} entities being written
     * @throws PhotoAccessException if accessing photo storage fails
     */
    void writePhotos(List<Photo> photos);
}
