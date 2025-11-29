package entity;


import java.time.LocalDate;

public class AdoptionApplication {
    // Required info
    enum IDType {PHOTO_CARD, DRIVERS_LICENSE, MAIL, OTHER}

    private Pet corespondingPet;
    private String adopterName;
    private IDType idType;
    private String[] adopterAddress;
    private String adopterPhone;
    private String adopterEmail;
    private LocalDate applicationDate;

    // Adopter Survey info
    private SurveyInfo surveyInfo;

    /* GETTERS (FOR OTHER USE) */

    public Pet getCorespondingPet() {
        return corespondingPet;
    }

    public String getAdopterName() {
        return adopterName;
    }

    public IDType getIdType() {
        return idType;
    }

    public String[] getAdopterAddress() {
        return adopterAddress;
    }

    public String getAdopterPhone() {
        return adopterPhone;
    }

    public String getAdopterEmail() {
        return adopterEmail;
    }

    public SurveyInfo getSurveyInfo() {
        return surveyInfo;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    /* SETTERS (FOR BUILDER USE) */
    public void setCorespondingPet(Pet corespondingPet) {
        this.corespondingPet = corespondingPet;
    }

    public void setAdopterName(String adopterName) {
        this.adopterName = adopterName;
    }

    public void setIdType(IDType idType) {
        this.idType = idType;
    }

    public void setAdopterAddress(String[] adopterAddress) {
        this.adopterAddress = adopterAddress;
    }

    public void setAdopterPhone(String adopterPhone) {
        this.adopterPhone = adopterPhone;
    }

    public void setAdopterEmail(String adopterEmail) {
        this.adopterEmail = adopterEmail;
    }

    public void setSurveyInfo(SurveyInfo surveyInfo) {
        this.surveyInfo = surveyInfo;
    }

    public void setApplicationDate() {
        this.applicationDate = LocalDate.now();
    }
}