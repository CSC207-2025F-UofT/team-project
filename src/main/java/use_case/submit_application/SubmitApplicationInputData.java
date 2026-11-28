package use_case.submit_application;

/**
 * The Input Data for the Submit Application Use Case.
 */
public final class SubmitApplicationInputData {

    private final String adopterId;
    private final String petId;
    private final String housingType;
    private final String petExperience;
    private final String householdMembers;
    private final String contactInfo;
    private final String note;

    /**
     * Creates the input data.
     * @param adopterId the adopter who is applying
     * @param petId the pet being applied for
     * @param housingType the adopter's housing type
     * @param petExperience the adopter's experience with pets
     * @param householdMembers who lives in the adopter's home
     * @param contactInfo how the shelter can reach the adopter
     * @param note an optional note
     */
    public SubmitApplicationInputData(String adopterId, String petId, String housingType, String petExperience,
                                      String householdMembers, String contactInfo, String note) {
        this.adopterId = adopterId;
        this.petId = petId;
        this.housingType = housingType;
        this.petExperience = petExperience;
        this.householdMembers = householdMembers;
        this.contactInfo = contactInfo;
        this.note = note;
    }

    String getAdopterId() {
        return adopterId;
    }

    String getPetId() {
        return petId;
    }

    String getHousingType() {
        return housingType;
    }

    String getPetExperience() {
        return petExperience;
    }

    String getHouseholdMembers() {
        return householdMembers;
    }

    String getContactInfo() {
        return contactInfo;
    }

    String getNote() {
        return note;
    }
}
