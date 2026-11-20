package use_case.save_document;

import entity.Document;

public class SavingOutputData {

    private final Document document;

    public SavingOutputData(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }
}
