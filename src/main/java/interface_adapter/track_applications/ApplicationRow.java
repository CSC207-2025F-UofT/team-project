package interface_adapter.track_applications;

/**
 * One application, formatted as text for the My Applications table.
 */
public final class ApplicationRow {

    private final String petName;
    private final String submittedAt;
    private final String status;
    private final String adminNote;

    /**
     * Creates a table row.
     * @param petName the name of the pet applied for
     * @param submittedAt the submission date as text
     * @param status the current status as text
     * @param adminNote the shelter's note, or a placeholder if there is none
     */
    public ApplicationRow(String petName, String submittedAt, String status, String adminNote) {
        this.petName = petName;
        this.submittedAt = submittedAt;
        this.status = status;
        this.adminNote = adminNote;
    }

    public String getPetName() {
        return petName;
    }

    public String getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminNote() {
        return adminNote;
    }
}
