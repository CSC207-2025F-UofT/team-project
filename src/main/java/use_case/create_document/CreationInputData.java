package use_case.create_document;

import entity.User;

public class CreationInputData {

    private final String documentName;
    private final User creator;

    public CreationInputData(String documentName, User creator) {
        this.documentName = documentName;
        this.creator = creator;
    }

    String getDocumentName() {
        return documentName;
    }

    User getCreator() {
        return creator;
    }
}
