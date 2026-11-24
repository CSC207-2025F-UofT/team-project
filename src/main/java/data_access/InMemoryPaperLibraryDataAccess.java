package data_access;

import entity.PaperLibrary;
import use_case.save_paper.SavePaperDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class InMemoryPaperLibraryDataAccess implements SavePaperDataAccessInterface {
    private final Map<String, PaperLibrary> libraries = new HashMap<>();

    @Override
    public PaperLibrary loadLibrary(String userId) {
        PaperLibrary library = libraries.get(userId);
        if (library == null) {
            return new PaperLibrary();
        }
        return library;
    }

    @Override
    public void saveLibrary(String userId, PaperLibrary library) {
        libraries.put(userId, library);
    }
}