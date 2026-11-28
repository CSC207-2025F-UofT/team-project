package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;

class PetCatalogTest {

    @TempDir
    Path tempDir;

    private Pet pet(String id, PetStatus status) {
        return new Pet(id, "Cat" + id, Species.CAT, "Tabby", 2, PetSex.FEMALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", status);
    }

    private FilePetDataAccessObject store() {
        return new FilePetDataAccessObject(tempDir.resolve("pets.json"));
    }

    @Test
    void onlyAvailableCatsAreListed() {
        final FilePetDataAccessObject store = store();
        store.add(pet("1", PetStatus.AVAILABLE));
        store.add(pet("2", PetStatus.ADOPTED));

        final List<Pet> shown = new PetCatalog(store).getAvailablePets();

        assertEquals(1, shown.size());
        assertEquals("1", shown.get(0).getId());
    }

    @Test
    void catAddedByTheAdminShowsUpRightAway() {
        final FilePetDataAccessObject store = store();
        final PetCatalog catalog = new PetCatalog(store);
        assertTrue(catalog.getAvailablePets().isEmpty());

        store.add(pet("5", PetStatus.AVAILABLE));

        assertEquals(1, catalog.getAvailablePets().size());
    }

    @Test
    void findByIdReturnsTheCatOrEmpty() {
        final FilePetDataAccessObject store = store();
        store.add(pet("1", PetStatus.AVAILABLE));
        final PetCatalog catalog = new PetCatalog(store);

        assertTrue(catalog.findById("1").isPresent());
        assertFalse(catalog.findById("nope").isPresent());
    }
}
