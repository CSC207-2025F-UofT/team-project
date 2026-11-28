package interface_adapter.view_pet_details;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;
import interface_adapter.ViewManagerModel;
import use_case.view_pet_details.ViewPetDetailsOutputData;

class ViewPetDetailsPresenterTest {
    private final PetDetailViewModel viewModel = new PetDetailViewModel();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private final ViewPetDetailsPresenter presenter = new ViewPetDetailsPresenter(viewModel, viewManagerModel);

    private Pet pet(String notes, List<Temperament> tags) {
        return new Pet("7", "Miso", Species.CAT, "Tabby", 2, PetSex.FEMALE, tags, notes, true, true, false,
                new BigDecimal("150"), "", PetStatus.AVAILABLE);
    }

    @Test
    void writesTheProfileAsText() {
        presenter.presentPet(new ViewPetDetailsOutputData(
                pet("Eats special food", List.of(Temperament.CALM, Temperament.GOOD_WITH_KIDS))));

        final PetDetailState state = viewModel.getState();
        assertEquals("7", state.getPetId());
        assertEquals("Miso", state.getName());
        assertEquals("Cat, Tabby, 2 years old", state.getSummary());
        assertTrue(state.getDetailLines().contains("Temperament: Calm, Good with kids"));
        assertTrue(state.getDetailLines().contains("Health notes: Eats special food"));
        assertTrue(state.getDetailLines().contains("Needs daily medication: Yes"));
        assertTrue(state.getDetailLines().contains("Vaccinated: Yes"));
        assertTrue(state.getDetailLines().contains("Spayed or neutered: No"));
        assertTrue(state.getDetailLines().contains("Adoption fee: $150.00"));
        assertTrue(state.getDetailLines().contains("Status: Available"));
    }

    @Test
    void emptyFieldsAreWrittenOutInWords() {
        presenter.presentPet(new ViewPetDetailsOutputData(pet("", List.of())));

        assertTrue(viewModel.getState().getDetailLines().contains("Temperament: None listed"));
        assertTrue(viewModel.getState().getDetailLines().contains("Health notes: None"));
    }

    @Test
    void switchesToTheDetailPageAndNotifiesTheView() {
        final List<String> events = new ArrayList<>();
        viewModel.addListener(() -> events.add("state"));
        viewManagerModel.addPropertyChangeListener(event -> events.add("view:" + event.getNewValue()));

        presenter.presentPet(new ViewPetDetailsOutputData(pet("", List.of())));

        assertEquals(List.of("state", "view:" + PetDetailViewModel.VIEW_NAME), events);
    }

    @Test
    void notFoundShowsTheMessage() {
        presenter.presentNotFound("This cat is no longer available.");

        assertEquals("This cat is no longer available.", viewModel.getState().getErrorMessage());
        assertEquals(PetDetailViewModel.VIEW_NAME, viewManagerModel.getActiveView());
    }
}
