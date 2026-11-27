package data_access;

import entity.Pet;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.adopt_pet.AdoptPetDataAccessInterface;
import use_case.interact_with_pet.PetDataAccessInterface;
import use_case.play_with_pet.PlayWithPetDataAccessInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class FilePetDataAccessObject
        implements PetDataAccessInterface,
        AdoptPetDataAccessInterface,
        PlayWithPetDataAccessInterface {

    private final Path filePath;
    private final Map<String, Pet> petsByOwner = new HashMap<>();

    public FilePetDataAccessObject(String filename) {
        filePath = Paths.get(filename).toAbsolutePath();
        loadPets();
    }

    private void loadPets() {
        String contents;

        try {
            contents = Files.readString(filePath);
        }
        catch (NoSuchFileException exception) {
            // No save file yet: start with no pets.
            return;
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not read saved pets.", exception);
        }

        try {
            JSONArray pets = new JSONArray(contents);

            for (int i = 0; i < pets.length(); i++) {
                JSONObject json = pets.getJSONObject(i);
                Pet pet = PetJsonMapper.fromJson(json);

                if (petsByOwner.containsKey(pet.getOwnerUsername())) {
                    throw new IllegalArgumentException(
                            "Duplicate pet owner in save file.");
                }

                petsByOwner.put(pet.getOwnerUsername(), pet);
            }
        }
        catch (RuntimeException exception) {
            throw new IllegalStateException(
                    "The pet save file contains invalid data.", exception);
        }
    }

    @Override
    public synchronized Pet findByOwnerUsername(String username) {
        Pet pet = petsByOwner.get(username);
        return pet == null ? null : copyPet(pet);
    }

    @Override
    public synchronized void save(Pet pet) {
        Map<String, Pet> updatedPets = new HashMap<>(petsByOwner);
        updatedPets.put(pet.getOwnerUsername(), copyPet(pet));

        JSONArray pets = new JSONArray();

        for (Pet savedPet : updatedPets.values()) {
            pets.put(PetJsonMapper.toJson(savedPet));
        }

        Path temporaryFile = null;

        try {
            Files.createDirectories(filePath.getParent());

            temporaryFile = Files.createTempFile(
                    filePath.getParent(), "pets-", ".tmp");

            Files.writeString(temporaryFile, pets.toString(2));

            Files.move(
                    temporaryFile,
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        }
        catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not save your pet.", exception);
        }
        finally {
            if (temporaryFile != null) {
                try {
                    Files.deleteIfExists(temporaryFile);
                }
                catch (IOException ignored) {
                    // A leftover temporary file won't affect saved pets.
                }
            }
        }

        petsByOwner.clear();
        petsByOwner.putAll(updatedPets);
    }

    private Pet copyPet(Pet pet) {
        return PetJsonMapper.fromJson(PetJsonMapper.toJson(pet));
    }
}