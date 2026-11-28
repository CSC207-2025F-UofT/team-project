package interface_adapter.view_pet_details;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import entity.Pet;
import entity.PetProfileDetails;
import entity.Temperament;
import interface_adapter.ViewManagerModel;
import interface_adapter.browse_pets.AgeFilterOption;
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
        state.setBasicInfo(basicInfo(pet));
        state.setDetailLines(detailLines(pet));
        state.setHealthLines(healthLines(pet));
        state.setPhotoPath(pet.getPhotoPath());
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
        viewManagerModel.setActiveView(PetDetailViewModel.VIEW_NAME);
    }

    private String summary(Pet pet) {
        final String age = pet.getAgeYears() == 1 ? "1 year old" : pet.getAgeYears() + " years old";
        return capitalize(pet.getSpecies().name()) + ", " + pet.getBreed() + ", " + age;
    }

    private List<String> basicInfo(Pet pet) {
        final PetProfileDetails profile = pet.getProfileDetails();
        return List.of("Breed: " + pet.getBreed(),
                "Age: " + AgeFilterOption.forAge(pet.getAgeYears()).getLabel(),
                "Sex: " + capitalize(pet.getSex().name()),
                "Color: " + listedOrUnknown(profile.getColor()),
                "Hair length: " + listedOrUnknown(profile.getHairLength()));
    }

    private List<String> detailLines(Pet pet) {
        final Boolean microchipped = pet.getProfileDetails().getMicrochipped();
        final BigDecimal fee = pet.getAdoptionFee();
        return List.of("Microchipped: " + (microchipped == null ? "Not listed" : yesNo(microchipped)),
                "Temperament: " + temperaments(pet),
                "Adoption fee: " + (fee == null ? "Not listed" : "$" + fee.setScale(2, RoundingMode.HALF_UP)),
                "Status: " + capitalize(pet.getStatus().name()));
    }

    private List<String> healthLines(Pet pet) {
        return List.of("Vaccinated: " + yesNo(pet.isVaccinated()),
                "Spayed or neutered: " + yesNo(pet.isSpayedNeutered()),
                "Needs daily medication: " + yesNo(pet.needsMedication()),
                "Health notes: " + (pet.getHealthNotes().isEmpty() ? "None" : pet.getHealthNotes()));
    }

    private static String listedOrUnknown(String value) {
        return value.isEmpty() ? "Not listed" : value;
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
