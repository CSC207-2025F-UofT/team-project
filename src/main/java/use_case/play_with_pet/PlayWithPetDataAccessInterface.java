package use_case.play_with_pet;

import entity.Pet;

public interface PlayWithPetDataAccessInterface {

    Pet findByOwnerUsername(String username);

    void save(Pet pet);
}