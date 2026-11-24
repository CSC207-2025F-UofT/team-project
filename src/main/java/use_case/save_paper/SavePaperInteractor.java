package use_case.save_paper;

import entity.PaperLibrary;

public class SavePaperInteractor implements SavePaperInputBoundary {
    private final SavePaperDataAccessInterface dataAccess;
    private final SavePaperOutputBoundary outputBoundary;
    private final String userId;

    public SavePaperInteractor(SavePaperDataAccessInterface dataAccess,
                               SavePaperOutputBoundary outputBoundary,
                               String userId) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.userId = userId;
    }

    @Override
    public void execute(SavePaperInputData inputData) {
        String url = inputData.getUrl();
        if (url == null || url.isBlank()) {
            outputBoundary.prepareFailView("This paper has no link to save");
            return;
        }

        PaperLibrary library = dataAccess.loadLibrary(userId);
        boolean saved = library.savePaper(url);
        if (saved) {
            dataAccess.saveLibrary(userId, library);
            outputBoundary.prepareSuccessView();
        } else {
            outputBoundary.prepareFailView("This paper is already saved ");
        }

    }
}


