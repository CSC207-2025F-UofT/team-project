package data_access;

import entity.Pet;
import use_case.interact_with_pet.PetDataAccessInterface;
import use_case.adopt_pet.AdoptPetDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPetDataAccessObject
        implements PetDataAccessInterface, AdoptPetDataAccessInterface {

    private final Map<String, Pet> petsByOwner = new HashMap<>();

    @Override
    public Pet findByOwnerUsername(String username) {
        return petsByOwner.get(username);
    }

    public void save(Pet pet) {
        petsByOwner.put(pet.getOwnerUsername(), pet);
    }
}