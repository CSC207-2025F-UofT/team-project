package use_case.outfit;

import data_access.InMemoryOutfitRepository;
import entity.ClothingItem;
import entity.ClothingType;
import entity.Outfit;
import entity.Tag;
import java.util.List;

/** Dependency-free edit regression checks; run explicitly like OutfitInteractorChecks. */
public final class EditOutfitChecks {
    private static final class Output implements OutfitOutputBoundary {
        private OutfitOutputData data;
        private String error;
        public void present(OutfitOutputData data) { this.data = data; error = null; }
        public void presentError(String message) { error = message; }
    }

    public static void main(String[] args) {
        InMemoryOutfitRepository repository = new InMemoryOutfitRepository(List.of(
                new ClothingItem("shirt", "Shirt", ClothingType.TOP, "", List.of()),
                new ClothingItem("sweater", "Sweater", ClothingType.TOP, "", List.of())));
        repository.saveTag(new Tag("school", "School"));
        repository.saveTag(new Tag("summer", "Summer"));
        repository.saveOutfit(new Outfit("outfit", "Original", List.of("shirt"), List.of("school")));
        repository.saveOutfit(new Outfit("other", "Other", List.of("shirt"), List.of()));
        Output output = new Output();
        OutfitInteractor interactor = new OutfitInteractor(repository, output);

        interactor.load("school");
        interactor.editOutfit("outfit", "  Updated  ", List.of("sweater", "sweater"), List.of("summer", "summer"));
        Outfit updated = repository.getOutfits().get(0);
        check(output.error == null, "Valid edit succeeds");
        check(updated.getId().equals("outfit") && repository.getOutfits().size() == 2,
                "Edit replaces the same outfit instead of creating another");
        check(updated.getName().equals("Updated"), "Edited name is trimmed");
        check(updated.getClothingIds().equals(List.of("sweater")), "Clothing replaced and deduplicated");
        check(updated.getTagIds().equals(List.of("summer")), "Tags replaced and deduplicated");
        check(repository.getOutfits().get(1).getName().equals("Other"), "Other outfit unchanged");
        check(output.data.updatedOutfitId.equals("outfit") && !output.data.outfitCreated,
                "Edit success identifies the saved outfit independently of create success");
        check(output.data.selectedTagId.equals("school") && output.data.outfits.isEmpty(),
                "Removing current filter tag removes outfit from filtered results");

        interactor.load("summer");
        check(output.data.updatedOutfitId == null && output.data.outfits.size() == 1,
                "Reload clears edit completion and displays new tag membership");
        interactor.editOutfit("outfit", "", List.of("sweater"), List.of("school"));
        check(output.error != null && repository.getOutfits().get(0) == updated,
                "Blank name rejected without changing stored outfit");
        interactor.editOutfit("outfit", null, List.of("sweater"), List.of());
        check(output.error != null && repository.getOutfits().get(0) == updated,
                "Null name rejected without mutation");
        interactor.editOutfit("outfit", "Invalid tag", List.of("sweater"), List.of("missing"));
        check(output.error != null && repository.getOutfits().get(0) == updated,
                "Unknown tag rejected without mutation");
        interactor.editOutfit("outfit", "No clothes", List.of(), List.of());
        check(output.error != null && repository.getOutfits().get(0) == updated,
                "Removing every clothing item is rejected without mutation");
        interactor.editOutfit("outfit", "Unknown clothes", List.of("missing"), List.of());
        check(output.error != null && repository.getOutfits().get(0) == updated,
                "Unknown clothing rejected without mutation");
        interactor.editOutfit("missing", "Missing", List.of("sweater"), List.of());
        check(output.error != null && repository.getOutfits().size() == 2,
                "Deleted or unknown outfit is not recreated");

        interactor.editOutfit("outfit", "No tags", List.of("sweater"), List.of());
        check(output.error == null && repository.getOutfits().get(0).getTagIds().isEmpty(),
                "Removing every tag is allowed");
        interactor.load(null);
        check(output.data.outfits.size() == 2, "All outfits still includes untagged edit");

        interactor.editOutfit("outfit", "Layered", List.of("shirt", "sweater"), List.of("school"));
        check(output.error == null && repository.getOutfits().get(0).getClothingIds()
                        .equals(List.of("shirt", "sweater")), "Multiple clothing selections can be saved");
        check(repository.getClothing().size() == 2, "Editing does not delete wardrobe items");
        Outfit beforeFailure = repository.getOutfits().get(0);
        OutfitRepository failingStorage = new OutfitRepository() {
            public List<ClothingItem> getClothing() { return repository.getClothing(); }
            public List<Outfit> getOutfits() { return repository.getOutfits(); }
            public List<Tag> getTags() { return repository.getTags(); }
            public void saveOutfit(Outfit outfit) { throw new IllegalStateException("offline"); }
            public void saveTag(Tag tag) { throw new UnsupportedOperationException(); }
            public void deleteOutfit(String id) { throw new UnsupportedOperationException(); }
        };
        Output failed = new Output();
        new OutfitInteractor(failingStorage, failed).editOutfit("outfit", "Failed", List.of("sweater"), List.of());
        check(failed.error != null && failed.data == null
                        && repository.getOutfits().get(0) == beforeFailure,
                "Write failure reports error and leaves existing outfit intact");
        System.out.println("PASS: Outfit edit regression checks.");
    }

    private static void check(boolean condition, String description) {
        if (!condition) throw new AssertionError(description);
    }
}
