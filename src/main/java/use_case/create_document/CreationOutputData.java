package use_case.create_document;

import entity.Document;

public class CreationOutputData {

    private final Document document;

    public CreationOutputData(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
