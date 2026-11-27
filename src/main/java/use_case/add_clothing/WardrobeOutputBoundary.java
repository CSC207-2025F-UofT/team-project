package use_case.add_clothing;

public interface WardrobeOutputBoundary {
    void present(WardrobeOutputData data);
    void presentError(String message);
}
