package interface_adapter.save_paper;

import use_case.save_paper.SavePaperOutputBoundary;

public class SavePaperPresenter implements SavePaperOutputBoundary {
    private final SavePaperViewModel viewModel;

    public SavePaperPresenter(SavePaperViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(){
        viewModel.setMessage("Saved");
    }

    @Override
    public void prepareFailView(String errorMessage){
        viewModel.setMessage(errorMessage);
    }
}
