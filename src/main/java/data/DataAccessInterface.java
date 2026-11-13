package data;

import java.util.List;

import entity.Photo;
import entity.Trip;

public interface DataAccessInterface {
    Photo getPhotoFromId(String id);
    void addPhoto(Photo photo);

    Trip getTripFromId(String id);
    List<Photo> getTripPhotos(String tripId);
}
