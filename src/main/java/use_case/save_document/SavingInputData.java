package use_case.save_document;

import entity.Document;
import entity.User;

public class SavingInputData {

    private final Document document;

    public SavingInputData(Document document) {
        this.document = document;
    }

    Document getDocument() {
        return document;
    }
}
