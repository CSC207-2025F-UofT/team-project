package use_case.save_document;

import entity.Document;

public interface SavingDataAccessInterface {

    /**
     * Checks if the given document name exists.
     * @param documentName the document name to look for
     * @return true if a document with the given name exists; false otherwise
     */
    boolean existsByName(String documentName);

    /**
     * Saves the document.
     * @param document the document to save
     */
    void save(Document document);
}
