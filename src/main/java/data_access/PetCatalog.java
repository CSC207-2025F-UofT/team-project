package data_access;

import java.util.ArrayList;
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
    private final BrowsePetsDataAccessInterface externalPets;

    /**
     * Creates a read-only view over a pet store.
     *
     * @param store the pet store shared with pet management
     */
    public PetCatalog(ManagePetDataAccessInterface store) {
        this(store, List::of);
    }

    /**
     * Combines local listings and an external source for the public catalog.
     * @param store the local store used by pet management
     * @param externalPets the read-only source
     */
    public PetCatalog(ManagePetDataAccessInterface store, BrowsePetsDataAccessInterface externalPets) {
        this.store = store;
        this.externalPets = externalPets;
    }

    @Override
    public List<Pet> getAvailablePets() {
        final List<Pet> pets = new ArrayList<>(store.getPets().stream()
                .filter(pet -> pet.getStatus() == PetStatus.AVAILABLE)
                .collect(Collectors.toList()));
        pets.addAll(externalPets.getAvailablePets());
        return pets;
    }

    @Override
    public Optional<Pet> findById(String petId) {
        final Pet local = store.findById(petId);
        return local == null ? externalPets.getAvailablePets().stream()
                .filter(pet -> pet.getId().equals(petId)).findFirst() : Optional.of(local);
    }
}
