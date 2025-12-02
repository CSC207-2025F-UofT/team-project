package dataAccess;

import entity.Pet;
import service.Backend;
import use_case.pet_management.PetDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * PetDataAccessInterface implementation backed by the shared Backend instance.
 */
public class BackendPetDataAccessObject implements PetDataAccessInterface {

    private final Backend backend;

    public BackendPetDataAccessObject(Backend backend) {
        this.backend = backend;
    }

    @Override
    public boolean existsById(int id) {
        return backend.pets.containsKey(id);
    }

    @Override
    public Pet getById(int id) {
        return backend.pets.get(id);
    }

    @Override
    public List<Pet> getAll() {
        return new ArrayList<>(backend.pets.values());
    }

    @Override
    public void save(Pet pet) {
        // New pet: use addPet (which asserts it's new)
        if (!backend.pets.containsKey(pet.getId())) {
            backend.addPet(pet);
        }
        // Existing pet: overwrite the entry
        else {
            backend.pets.put(pet.getId(), pet);
        }
    }

    @Override
    public void deleteById(int id) {
        Pet existing = backend.pets.get(id);
        if (existing != null) {
            backend.removePet(existing);
        }
    }
}
