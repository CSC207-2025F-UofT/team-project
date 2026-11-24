package use_case.save_paper;

public interface SavePaperOutputBoundary {
    void prepareSuccessView();
    void prepareFailView(String errorMessage);
}
