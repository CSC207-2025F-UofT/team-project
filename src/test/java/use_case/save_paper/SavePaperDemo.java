package use_case.save_paper;

import data_access.InMemoryPaperLibraryDataAccess;
import interface_adapter.save_paper.SavePaperController;
import interface_adapter.save_paper.SavePaperPresenter;
import interface_adapter.save_paper.SavePaperViewModel;

public class SavePaperDemo {
    public static void main(String[] args) {
        var dao = new InMemoryPaperLibraryDataAccess();
        var viewModel = new SavePaperViewModel();
        var presenter = new SavePaperPresenter(viewModel);
        var interactor = new SavePaperInteractor(dao, presenter, "test-user");
        var controller = new SavePaperController(interactor);

        String url = "https://example.com/paper";

        controller.execute(url);
        System.out.println(viewModel.getMessage());

        controller.execute(url);
        System.out.println(viewModel.getMessage());
    }
}
