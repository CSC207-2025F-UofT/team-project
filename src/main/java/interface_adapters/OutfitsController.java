package interface_adapters;

import use_case.SaveOutfitInputBoundary;
import use_case.SaveOutfitInputData;

import java.util.Arrays;
import java.util.List;

public class SaveOutfitController {

    private final SaveOutfitInputBoundary interactor;

    public SaveOutfitController(SaveOutfitInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void saveOutfit(String outfitName,
                           String itemsText,
                           String weatherProfile,
                           String location,
                           boolean overwrite) {

        List<String> items = Arrays.stream(itemsText.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        SaveOutfitInputData inputData = new SaveOutfitInputData(
                outfitName,
                items,
                weatherProfile,
                location,
                overwrite
        );
        interactor.execute(inputData);
    }
}
