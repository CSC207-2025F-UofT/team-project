package interface_adapter.review_applications;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The state for the Review Applications View Model: the list, and the application the admin opened.
 * Every value is text, ready to show.
 */
public final class ReviewApplicationsState {

    // ---------- The list ----------
    // One entry per application; the lists always have the same length, see addApplication.

    private final List<String> applicationIds = new ArrayList<>();
    private final List<String> petNames = new ArrayList<>();
    private final List<String> adopterNames = new ArrayList<>();
    private final List<String> submittedDates = new ArrayList<>();
    private final List<String> statuses = new ArrayList<>();
    private String message = "";

    // ---------- The opened application ----------
    // detailApplicationId is empty when no application is open.

    private String detailApplicationId = "";
    private String detailPetName = "";
    private String detailAdopterName = "";
    private String detailSubmittedAt = "";
    private String detailStatus = "";
    private String detailHousingType = "";
    private String detailPetExperience = "";
    private String detailHouseholdMembers = "";
    private String detailContactInfo = "";
    private String detailNote = "";
    private String detailAdminNote = "";
    private String detailMessage = "";

    /**
     * Adds one application to the end of the list.
     * @param applicationId the application's id, used when the admin opens it
     * @param petName the name of the pet applied for
     * @param adopterName the name of the adopter who applied
     * @param submittedAt the submission date as text
     * @param status the current status as text
     */
    public void addApplication(String applicationId, String petName, String adopterName, String submittedAt,
                               String status) {
        applicationIds.add(applicationId);
        petNames.add(petName);
        adopterNames.add(adopterName);
        submittedDates.add(submittedAt);
        statuses.add(status);
    }

    /**
     * Empties the list.
     */
    public void clearApplications() {
        applicationIds.clear();
        petNames.clear();
        adopterNames.clear();
        submittedDates.clear();
        statuses.clear();
    }

    public int getApplicationCount() {
        return applicationIds.size();
    }

    public List<String> getApplicationIds() {
        return Collections.unmodifiableList(applicationIds);
    }

    public List<String> getPetNames() {
        return Collections.unmodifiableList(petNames);
    }

    public List<String> getAdopterNames() {
        return Collections.unmodifiableList(adopterNames);
    }

    public List<String> getSubmittedDates() {
        return Collections.unmodifiableList(submittedDates);
    }

    public List<String> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    /**
     * Returns the message shown above the list, for example a hint or why the list is empty.
     * @return the list message, empty if there is none
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Returns whether an application is open in the details panel.
     * @return true if the detail fields describe an application
     */
    public boolean hasDetails() {
        return !detailApplicationId.isEmpty();
    }

    /**
     * Closes the opened application: every detail field becomes empty. The details message is kept.
     */
    public void clearDetails() {
        detailApplicationId = "";
        detailPetName = "";
        detailAdopterName = "";
        detailSubmittedAt = "";
        detailStatus = "";
        detailHousingType = "";
        detailPetExperience = "";
        detailHouseholdMembers = "";
        detailContactInfo = "";
        detailNote = "";
        detailAdminNote = "";
    }

    public String getDetailApplicationId() {
        return detailApplicationId;
    }

    public void setDetailApplicationId(String detailApplicationId) {
        this.detailApplicationId = detailApplicationId;
    }

    public String getDetailPetName() {
        return detailPetName;
    }

    public void setDetailPetName(String detailPetName) {
        this.detailPetName = detailPetName;
    }

    public String getDetailAdopterName() {
        return detailAdopterName;
    }

    public void setDetailAdopterName(String detailAdopterName) {
        this.detailAdopterName = detailAdopterName;
    }

    public String getDetailSubmittedAt() {
        return detailSubmittedAt;
    }

    public void setDetailSubmittedAt(String detailSubmittedAt) {
        this.detailSubmittedAt = detailSubmittedAt;
    }

    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getDetailHousingType() {
        return detailHousingType;
    }

    public void setDetailHousingType(String detailHousingType) {
        this.detailHousingType = detailHousingType;
    }

    public String getDetailPetExperience() {
        return detailPetExperience;
    }

    public void setDetailPetExperience(String detailPetExperience) {
        this.detailPetExperience = detailPetExperience;
    }

    public String getDetailHouseholdMembers() {
        return detailHouseholdMembers;
    }

    public void setDetailHouseholdMembers(String detailHouseholdMembers) {
        this.detailHouseholdMembers = detailHouseholdMembers;
    }

    public String getDetailContactInfo() {
        return detailContactInfo;
    }

    public void setDetailContactInfo(String detailContactInfo) {
        this.detailContactInfo = detailContactInfo;
    }

    public String getDetailNote() {
        return detailNote;
    }

    public void setDetailNote(String detailNote) {
        this.detailNote = detailNote;
    }

    public String getDetailAdminNote() {
        return detailAdminNote;
    }

    public void setDetailAdminNote(String detailAdminNote) {
        this.detailAdminNote = detailAdminNote;
    }

    /**
     * Returns the message shown in the details panel, for example why the application could not be opened.
     * @return the details message, empty if there is none
     */
    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }
}
