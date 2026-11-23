package interface_adapter.browse_pets;

import java.util.List;
import java.util.stream.Collectors;

import entity.Pet;
import entity.PetStatus;
import use_case.browse_pets.BrowsePetsOutputBoundary;
import use_case.browse_pets.BrowsePetsOutputData;

/**
 * Turns matching pets into card text and hands it to the view model.
 */
public class BrowsePetsPresenter implements BrowsePetsOutputBoundary {
    private final GalleryViewModel viewModel;

    /**
     * Creates the presenter.
     *
     * @param viewModel the model the gallery view listens to
     */
    public BrowsePetsPresenter(GalleryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentPets(BrowsePetsOutputData outputData) {
        final List<PetCardData> cards = outputData.getPets().stream()
                .map(this::toCard)
                .collect(Collectors.toList());
        viewModel.setCards(cards);
    }

    private PetCardData toCard(Pet pet) {
        final String age = pet.getAgeYears() == 1 ? "1 year old" : pet.getAgeYears() + " years old";
        final String summary = capitalize(pet.getSpecies().name()) + ", " + pet.getBreed() + ", " + age;
        return new PetCardData(pet.getName(), summary, statusText(pet.getStatus()));
    }

    private String statusText(PetStatus status) {
        return "Status: " + capitalize(status.name());
    }

    private String capitalize(String text) {
        return text.substring(0, 1) + text.substring(1).toLowerCase();
    }
}
