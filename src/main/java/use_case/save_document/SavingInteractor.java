package use_case.save_document;

import entity.Document;
import entity.User;

public class SavingInteractor implements SavingInputBoundary {
    private final SavingDataAccessInterface documentDataAccessObject;
    private final SavingOutputBoundary userPresenter;

    public SavingInteractor(SavingDataAccessInterface documentDataAccessInterface,
                            SavingOutputBoundary savingOutputBoundary) {
        this.documentDataAccessObject = documentDataAccessInterface;
        this.userPresenter = savingOutputBoundary;
    }

    @Override
    public void execute(SavingInputData savingInputData) {
        Document document = savingInputData.getDocument();
        documentDataAccessObject.save(document);
        userPresenter.prepareSuccessView(new SavingOutputData(document));
    }
}
