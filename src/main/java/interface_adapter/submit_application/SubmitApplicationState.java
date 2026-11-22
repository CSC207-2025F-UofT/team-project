package interface_adapter.submit_application;

import java.util.Objects;

/**
 * The state for the Submit Application View Model.
 */
public final class SubmitApplicationState {

    private String adopterId = "";
    private String petId = "";
    private String petName = "";
    private String error = "";
    private String successMessage = "";

    public String getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(String adopterId) {
        this.adopterId = adopterId;
    }

    public String getPetId() {
        return petId;
    }

    /**
     * Sets the pet being applied for. Messages about another pet are cleared.
     * @param petId the pet being applied for
     */
    public void setPetId(String petId) {
        if (!Objects.equals(this.petId, petId)) {
            clearMessages();
        }
        this.petId = petId;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    private void clearMessages() {
        error = "";
        successMessage = "";
    }
}
