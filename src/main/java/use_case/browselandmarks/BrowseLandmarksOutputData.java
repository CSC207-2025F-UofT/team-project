// src/java/use_case/browselandmarks/BrowseLandmarksOutputData.java
package use_case.browselandmarks;

import java.util.List;

public class BrowseLandmarksOutputData {

    public static class LandmarkDTO {
        public final String name;
        public final double latitude;
        public final double longitude;

        public LandmarkDTO(String name, double latitude, double longitude) {
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private final List<LandmarkDTO> landmarks;

    public BrowseLandmarksOutputData(List<LandmarkDTO> landmarks) {
        this.landmarks = landmarks;
    }

    public List<LandmarkDTO> getLandmarks() {
        return landmarks;
    }
}
