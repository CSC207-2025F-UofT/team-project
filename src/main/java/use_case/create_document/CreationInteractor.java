package use_case.create_document;

import entity.User;
import entity.Document;

public class CreationInteractor implements CreationInputBoundary {
    private final CreationDataAccessInterface documentDataAccessObject;
    private final CreationOutputBoundary userPresenter;

    public CreationInteractor(CreationDataAccessInterface documentDataAccessInterface,
                              CreationOutputBoundary creationOutputBoundary) {
        this.documentDataAccessObject = documentDataAccessInterface;
        this.userPresenter = creationOutputBoundary;
    }

    @Override
    public void execute(CreationInputData creationInputData) {
        if (documentDataAccessObject.existsByName(creationInputData.getDocumentName())) {
            userPresenter.prepareFailView("A document already exists with that name.");
        }
        else if ("".equals(creationInputData.getDocumentName())) {
            userPresenter.prepareFailView("Document name cannot be empty");
        }
        else {
            User creator = creationInputData.getCreator();
            final Document document = new Document(creationInputData.getDocumentName(), creator);
            documentDataAccessObject.save(document);
            creator.addDocumentCreated(document);

            final CreationOutputData outputData = new CreationOutputData(document);
            userPresenter.prepareSuccessView(outputData);
        }
    }
}
