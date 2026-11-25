package usecase.savegame;

public interface SaveGameOutputBoundary {
    void presentSaveSuccess(String slotName);
    void presentSaveFailure(String slotName, String reason);
}