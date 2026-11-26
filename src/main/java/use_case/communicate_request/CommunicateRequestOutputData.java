package use_case.communicate_request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Plain output data for the child communication screen.
 */
public final class CommunicateRequestOutputData {

    private final List<PictogramItem> availablePictograms;
    private final String previewText;
    private final boolean hasSelection;

    public CommunicateRequestOutputData(
            List<PictogramItem> availablePictograms,
            String previewText,
            boolean hasSelection) {

        this.availablePictograms = Collections.unmodifiableList(
                new ArrayList<>(Objects.requireNonNull(
                        availablePictograms,
                        "availablePictograms cannot be null")));

        this.previewText =
                Objects.requireNonNull(previewText, "previewText cannot be null");

        this.hasSelection = hasSelection;
    }

    public List<PictogramItem> getAvailablePictograms() {
        return availablePictograms;
    }

    public String getPreviewText() {
        return previewText;
    }

    public boolean hasSelection() {
        return hasSelection;
    }

    /**
     * Plain pictogram data used by the interface adapter.
     */
    public static final class PictogramItem {

        private final int id;
        private final String label;
        private final String imageLocation;

        public PictogramItem(int id, String label, String imageLocation) {
            this.id = id;
            this.label = Objects.requireNonNull(label);
            this.imageLocation = Objects.requireNonNull(imageLocation);
        }

        public int getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getImageLocation() {
            return imageLocation;
        }
    }
}
