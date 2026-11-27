package use_case.add_clothing;

import entity.ClothingItem;
import entity.ClothingType;
import use_case.outfit.OutfitOutputData.ClothingData;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public final class AddClothingInteractor implements AddClothingInputBoundary {
    private final WardrobeRepository repository;
    private final ImageStore imageStore;
    private final WardrobeOutputBoundary output;

    public AddClothingInteractor(WardrobeRepository repository, ImageStore imageStore,
                                 WardrobeOutputBoundary output) {
        this.repository = repository;
        this.imageStore = imageStore;
        this.output = output;
    }

    @Override public void load() {
        run(() -> publish("", false));
    }

    @Override public void addClothing(String name, String type, String sourceImagePath) {
        run(() -> {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Enter a name for the clothing item.");
            }
            if (type == null || type.isBlank()) {
                throw new IllegalArgumentException("Select a clothing type.");
            }
            ClothingType clothingType;
            try {
                clothingType = ClothingType.valueOf(type);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Select a valid clothing type.");
            }
            if (sourceImagePath == null || sourceImagePath.isBlank()) {
                throw new IllegalArgumentException("Select an image of the clothing item.");
            }
            String storedPath = imageStore.store(sourceImagePath);
            ClothingItem item = new ClothingItem(UUID.randomUUID().toString(), name.trim(),
                    clothingType, storedPath, List.of());
            repository.saveClothing(item);
            publish("Added \"" + item.getName() + "\" to your wardrobe.", true);
        });
    }

    private void publish(String message, boolean added) {
        List<ClothingData> clothing = repository.getClothing().stream()
                .map(c -> new ClothingData(c.getId(), c.getName(),
                        c.getType().name(), c.getImagePath()))
                .collect(Collectors.toList());
        output.present(new WardrobeOutputData(clothing, message, added));
    }

    private void run(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException ex) { output.presentError(ex.getMessage()); }
        catch (RuntimeException ex) { output.presentError("Could not save the clothing item. Please retry."); }
    }
}
