// src/java/interface_adapter/browse_landmarks/BrowseLandmarksState.java
package interface_adapter.browselandmarks;

import java.util.ArrayList;
import java.util.List;

public class BrowseLandmarksState {

    public static class LandmarkVM {
        public String name;
        public double latitude;
        public double longitude;
    }

    private List<LandmarkVM> landmarks = new ArrayList<>();

    public List<LandmarkVM> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<LandmarkVM> landmarks) {
        this.landmarks = landmarks;
    }
}
