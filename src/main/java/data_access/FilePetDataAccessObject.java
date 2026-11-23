package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;
import use_case.manage_pet.ManagePetDataAccessInterface;
import use_case.submit_application.SubmitApplicationPetDataAccessInterface;
import use_case.track_applications.TrackApplicationsPetDataAccessInterface;

/**
 * Stores pet profiles in a local JSON file and supplies pet details to application submission.
 */
public class FilePetDataAccessObject implements ManagePetDataAccessInterface,
        SubmitApplicationPetDataAccessInterface, TrackApplicationsPetDataAccessInterface {

    private final Path jsonPath;
    private Map<String, Pet> pets;

    /**
     * Opens the pet store or starts with an empty store if the file is absent.
     * @param jsonPath the pet data file
     */
    public FilePetDataAccessObject(Path jsonPath) {
        this.jsonPath = jsonPath.toAbsolutePath();
        this.pets = load();
    }

    @Override
    public synchronized List<Pet> getPets() {
        return new ArrayList<>(pets.values());
    }

    @Override
    public synchronized Pet findById(String id) {
        return pets.get(id);
    }

    @Override
    public synchronized void add(Pet pet) {
        if (pets.containsKey(pet.getId())) {
            throw new IllegalStateException("A pet with that ID already exists.");
        }
        final Map<String, Pet> updated = new LinkedHashMap<>(pets);
        updated.put(pet.getId(), pet);
        save(updated);
    }

    @Override
    public synchronized boolean update(Pet pet) {
        if (!pets.containsKey(pet.getId())) {
            return false;
        }
        final Map<String, Pet> updated = new LinkedHashMap<>(pets);
        updated.put(pet.getId(), pet);
        save(updated);
        return true;
    }

    @Override
    public synchronized boolean remove(String id) {
        if (!pets.containsKey(id)) {
            return false;
        }
        final Map<String, Pet> updated = new LinkedHashMap<>(pets);
        updated.remove(id);
        save(updated);
        return true;
    }

    @Override
    public synchronized boolean isAvailable(String petId) {
        final Pet pet = pets.get(petId);
        return pet != null && pet.getStatus() == PetStatus.AVAILABLE;
    }

    @Override
    public synchronized String getPetName(String petId) {
        final Pet pet = pets.get(petId);
        if (pet == null) {
            throw new IllegalArgumentException("Pet does not exist: " + petId);
        }
        return pet.getName();
    }

    private Map<String, Pet> load() {
        final Map<String, Pet> loaded = new LinkedHashMap<>();
        try {
            final JSONArray array = new JSONArray(Files.readString(jsonPath));
            for (int i = 0; i < array.length(); i++) {
                final Pet pet = readPet(array.getJSONObject(i));
                if (loaded.putIfAbsent(pet.getId(), pet) != null) {
                    throw new JSONException("Duplicate pet ID: " + pet.getId());
                }
            }
        }
        catch (NoSuchFileException ex) {
            return loaded;
        }
        catch (IOException | JSONException | IllegalArgumentException ex) {
            throw new IllegalStateException("Could not load pets from " + jsonPath, ex);
        }
        return loaded;
    }

    private Pet readPet(JSONObject json) {
        final JSONArray tags = json.getJSONArray("temperamentTags");
        final List<Temperament> temperament = new ArrayList<>();
        for (int i = 0; i < tags.length(); i++) {
            temperament.add(Temperament.valueOf(tags.getString(i)));
        }
        return new Pet(json.getString("id"), json.getString("name"), Species.valueOf(json.getString("species")),
                json.getString("breed"), json.getInt("ageYears"), PetSex.valueOf(json.getString("sex")), temperament,
                json.getString("healthNotes"), json.getBoolean("needsMedication"), json.getBoolean("vaccinated"),
                json.getBoolean("spayedNeutered"), json.getBigDecimal("adoptionFee"), json.getString("photoPath"),
                PetStatus.valueOf(json.getString("status")));
    }

    private JSONObject writePet(Pet pet) {
        final JSONArray temperament = new JSONArray();
        for (Temperament tag : pet.getTemperamentTags()) {
            temperament.put(tag.name());
        }
        final JSONObject json = new JSONObject();
        json.put("id", pet.getId());
        json.put("name", pet.getName());
        json.put("species", pet.getSpecies().name());
        json.put("breed", pet.getBreed());
        json.put("ageYears", pet.getAgeYears());
        json.put("sex", pet.getSex().name());
        json.put("temperamentTags", temperament);
        json.put("healthNotes", pet.getHealthNotes());
        json.put("needsMedication", pet.needsMedication());
        json.put("vaccinated", pet.isVaccinated());
        json.put("spayedNeutered", pet.isSpayedNeutered());
        json.put("adoptionFee", pet.getAdoptionFee().toString());
        json.put("photoPath", pet.getPhotoPath());
        json.put("status", pet.getStatus().name());
        return json;
    }

    private void save(Map<String, Pet> updated) {
        final JSONArray array = new JSONArray();
        for (Pet pet : updated.values()) {
            array.put(writePet(pet));
        }

        // Replace the file only after the complete new snapshot has been written.
        Path temporaryPath = null;
        try {
            temporaryPath = Files.createTempFile(jsonPath.getParent(), "pets-", ".tmp");
            Files.writeString(temporaryPath, array.toString(2));
            Files.move(temporaryPath, jsonPath, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException ex) {
            if (temporaryPath != null) {
                try {
                    Files.deleteIfExists(temporaryPath);
                }
                catch (IOException cleanupFailure) {
                    ex.addSuppressed(cleanupFailure);
                }
            }
            throw new IllegalStateException("Could not save pets to " + jsonPath, ex);
        }
        pets = updated;
    }
}
