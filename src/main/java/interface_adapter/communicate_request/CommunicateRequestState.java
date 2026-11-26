package interface_adapter.communicate_request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * State displayed by the child communication screen.
 */
public class CommunicateRequestState {

    private List<PictogramItem> pictograms = new ArrayList<>();
    private String previewText = "";
    private boolean hasSelection;
    private String errorMessage;

    public List<PictogramItem> getPictograms() {
        return Collections.unmodifiableList(pictograms);
    }

    public void setPictograms(List<PictogramItem> pictograms) {
        this.pictograms = new ArrayList<>(pictograms);
    }

    public String getPreviewText() {
        return previewText;
    }

    public void setPreviewText(String previewText) {
        this.previewText = previewText;
    }

    public boolean hasSelection() {
        return hasSelection;
    }

    public void setHasSelection(boolean hasSelection) {
        this.hasSelection = hasSelection;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * One pictogram card displayed by the view.
     */
    public static final class PictogramItem {

        private final int id;
        private final String label;
        private final String imageLocation;

        public PictogramItem(
                int id,
                String label,
                String imageLocation) {

            this.id = id;
            this.label = label;
            this.imageLocation = imageLocation;
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
