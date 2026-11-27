package interface_adapter.update_application_status;

import entity.ApplicationStatus;

public final class UpdateApplicationState {
    private ApplicationStatus newStatus;
    private String adminNote = "";
    private String applicationId = "";
    private String petName = "";
    private String adopterName = "";

    private String updateError = "";

    public ApplicationStatus getNewStatus() {
        return newStatus;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public String getUpdateError() {
        return updateError;
    }

    public String getApplicationId() {return applicationId;}

    public String getPetName() {return petName;}

    public String getAdopterName() {return adopterName;}

    public void setNewStatus(ApplicationStatus newStatus) {this.newStatus = newStatus;}

    public void setAdminNote(String adminNote) {this.adminNote = adminNote;}

    public void setUpdateError(String updateError) {this.updateError = updateError;}

    public void setApplicationId(String applicationId) {this.applicationId = applicationId;}

    public void setPetName(String petName) {this.petName = petName;}

    public void setAdopterName(String adopterName) {this.adopterName = adopterName;}
}
