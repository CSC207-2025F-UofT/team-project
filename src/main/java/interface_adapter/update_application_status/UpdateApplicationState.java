package interface_adapter.update_application_status;

import entity.Application;
import entity.ApplicationStatus;

public final class UpdateApplicationState {
    private ApplicationStatus newStatus;
    private String adminNote = "";

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

    public void setNewStatus(ApplicationStatus newStatus) {this.newStatus = newStatus;}

    public void setAdminNote(String adminNote) {this.adminNote = adminNote;}

    public void setUpdateError(String updateError) {this.updateError = updateError;}
}
