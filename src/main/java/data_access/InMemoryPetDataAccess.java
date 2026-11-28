package data_access;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entity.Pet;
import entity.PetStatus;
import use_case.browse_pets.BrowsePetsDataAccessInterface;
import use_case.view_pet_details.ViewPetDetailsDataAccessInterface;

/**
 * Keeps a supplied set of pets in memory for the gallery and detail page.
 */
public class InMemoryPetDataAccess implements BrowsePetsDataAccessInterface, ViewPetDetailsDataAccessInterface {
    private final List<Pet> pets;

    /**
     * Creates a store holding the given pets.
     *
     * @param pets the pets to hold
     */
    public InMemoryPetDataAccess(List<Pet> pets) {
        this.pets = new ArrayList<>(pets);
    }

    @Override
    public List<Pet> getAvailablePets() {
        return pets.stream()
                .filter(pet -> pet.getStatus() == PetStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> findById(String petId) {
        return pets.stream().filter(pet -> pet.getId().equals(petId)).findFirst();
    }
}
