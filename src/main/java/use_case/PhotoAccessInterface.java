package use_case;

import java.util.List;

import entity.Photo;

public interface PhotoAccessInterface {
    Photo getPhotoById(String id);
    List<Photo> getPhotos();
    List<Photo> getPhotosByTripId(String tripId);

    void writePhoto(Photo photo);
    void writePhotos(List<Photo> photos);
}
