package use_case.manage_pet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import entity.Pet;
import entity.PetSex;
import entity.PetStatus;
import entity.Species;
import entity.Temperament;

/** Validates and carries out the admin's pet listing changes. */
public final class ManagePetInteractor implements ManagePetInputBoundary {
    private static final String PET_MISSING = "This pet listing no longer exists.";

    private final ManagePetDataAccessInterface dataAccess;
    private final ManagePetOutputBoundary presenter;

    /**
     * Creates the interactor with its input and output collaborators.
     * @param dataAccess the pet storage port
     * @param presenter the output boundary
     */
    public ManagePetInteractor(ManagePetDataAccessInterface dataAccess, ManagePetOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void refresh() {
        publishListings("");
    }

    @Override
    public void add(ManagePetInputData inputData) {
        final Pet pet;
        try {
            pet = createPet(UUID.randomUUID().toString(), inputData, PetStatus.AVAILABLE);
            dataAccess.add(pet);
        }
        catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }
        catch (IllegalStateException exception) {
            presenter.prepareFailView("The pet could not be added. Please try again.");
            return;
        }
        publishListings("Added " + pet.getName() + ".");
    }

    @Override
    public void edit(String petId, ManagePetInputData inputData) {
        final Pet revised;
        try {
            final Pet existing = dataAccess.findById(petId);
            if (existing == null) {
                presenter.prepareFailView(PET_MISSING);
                return;
            }
            revised = createPet(existing.getId(), inputData, existing.getStatus());
            if (!dataAccess.update(revised)) {
                presenter.prepareFailView(PET_MISSING);
                return;
            }
        }
        catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }
        catch (IllegalStateException exception) {
            presenter.prepareFailView("The pet could not be updated. Please try again.");
            return;
        }
        publishListings("Updated " + revised.getName() + ".");
    }

    @Override
    public void remove(String petId) {
        try {
            if (!dataAccess.remove(petId)) {
                presenter.prepareFailView(PET_MISSING);
                return;
            }
        }
        catch (IllegalStateException exception) {
            presenter.prepareFailView("The pet could not be removed. Please try again.");
            return;
        }
        publishListings("Pet listing removed.");
    }

    private void publishListings(String message) {
        final List<Pet> pets;
        try {
            pets = dataAccess.getPets();
        }
        catch (IllegalStateException exception) {
            presenter.prepareFailView((message + " The pet list could not be loaded. Please refresh.").strip());
            return;
        }
        presenter.prepareSuccessView(new ManagePetOutputData(pets, message));
    }

    private static Pet createPet(String petId, ManagePetInputData inputData, PetStatus status) {
        final Species species = parseChoice(Species.class, inputData.getSpecies(), "Choose a species.");
        final PetSex sex = parseChoice(PetSex.class, inputData.getSex(), "Choose the pet's sex.");
        final List<Temperament> temperamentTags = new ArrayList<>();
        for (String tag : inputData.getTemperamentTags()) {
            temperamentTags.add(parseChoice(Temperament.class, tag, "Choose valid temperament tags."));
        }
        return new Pet(petId, inputData.getName(), species, inputData.getBreed(), parseAge(inputData.getAgeYears()),
                sex, temperamentTags, inputData.getHealthNotes(), inputData.needsMedication(),
                inputData.isVaccinated(), inputData.isSpayedNeutered(), parseFee(inputData.getAdoptionFee()),
                inputData.getPhotoPath(), status);
    }

    private static int parseAge(String age) {
        if (age != null) {
            try {
                return Integer.parseInt(age.strip());
            }
            catch (NumberFormatException exception) {
                throw new IllegalArgumentException("Enter the age in whole years.", exception);
            }
        }
        throw new IllegalArgumentException("Enter the age in whole years.");
    }

    private static BigDecimal parseFee(String fee) {
        if (fee == null || !fee.strip().matches("-?[0-9]+(\\.[0-9]+)?")) {
            throw new IllegalArgumentException("Enter an adoption fee as a decimal amount, for example 75.00.");
        }
        return new BigDecimal(fee.strip());
    }

    private static <T extends Enum<T>> T parseChoice(Class<T> type, String value, String error) {
        if (value == null) {
            throw new IllegalArgumentException(error);
        }
        try {
            return Enum.valueOf(type, value);
        }
        catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(error, exception);
        }
    }
}
