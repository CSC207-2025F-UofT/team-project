package use_case.save_result;

import entity.QuizResult;

import java.time.LocalDate;

public class SaveResultInteractor implements SaveResultInputBoundary { 
    private final SaveResultOutputBoundary presenter;
    private final SaveResultDataAccessInterface dao;

    public SaveResultInteractor(SaveResultOutputBoundary presenter, SaveResultDataAccessInterface dao) { 
        this.presenter = presenter;
        this.dao = dao;
    }

    @Override
    public void execute(SaveResultInputData input) {

        // Invalid name like "" or "   "
        String name = input.getUsername();
        if (name == null || name.isBlank()) {
            presenter.prepareFailView("Please enter a name.");
            return;
        }
        
        // Build QuizResult result
        QuizResult result = new QuizResult(
            name.trim(),
            LocalDate.now(),
            input.getChosenIndices(),
            input.getMatchedLibrary()
        );

        // Save (name, result) -> DB
        dao.save(result);

        // Thank you page
        presenter.prepareSuccessView(new SaveResultOutputData(name.trim(), input.getMatchedLibrary()));
    }
}



