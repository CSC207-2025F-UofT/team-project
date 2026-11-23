package data_access;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;
import use_case.browse_pets.BrowsePetsDataAccessInterface;

/**
 * Keeps pets in memory. Used for tests and as sample data until the JSON source is ready.
 */
public class InMemoryPetDataAccess implements BrowsePetsDataAccessInterface {
    private final List<Pet> pets;

    /**
     * Creates a store holding the given pets.
     *
     * @param pets the pets to hold
     */
    public InMemoryPetDataAccess(List<Pet> pets) {
        this.pets = new ArrayList<>(pets);
    }

    /**
     * Creates a store with a few sample pets so the gallery has something to show.
     *
     * @return a store with sample data
     */
    public static InMemoryPetDataAccess withSampleData() {
        final List<Pet> sample = new ArrayList<>();
        sample.add(sample("1", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE,
                List.of(Temperament.AFFECTIONATE, Temperament.CALM), PetStatus.AVAILABLE));
        sample.add(sample("2", "Biscuit", Species.CAT, "Siamese", 4, PetSex.MALE,
                List.of(Temperament.PLAYFUL, Temperament.GOOD_WITH_KIDS), PetStatus.AVAILABLE));
        sample.add(sample("3", "Shadow", Species.CAT, "Black cat", 6, PetSex.MALE,
                List.of(Temperament.SHY, Temperament.INDEPENDENT), PetStatus.AVAILABLE));
        sample.add(sample("4", "Pepper", Species.CAT, "Orange tabby", 1, PetSex.FEMALE,
                List.of(Temperament.ENERGETIC), PetStatus.AVAILABLE));
        sample.add(sample("5", "Olive", Species.CAT, "Calico", 3, PetSex.FEMALE,
                List.of(Temperament.PLAYFUL), PetStatus.ADOPTED));
        return new InMemoryPetDataAccess(sample);
    }

    private static Pet sample(String id, String name, Species species, String breed, int age, PetSex sex,
                              List<Temperament> tags, PetStatus status) {
        final boolean needsMedication = "3".equals(id);
        return new Pet(id, name, species, breed, age, sex, tags, "", needsMedication, true, true,
                new BigDecimal("150.00"), "", status);
    }

    @Override
    public List<Pet> getAvailablePets() {
        return pets.stream()
                .filter(pet -> pet.getStatus() == PetStatus.AVAILABLE)
                .collect(Collectors.toList());
    }
}
