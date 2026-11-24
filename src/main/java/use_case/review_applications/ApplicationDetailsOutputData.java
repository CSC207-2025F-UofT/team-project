package use_case.review_applications;

import java.time.LocalDate;

/**
 * One application with the adopter's answers, as shown when the admin opens it.
 */
public class ApplicationDetailsOutputData {

    private final String applicationId;
    private final String petId;
    private final String petName;
    private final String adopterName;
    private final LocalDate submittedAt;
    private final String status;
    private final String housingType;
    private final String petExperience;
    private final String householdMembers;
    private final String contactInfo;
    private final String note;
    private final String adminNote;

    /**
     * Creates the details of one application.
     * @param applicationId the application's id
     * @param petId the id of the pet applied for
     * @param petName the name of the pet applied for
     * @param adopterName the name of the adopter who applied
     * @param submittedAt the day the application was submitted
     * @param status the current status, for example PENDING
     * @param housingType the adopter's housing type
     * @param petExperience the adopter's experience with pets
     * @param householdMembers who lives in the adopter's home
     * @param contactInfo how the shelter can reach the adopter
     * @param note the adopter's note, empty if there is none
     * @param adminNote the shelter's note, empty if there is none
     */
    public ApplicationDetailsOutputData(String applicationId, String petId, String petName, String adopterName,
                                        LocalDate submittedAt, String status, String housingType,
                                        String petExperience, String householdMembers, String contactInfo,
                                        String note, String adminNote) {
        this.applicationId = applicationId;
        this.petId = petId;
        this.petName = petName;
        this.adopterName = adopterName;
        this.submittedAt = submittedAt;
        this.status = status;
        this.housingType = housingType;
        this.petExperience = petExperience;
        this.householdMembers = householdMembers;
        this.contactInfo = contactInfo;
        this.note = note;
        this.adminNote = adminNote;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public String getPetId() {
        return petId;
    }

    public String getPetName() {
        return petName;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public LocalDate getSubmittedAt() {
        return submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getHousingType() {
        return housingType;
    }

    public String getPetExperience() {
        return petExperience;
    }

    public String getHouseholdMembers() {
        return householdMembers;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getNote() {
        return note;
    }

    public String getAdminNote() {
        return adminNote;
    }
}
