package use_case.save_paper;

import entity.PaperLibrary;

public interface SavePaperDataAccessInterface {
    PaperLibrary loadLibrary(String userId);

    void saveLibrary(String userId, PaperLibrary library);
}