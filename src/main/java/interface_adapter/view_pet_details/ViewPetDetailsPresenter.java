package interface_adapter.view_pet_details;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import entity.Pet;
import entity.Temperament;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.FilterLabels;
import use_case.view_pet_details.ViewPetDetailsOutputBoundary;
import use_case.view_pet_details.ViewPetDetailsOutputData;

/**
 * Writes a cat's profile as text for the detail page and switches to that page.
 */
public class ViewPetDetailsPresenter implements ViewPetDetailsOutputBoundary {
    private final PetDetailViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the model the detail page listens to
     * @param viewManagerModel the model that chooses which screen shows
     */
    public ViewPetDetailsPresenter(PetDetailViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void presentPet(ViewPetDetailsOutputData outputData) {
        final Pet pet = outputData.getPet();
        final PetDetailState state = new PetDetailState();
        state.setPetId(pet.getId());
        state.setName(pet.getName());
        state.setSummary(summary(pet));
        state.setDetailLines(detailLines(pet));
        viewModel.setState(state);
        viewModel.notifyStateChanged();
        showDetailPage();
    }

    @Override
    public void presentNotFound(String message) {
        final PetDetailState state = new PetDetailState();
        state.setErrorMessage(message);
        viewModel.setState(state);
        viewModel.notifyStateChanged();
        showDetailPage();
    }

    private void showDetailPage() {
        viewManagerModel.setState(PetDetailViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    private String summary(Pet pet) {
        final String age = pet.getAgeYears() == 1 ? "1 year old" : pet.getAgeYears() + " years old";
        return capitalize(pet.getSpecies().name()) + ", " + pet.getBreed() + ", " + age;
    }

    private List<String> detailLines(Pet pet) {
        final List<String> lines = new ArrayList<>();
        lines.add("Sex: " + capitalize(pet.getSex().name()));
        lines.add("Temperament: " + temperaments(pet));
        lines.add("Health notes: " + (pet.getHealthNotes().isEmpty() ? "None" : pet.getHealthNotes()));
        lines.add("Needs daily medication: " + yesNo(pet.needsMedication()));
        lines.add("Vaccinated: " + yesNo(pet.isVaccinated()));
        lines.add("Spayed or neutered: " + yesNo(pet.isSpayedNeutered()));
        lines.add("Adoption fee: $" + pet.getAdoptionFee().setScale(2, java.math.RoundingMode.HALF_UP));
        lines.add("Status: " + capitalize(pet.getStatus().name()));
        return lines;
    }

    private String temperaments(Pet pet) {
        if (pet.getTemperamentTags().isEmpty()) {
            return "None listed";
        }
        return pet.getTemperamentTags().stream()
                .map((Temperament tag) -> FilterLabels.temperament(tag))
                .collect(Collectors.joining(", "));
    }

    private String yesNo(boolean value) {
        return value ? "Yes" : "No";
    }

    private String capitalize(String text) {
        return text.substring(0, 1) + text.substring(1).toLowerCase();
    }
}
