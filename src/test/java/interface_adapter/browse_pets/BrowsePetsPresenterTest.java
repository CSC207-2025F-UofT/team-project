package interface_adapter.browse_pets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import use_case.browse_pets.BrowsePetsOutputData;

class BrowsePetsPresenterTest {

    private Pet pet(String name, int age) {
        return new Pet("1", name, Species.CAT, "Tabby", age, PetSex.FEMALE, List.of(), "", false, true, true,
                BigDecimal.TEN, "", PetStatus.AVAILABLE);
    }

    @Test
    void formatsCardText() {
        final GalleryViewModel viewModel = new GalleryViewModel();

        new BrowsePetsPresenter(viewModel).presentPets(new BrowsePetsOutputData(List.of(pet("Miso", 2))));

        final PetCardData card = viewModel.getCards().get(0);
        assertEquals("1", card.getPetId());
        assertEquals("Miso", card.getName());
        assertEquals("Cat, Tabby, 2 years old", card.getSummary());
        assertEquals("Status: Available", card.getStatus());
    }

    @Test
    void singularYear() {
        final GalleryViewModel viewModel = new GalleryViewModel();

        new BrowsePetsPresenter(viewModel).presentPets(new BrowsePetsOutputData(List.of(pet("Kit", 1))));

        assertEquals("Cat, Tabby, 1 year old", viewModel.getCards().get(0).getSummary());
    }

    @Test
    void noMatchesGivesEmptyCards() {
        final GalleryViewModel viewModel = new GalleryViewModel();

        new BrowsePetsPresenter(viewModel).presentPets(new BrowsePetsOutputData(List.of()));

        assertTrue(viewModel.getCards().isEmpty());
    }
}
