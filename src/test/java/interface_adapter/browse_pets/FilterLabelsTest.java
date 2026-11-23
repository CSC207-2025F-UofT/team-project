package interface_adapter.browse_pets;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import entity.Temperament;

class FilterLabelsTest {

    @Test
    void singleWordTagIsCapitalised() {
        assertEquals("Calm", FilterLabels.temperament(Temperament.CALM));
    }

    @Test
    void underscoresBecomeSpaces() {
        assertEquals("Good with kids", FilterLabels.temperament(Temperament.GOOD_WITH_KIDS));
    }
}
