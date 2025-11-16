package use_case.save_outfit;

import entity.Outfit;

import java.util.List;

public class SaveOutfitInteractor implements SaveOutfitInputBoundary {

    private final OutfitDataAccessInterface outfitDataAccess;
    private final SaveOutfitOutputBoundary presenter;

    public SaveOutfitInteractor(OutfitDataAccessInterface outfitDataAccess,
                                SaveOutfitOutputBoundary presenter) {
        this.outfitDataAccess = outfitDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveOutfitInputData inputData) {
        // 1. Validate required fields (main alt flow: Missing required fields)
        if (inputData.getOutfitName() == null || inputData.getOutfitName().isBlank()) {
            presenter.prepareFailView("Outfit name is required.");
            return;
        }
        if (inputData.getItems() == null || inputData.getItems().isEmpty()) {
            presenter.prepareFailView("At least one item is required.");
            return;
        }
        if (inputData.getWeatherProfile() == null || inputData.getWeatherProfile().isBlank()) {
            presenter.prepareFailView("Weather profile is required.");
            return;
        }

        // 2. Check duplicate (alt: duplicate name for same profile/location)
        boolean exists = outfitDataAccess.existsByNameAndProfile(
                inputData.getOutfitName(),
                inputData.getWeatherProfile(),
                inputData.getLocation()
        );

        if (exists && !inputData.isOverwrite()) {
            presenter.prepareFailView(
                    "An outfit with this name and weather/location already exists. " +
                            "Please choose 'Overwrite' if you want to replace it.");
            return;
        }

        // 3. Create entity and save
        Outfit outfit = new Outfit(
                inputData.getOutfitName(),
                inputData.getItems(),
                inputData.getWeatherProfile(),
                inputData.getLocation()
        );

        outfitDataAccess.saveOutfit(outfit);

        // 4. Load all outfits so that the View can update "Saved Outfits" section
        List<Outfit> allOutfits = outfitDataAccess.getAllOutfits();

        SaveOutfitOutputData outputData = new SaveOutfitOutputData(
                "Outfit saved successfully.",
                allOutfits
        );

        presenter.prepareSuccessView(outputData);
    }
}
