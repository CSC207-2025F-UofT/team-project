package dataAccess;

import entity.Pet;
import use_case.pet_management.PetDataAccessInterface;

import java.util.*;

public class InMemoryPetDataAccessObject implements PetDataAccessInterface {

    private final Map<Integer, Pet> pets = new HashMap<>();

    @Override
    public boolean existsById(int id) {
        return pets.containsKey(id);
    }

    @Override
    public Pet getById(int id) {
        return pets.get(id);
    }

    @Override
    public List<Pet> getAll() {
        return new ArrayList<>(pets.values());
    }

    @Override
    public void save(Pet pet) {
        pets.put(pet.getId(), pet);
    }

    @Override
    public void deleteById(int id) {
        pets.remove(id);
    }
}
