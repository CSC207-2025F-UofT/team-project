package use_case.adopt_pet;

import entity.Pet;

public interface AdoptPetDataAccessInterface {

    Pet findByOwnerUsername(String username);

    void save(Pet pet);
}
