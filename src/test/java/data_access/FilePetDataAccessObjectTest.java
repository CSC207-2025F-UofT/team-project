package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;

class FilePetDataAccessObjectTest {

    @TempDir
    Path folder;

    @Test
    void allProfileFieldsSurviveReload() {
        final Path path = folder.resolve("pets.json");
        final FilePetDataAccessObject first = new FilePetDataAccessObject(path);
        assertTrue(first.getPets().isEmpty());
        first.add(pet("Mochi", PetStatus.AVAILABLE));

        final FilePetDataAccessObject reloaded = new FilePetDataAccessObject(path);
        assertEquals(1, reloaded.getPets().size());
        final Pet loaded = reloaded.findById("pet-1");
        assertEquals("pet-1", loaded.getId());
        assertEquals("Mochi", loaded.getName());
        assertEquals(Species.CAT, loaded.getSpecies());
        assertEquals("Domestic Shorthair", loaded.getBreed());
        assertEquals(4, loaded.getAgeYears());
        assertEquals(PetSex.FEMALE, loaded.getSex());
        assertEquals(List.of(Temperament.CALM, Temperament.AFFECTIONATE), loaded.getTemperamentTags());
        assertEquals("Daily medication", loaded.getHealthNotes());
        assertTrue(loaded.needsMedication());
        assertTrue(loaded.isVaccinated());
        assertFalse(loaded.isSpayedNeutered());
        assertEquals(new BigDecimal("145.50"), loaded.getAdoptionFee());
        assertEquals("photos/mochi.jpg", loaded.getPhotoPath());
        assertEquals(PetStatus.AVAILABLE, loaded.getStatus());
    }

    @Test
    void editsAndRemovalsPersistAndAffectApplicationAvailability() {
        final Path path = folder.resolve("pets.json");
        final FilePetDataAccessObject first = new FilePetDataAccessObject(path);
        first.add(pet("Mochi", PetStatus.AVAILABLE));
        assertTrue(first.isAvailable("pet-1"));
        assertTrue(first.update(pet("Mochi Jr.", PetStatus.PENDING)));

        final FilePetDataAccessObject afterEdit = new FilePetDataAccessObject(path);
        assertEquals("Mochi Jr.", afterEdit.getPetName("pet-1"));
        assertFalse(afterEdit.isAvailable("pet-1"));
        assertTrue(afterEdit.remove("pet-1"));

        final FilePetDataAccessObject afterRemoval = new FilePetDataAccessObject(path);
        assertFalse(afterRemoval.isAvailable("pet-1"));
        assertNull(afterRemoval.findById("pet-1"));
        assertFalse(afterRemoval.update(pet("Mochi Jr.", PetStatus.PENDING)));
        assertFalse(afterRemoval.remove("pet-1"));
    }

    @Test
    void failedWriteKeepsPreviousSnapshotAndAllowsRetry() throws IOException {
        final Path path = folder.resolve("pets.json");
        final FilePetDataAccessObject dao = new FilePetDataAccessObject(path);
        dao.add(pet("Mochi", PetStatus.AVAILABLE));
        final Path backup = Files.move(path, folder.resolve("original.json"));
        Files.createDirectory(path);
        final Path marker = Files.createFile(path.resolve("blocked"));

        final Pet updated = pet("Mochi Jr.", PetStatus.PENDING);
        assertThrows(IllegalStateException.class, () -> dao.update(updated));
        assertEquals("Mochi", dao.getPetName("pet-1"));
        assertEquals("Mochi", new FilePetDataAccessObject(backup).getPetName("pet-1"));

        Files.delete(marker);
        Files.delete(path);
        Files.move(backup, path);
        assertTrue(dao.update(updated));
        assertEquals("Mochi Jr.", new FilePetDataAccessObject(path).getPetName("pet-1"));
    }

    private Pet pet(String name, PetStatus status) {
        return new Pet("pet-1", name, Species.CAT, "Domestic Shorthair", 4, PetSex.FEMALE,
                List.of(Temperament.CALM, Temperament.AFFECTIONATE), "Daily medication", true, true, false,
                new BigDecimal("145.50"), "photos/mochi.jpg", status);
    }
}
