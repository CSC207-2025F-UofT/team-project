package data_access;

import entity.Landmark;

public interface LandmarkDataAccessInterface {
    /**
     * Checks if the landmark exists.
     * @param landmarkName the landmark name to look for
     * @return true if a landmark with the given name exists; false otherwise
     */
    boolean existsByName(String landmarkName);

//    /**
//     * Checks if the landmark exists.
//     * @param landmarkId the landmark id to look for
//     * @return true if a landmark with the given id exists; false otherwise
//     */
//    boolean existsById(String landmarkId);
//
//    /**
//     * Get landmark based on id.
//     * @param landmarkId the landmark id
//     * @return the Landmark object represented by the landmarkId
//     */
//    Landmark getLandmarkById(String landmarkId);

    /**
      * Get landmark based on name.
      * @param landmarkName the landmark name
      * @return the Landmark object represented by the landmarkName
      */
    Landmark getLandmarkByName(String landmarkName);
}
