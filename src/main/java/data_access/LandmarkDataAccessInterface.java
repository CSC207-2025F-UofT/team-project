// src/java/data_access/LandmarkDataAccessInterface.java
package data_access;

import entity.Landmark;

import java.util.List;

public interface LandmarkDataAccessInterface {
    List<Landmark> getLandmarks();
    boolean existsByName(String landmarkName);
}
