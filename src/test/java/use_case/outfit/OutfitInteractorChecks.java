package use_case.outfit;

import data_access.InMemoryOutfitRepository;
import entity.*;
import java.util.*;

/** Dependency-free regression checks. Throws AssertionError on any failure. */
public final class OutfitInteractorChecks {
    private static final class Output implements OutfitOutputBoundary {
        OutfitOutputData data;
        String error;
        public void present(OutfitOutputData data) { this.data = data; error = null; }
        public void presentError(String error) { this.error = error; }
    }
    private static final class FailingRepository implements OutfitRepository {
        public List<ClothingItem> getClothing() { return List.of(
                new ClothingItem("c1", "Shirt", ClothingType.TOP, "", List.of())); }
        public List<Outfit> getOutfits() { return List.of(); }
        public List<Tag> getTags() { return List.of(); }
        public void saveOutfit(Outfit outfit) { throw new IllegalStateException("storage offline"); }
        public void saveTag(Tag tag) { throw new IllegalStateException("storage offline"); }
        public void deleteOutfit(String id) { throw new IllegalStateException("storage offline"); }
    }
    public static void main(String[] args) {
        ClothingItem item = new ClothingItem("c1", "Shirt", ClothingType.TOP, "", List.of("white"));
        InMemoryOutfitRepository repo = new InMemoryOutfitRepository(List.of(item));
        Output output = new Output();
        OutfitInteractor useCase = new OutfitInteractor(repo, output);

        useCase.load(null);
        check(output.data.outfits.isEmpty(), "Initially empty wardrobe");
        useCase.createTag("  School  ");
        String school = repo.getTags().get(0).getId();
        check(repo.getTags().get(0).getName().equals("School"), "Tag name trimmed");
        useCase.createTag("school");
        check(output.error != null && repo.getTags().size() == 1, "Duplicate tag rejected");
        useCase.createTag("  ");
        check(output.error != null && repo.getTags().size() == 1, "Blank tag rejected");

        useCase.createOutfit(" ", List.of("c1"), List.of());
        check(output.error != null && repo.getOutfits().isEmpty(), "Blank outfit name rejected");
        useCase.createOutfit("Empty", List.of(), List.of());
        check(output.error != null && repo.getOutfits().isEmpty(), "Empty outfit rejected");
        useCase.createOutfit("Unknown clothing", List.of("missing"), List.of());
        check(output.error != null && repo.getOutfits().isEmpty(), "Unknown clothing rejected");
        useCase.createOutfit("Unknown tag", List.of("c1"), List.of("missing"));
        check(output.error != null && repo.getOutfits().isEmpty(), "Unknown tag rejected");

        useCase.createOutfit("  Class day  ", List.of("c1", "c1"), List.of(school, school));
        Outfit saved = repo.getOutfits().get(0);
        check(saved.getName().equals("Class day"), "Outfit name trimmed");
        check(saved.getClothingIds().size() == 1 && saved.getTagIds().size() == 1,
                "Duplicate selections removed");
        check(output.data.outfitCreated, "Successful creation reported");
        useCase.createOutfit("Weekend", List.of("c1"), List.of());
        useCase.load(school);
        check(output.data.outfits.size() == 1, "Tag filter selects only matching outfit");
        useCase.createTag("Summer");
        String summer = repo.getTags().get(1).getId();
        useCase.load(summer);
        check(output.data.outfits.isEmpty(), "Unused tag produces empty result");
        useCase.load(null);
        check(output.data.outfits.size() == 2, "All outfits resets filter");
        useCase.load("missing");
        check(output.error != null, "Unknown filter rejected");
        useCase.deleteOutfit(saved.getId());
        check(repo.getOutfits().size() == 1 && repo.getClothing().size() == 1,
                "Delete preserves wardrobe");
        useCase.deleteOutfit("missing");
        check(output.error != null, "Unknown outfit deletion rejected");
        try {
            saved.getClothingIds().add("bad");
            throw new AssertionError("Entity list should be immutable");
        } catch (UnsupportedOperationException expected) { }

        Output failed = new Output();
        new OutfitInteractor(new FailingRepository(), failed)
                .createOutfit("School", List.of("c1"), List.of());
        check(failed.error != null && failed.data == null, "Storage failure does not report success");
        System.out.println("PASS: 20 outfit and tag regression checks.");
    }
    private static void check(boolean condition, String description) {
        if (!condition) throw new AssertionError(description);
    }
}
