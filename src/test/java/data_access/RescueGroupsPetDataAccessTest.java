package data_access;

import java.nio.file.Path;
import java.util.List;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Temperament;
import use_case.browse_pets.AgeRangeFilter;
import use_case.browse_pets.HealthNeedsFilter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RescueGroupsPetDataAccessTest {
    private static final String SAMPLE_RESPONSE = """
            {
              "data": [
                {
                  "id":"1", "type":"animals",
                  "attributes":{
                    "name":"Juniper", "breedString":"Domestic Short Hair", "sex":"Female",
                    "qualities":"Playful, Shy", "isKidsOk":true,
                    "specialNeedsDetails":"Needs a quiet home", "isCurrentVaccinations":true,
                    "url":"https://example.org/cats/juniper"
                  },
                  "meta":{"ageString":"7 Months"},
                  "relationships":{
                    "orgs":{"data":[{"type":"orgs","id":"12"}]},
                    "pictures":{"data":[{"type":"pictures","id":"42"}]}
                  }
                },
                {
                  "id":"2", "type":"animals", "attributes":{"name":"Maple","ageGroup":"Adult"},
                  "relationships":{"orgs":{"data":{"type":"orgs","id":"12"}}}
                },
                {
                  "id":"3", "type":"animals", "attributes":{"name":"Missing rescue"}
                },
                {
                  "id":"4", "type":"animals", "attributes":{"name":"Already matched","isAdoptionPending":true},
                  "relationships":{"orgs":{"data":{"type":"orgs","id":"12"}}}
                }
              ],
              "included": [
                {"type":"orgs","id":"12","attributes":{"name":"Example Rescue","url":"https://example.org/"}},
                {"type":"pictures","id":"42","attributes":{"small":"https://cdn.rescuegroups.org/juniper.jpg"}}
              ]
            }
            """;

    @Test
    void mapsListingWithTheRescueNameAndPhotoFromIncludedResources() {
        final List<Pet> cats = RescueGroupsPetMapper.read(new JSONObject(SAMPLE_RESPONSE));
        assertEquals(2, cats.size());
        final Pet cat = cats.get(0);
        assertEquals("rescuegroups:1", cat.getId());
        assertEquals("Juniper", cat.getName());
        assertEquals("Domestic Short Hair", cat.getBreed());
        assertEquals(PetSex.FEMALE, cat.getSex());
        assertEquals(PetStatus.AVAILABLE, cat.getStatus());
        assertTrue(cat.getTemperamentTags().containsAll(List.of(
                Temperament.PLAYFUL, Temperament.SHY, Temperament.GOOD_WITH_KIDS)));
        assertTrue(cat.getHealthNotes().contains("Needs a quiet home"));
        assertTrue(cat.getHealthNotes().contains("Vaccinations current"));
        assertEquals("https://cdn.rescuegroups.org/juniper.jpg", cat.getPhotoPath());
        assertEquals("Example Rescue", cat.getExternalListing().getOrganizationName());
        assertEquals("https://example.org/", cat.getExternalListing().getOrganizationUrl());
        assertEquals("https://example.org/cats/juniper", cat.getExternalListing().getAdoptionUrl());
        assertEquals("7 Months", cat.getExternalListing().getAgeLabel());
        assertTrue(new AgeRangeFilter(0, 1).matches(cat));
    }

    @Test
    void unknownFieldsDoNotPassTheKittenOrNoMedicalNeedsFilters() {
        final Pet cat = RescueGroupsPetMapper.read(new JSONObject(SAMPLE_RESPONSE)).get(1);
        assertEquals(PetSex.UNKNOWN, cat.getSex());
        assertTrue(cat.getHealthNotes().isEmpty());
        assertEquals("Adult", cat.getExternalListing().getAgeLabel());
        assertEquals("https://example.org/", cat.getExternalListing().getAdoptionUrl());
        assertFalse(new AgeRangeFilter(0, 1).matches(cat));
        assertFalse(new HealthNeedsFilter(false).matches(cat));
    }

    @Test
    void externalCatsAppearInTheCatalogWithoutEnteringTheLocalStore(@TempDir Path directory) {
        final FilePetDataAccessObject local = new FilePetDataAccessObject(directory.resolve("pets.json"),
                InMemoryPetDataAccess.withSampleData().getAvailablePets());
        final List<Pet> external = RescueGroupsPetMapper.read(new JSONObject(SAMPLE_RESPONSE));
        final PetCatalog catalog = new PetCatalog(local, () -> external);

        assertEquals(local.getAvailablePets().size() + 2, catalog.getAvailablePets().size());
        assertEquals("Miso", catalog.findById("1").orElseThrow().getName());
        assertEquals("Juniper", catalog.findById("rescuegroups:1").orElseThrow().getName());
        assertFalse(local.isAvailable("rescuegroups:1"));
    }

    @Test
    void usesATorontoRadiusAndDoesNotFetchWithoutAKey() throws Exception {
        final JSONObject radius = RescueGroupsPetDataAccess.searchBody()
                .getJSONObject("data").getJSONObject("filterRadius");
        assertEquals("43.6532,-79.3832", radius.getString("coordinates"));
        assertEquals(50, radius.getInt("kilometers"));

        final RescueGroupsPetDataAccess disabled = new RescueGroupsPetDataAccess(null);
        disabled.refresh();
        assertTrue(disabled.getAvailablePets().isEmpty());
    }
}
