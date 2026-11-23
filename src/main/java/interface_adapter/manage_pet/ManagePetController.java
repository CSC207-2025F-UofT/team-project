package interface_adapter.manage_pet;

import java.util.List;
import java.util.Optional;

import use_case.manage_pet.BreedLookupInputBoundary;
import use_case.manage_pet.ManagePetInputBoundary;
import use_case.manage_pet.ManagePetInputData;

/** Translates Swing form actions into pet management requests. */
public final class ManagePetController {
    private final ManagePetInputBoundary interactor;
    private final BreedLookupInputBoundary breedLookup;

    /**
     * Creates a controller with the use case's input boundary.
     * @param interactor the input boundary
     */
    public ManagePetController(ManagePetInputBoundary interactor) {
        this(interactor, name -> Optional.empty());
    }

    /**
     * Creates a controller for listing changes and breed suggestions.
     * @param interactor the listing input boundary
     * @param breedLookup the breed lookup input boundary
     */
    public ManagePetController(ManagePetInputBoundary interactor, BreedLookupInputBoundary breedLookup) {
        this.interactor = interactor;
        this.breedLookup = breedLookup;
    }

    /**
     * Looks up general breed information without editing the listing.
     * @param name the typed breed name
     * @return suggestions when the breed is known
     */
    public Optional<BreedReferenceViewData> lookupBreed(String name) {
        return breedLookup.lookup(name).map(ManagePetPresenter::toViewData);
    }

    /** Refreshes the listings. */
    public void refresh() {
        interactor.refresh();
    }

    /**
     * Submits the form for a new listing.
     * @param name the pet's name
     * @param species the selected species
     * @param breed the pet's breed or mix
     * @param ageYears age entered in years
     * @param sex the selected sex
     * @param temperamentTags the selected temperament tags
     * @param healthNotes the pet's health notes
     * @param needsMedication whether the pet needs medication
     * @param vaccinated whether the pet is vaccinated
     * @param spayedNeutered whether the pet is spayed or neutered
     * @param adoptionFee the entered adoption fee
     * @param photoPath the path to an optional local photo
     */
    public void add(String name, String species, String breed, String ageYears, String sex,
                    List<String> temperamentTags, String healthNotes, boolean needsMedication, boolean vaccinated,
                    boolean spayedNeutered, String adoptionFee, String photoPath) {
        interactor.add(new ManagePetInputData(name, species, breed, ageYears, sex, temperamentTags, healthNotes,
                needsMedication, vaccinated, spayedNeutered, adoptionFee, photoPath));
    }

    /**
     * Submits the form for an existing listing.
     * @param id the selected pet's ID
     * @param name the pet's name
     * @param species the selected species
     * @param breed the pet's breed or mix
     * @param ageYears age entered in years
     * @param sex the selected sex
     * @param temperamentTags the selected temperament tags
     * @param healthNotes the pet's health notes
     * @param needsMedication whether the pet needs medication
     * @param vaccinated whether the pet is vaccinated
     * @param spayedNeutered whether the pet is spayed or neutered
     * @param adoptionFee the entered adoption fee
     * @param photoPath the path to an optional local photo
     */
    public void edit(String id, String name, String species, String breed, String ageYears, String sex,
                     List<String> temperamentTags, String healthNotes, boolean needsMedication, boolean vaccinated,
                     boolean spayedNeutered, String adoptionFee, String photoPath) {
        interactor.edit(id, new ManagePetInputData(name, species, breed, ageYears, sex, temperamentTags, healthNotes,
                needsMedication, vaccinated, spayedNeutered, adoptionFee, photoPath));
    }

    /**
     * Removes the selected listing.
     * @param id the selected pet's ID
     */
    public void remove(String id) {
        interactor.remove(id);
    }
}
