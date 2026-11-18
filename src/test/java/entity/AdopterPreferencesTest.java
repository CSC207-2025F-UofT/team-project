package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

class AdopterPreferencesTest {

    @Test
    void gettersReturnConstructorValues() {
        AdopterPreferences prefs = new AdopterPreferences(true, false, true, false, true, Species.DOG);

        assertTrue(prefs.hasChildren());
        assertFalse(prefs.hasOtherPets());
        assertTrue(prefs.canHandleMedicalNeeds());
        assertFalse(prefs.canHandleKitten());
        assertTrue(prefs.canHandlePregnantAnimal());
        assertEquals(Species.DOG, prefs.getPreferredSpecies());
    }

    @Test
    void nullSpeciesMeansNoPreference() {
        AdopterPreferences prefs = new AdopterPreferences(false, false, false, false, false, null);

        assertNull(prefs.getPreferredSpecies());
    }

    @Test
    void sameValuesAreEqual() {
        AdopterPreferences a = new AdopterPreferences(true, true, false, false, true, Species.CAT);
        AdopterPreferences b = new AdopterPreferences(true, true, false, false, true, Species.CAT);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentValuesAreNotEqual() {
        AdopterPreferences a = new AdopterPreferences(true, true, false, false, true, Species.CAT);
        AdopterPreferences b = new AdopterPreferences(true, true, false, false, true, Species.DOG);

        assertNotEquals(a, b);
        assertNotEquals(a, null);
        assertNotEquals(a, "not preferences");
    }
}
