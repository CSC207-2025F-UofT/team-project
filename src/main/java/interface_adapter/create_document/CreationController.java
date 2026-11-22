package interface_adapter.create_document;

import entity.User;
import use_case.create_document.CreationInputBoundary;
import use_case.create_document.CreationInputData;

/**
 * Controller for the creating a document use case.
 */
public class CreationController {

    private final CreationInputBoundary creationUseCaseInteractor;

    public CreationController(CreationInputBoundary creationUseCaseInteractor) {
        this.creationUseCaseInteractor = creationUseCaseInteractor;
    }

    /**
     * Executes the creating a document use case.
     * @param name the name of the document
     * @param creator the document's creator
     */
    public void execute(String name, User creator) {
        final CreationInputData creationInputData = new CreationInputData(name, creator);

        creationUseCaseInteractor.execute(creationInputData);
    }
}