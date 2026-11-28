package data_access;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import entity.Pet;
import entity.PetStatus;
import use_case.browse_pets.BrowsePetsDataAccessInterface;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.view_pet_details.ViewPetDetailsDataAccessInterface;

/**
 * Lets the gallery and the detail page read the same pets the admin manages. It only reads; the pet store
 * stays the single place where pets are written.
 */
public class PetCatalog implements BrowsePetsDataAccessInterface, ViewPetDetailsDataAccessInterface {
    private final ManagePetDataAccessInterface store;

    /**
     * Creates a read-only view over a pet store.
     *
     * @param store the pet store shared with pet management
     */
    public PetCatalog(ManagePetDataAccessInterface store) {
        this.store = store;
    }

    @Override
    public List<Pet> getAvailablePets() {
        return store.getPets().stream()
                .filter(pet -> pet.getStatus() == PetStatus.AVAILABLE)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Pet> findById(String petId) {
        return Optional.ofNullable(store.findById(petId));
    }
}
