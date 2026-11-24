package interface_adapter.save_paper;

import use_case.save_paper.SavePaperInputBoundary;
import use_case.save_paper.SavePaperInputData;

public class SavePaperController {
    private final SavePaperInputBoundary savePaperUseCaseInteractor;

    public SavePaperController(SavePaperInputBoundary savePaperUseCaseInteractor){
        this.savePaperUseCaseInteractor = savePaperUseCaseInteractor;
    }

    public void execute(String url){
        final SavePaperInputData inputData = new SavePaperInputData(url);
        savePaperUseCaseInteractor.execute(inputData);
    }
}
