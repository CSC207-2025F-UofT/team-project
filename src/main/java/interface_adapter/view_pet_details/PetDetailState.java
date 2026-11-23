package interface_adapter.view_pet_details;

import java.util.ArrayList;
import java.util.List;

/**
 * What the pet detail page shows, already written out as text.
 */
public class PetDetailState {
    private String petId = "";
    private String name = "";
    private String summary = "";
    private List<String> detailLines = new ArrayList<>();
    private String errorMessage = "";

    /**
     * Returns the id of the cat on show.
     *
     * @return the cat's id
     */
    public String getPetId() {
        return petId;
    }

    /**
     * Sets the id of the cat on show.
     *
     * @param petId the cat's id
     */
    public void setPetId(String petId) {
        this.petId = petId;
    }

    /**
     * Returns the cat's name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the cat's name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the one-line summary, for example "Cat, Tabby, 2 years old".
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the one-line summary.
     *
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Returns the profile lines, each like "Vaccinated: Yes".
     *
     * @return the lines in display order
     */
    public List<String> getDetailLines() {
        return detailLines;
    }

    /**
     * Sets the profile lines.
     *
     * @param detailLines the lines in display order
     */
    public void setDetailLines(List<String> detailLines) {
        this.detailLines = new ArrayList<>(detailLines);
    }

    /**
     * Returns the error to show, empty when there is none.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the error to show.
     *
     * @param errorMessage the message, empty for none
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
