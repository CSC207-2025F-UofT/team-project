package use_case.update_application_status;

import entity.Pet;

public interface UpdateApplicationPetDataAccessInterface {
    Pet findById(String id);
}
