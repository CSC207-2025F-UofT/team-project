package use_case.outfit;

import entity.*;
import java.util.*;
import java.util.stream.Collectors;

public final class OutfitInteractor implements OutfitInputBoundary {
    private final OutfitRepository repository;
    private final OutfitOutputBoundary output;
    private String selectedTagId;

    public OutfitInteractor(OutfitRepository repository, OutfitOutputBoundary output) {
        this.repository = repository; this.output = output;
    }
    @Override public void load(String tagId) {
        run(() -> {
            if (tagId != null && repository.getTags().stream().noneMatch(t -> t.getId().equals(tagId))) {
                throw new IllegalArgumentException("This tag no longer exists.");
            }
            selectedTagId = tagId;
            publish("", false);
        });
    }
    @Override public void createOutfit(String name, List<String> clothingIds, List<String> tagIds) {
        run(() -> {
            List<String> items = new ArrayList<>(new LinkedHashSet<>(clothingIds));
            List<String> tags = new ArrayList<>(new LinkedHashSet<>(tagIds));
            Set<String> existingClothing = repository.getClothing().stream()
                    .map(ClothingItem::getId).collect(Collectors.toSet());
            Set<String> existingTags = repository.getTags().stream()
                    .map(Tag::getId).collect(Collectors.toSet());
            if (!existingClothing.containsAll(items) || !existingTags.containsAll(tags)) {
                throw new IllegalArgumentException("A selected item or tag no longer exists. Reload and try again.");
            }
            Outfit outfit = new Outfit(UUID.randomUUID().toString(), name, items, tags);
            repository.saveOutfit(outfit);
            selectedTagId = null;
            publish("Outfit saved.", true);
        });
    }
    @Override public void createTag(String name) {
        run(() -> {
            Tag tag = new Tag(UUID.randomUUID().toString(), name);
            if (repository.getTags().stream().anyMatch(t -> t.getName().equalsIgnoreCase(tag.getName()))) {
                throw new IllegalArgumentException("A tag with this name already exists.");
            }
            repository.saveTag(tag);
            publish("Tag created. Select it to add it to your outfit.", false);
        });
    }
    @Override public void deleteOutfit(String outfitId) {
        run(() -> {
            if (repository.getOutfits().stream().noneMatch(o -> o.getId().equals(outfitId))) {
                throw new IllegalArgumentException("This outfit no longer exists.");
            }
            repository.deleteOutfit(outfitId);
            publish("Outfit deleted.", false);
        });
    }
    private void publish(String message, boolean created) {
        List<OutfitOutputData.ClothingData> clothing = repository.getClothing().stream()
                .map(c -> new OutfitOutputData.ClothingData(c.getId(), c.getName(),
                        c.getType().name(), c.getImagePath())).collect(Collectors.toList());
        List<OutfitOutputData.TagData> tags = repository.getTags().stream()
                .map(t -> new OutfitOutputData.TagData(t.getId(), t.getName())).collect(Collectors.toList());
        List<OutfitOutputData.OutfitData> outfits = repository.getOutfits().stream()
                .filter(o -> selectedTagId == null || o.getTagIds().contains(selectedTagId))
                .map(o -> new OutfitOutputData.OutfitData(o.getId(), o.getName(),
                        o.getClothingIds(), o.getTagIds())).collect(Collectors.toList());
        output.present(new OutfitOutputData(clothing, tags, outfits, selectedTagId, message, created));
    }
    private void run(Runnable action) {
        try { action.run(); }
        catch (IllegalArgumentException ex) { output.presentError(ex.getMessage()); }
        catch (RuntimeException ex) { output.presentError("Could not complete the operation. Please retry."); }
    }
}
