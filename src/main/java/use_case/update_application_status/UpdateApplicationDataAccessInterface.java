package use_case.update_application_status;

import entity.Application;

public interface UpdateApplicationDataAccessInterface {
    Application getApplicationById(String applicationId);
    void save(Application application);
}
