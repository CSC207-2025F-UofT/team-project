package use_case.outfit;

public interface OutfitOutputBoundary {
    void present(OutfitOutputData data);
    void presentError(String message);
}
