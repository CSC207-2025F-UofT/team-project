package data_access;

import entity.Outfit;
import use_case.save_outfit.OutfitDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOutfitDataAccessObject implements OutfitDataAccessInterface {

    private final List<Outfit> outfits = new ArrayList<>();

    @Override
    public void saveOutfit(Outfit outfit) {
        outfits.removeIf(o ->
                o.getName().equals(outfit.getName()) &&
                        o.getWeatherProfile().equals(outfit.getWeatherProfile()) &&
                        o.getLocation().equals(outfit.getLocation())
        );
        outfits.add(outfit);
    }

    @Override
    public boolean existsByNameAndProfile(String name, String weatherProfile, String location) {
        return outfits.stream().anyMatch(o ->
                o.getName().equals(name) &&
                        o.getWeatherProfile().equals(weatherProfile) &&
                        o.getLocation().equals(location)
        );
    }

    @Override
    public List<Outfit> getAllOutfits() {
        return new ArrayList<>(outfits);
    }
}
