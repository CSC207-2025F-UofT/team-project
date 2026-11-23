package use_case.view_closet;

/**
 * TODO: RIE
 */
public class ViewClosetInteractor implements ViewClosetInputBoundary {
    private final ViewClosetDataAccessInterface wardrobe;
    private final ViewClosetOutputBoundary presenter;

    public ViewClosetInteractor(ViewClosetDataAccessInterface wardrobe,
                                ViewClosetOutputBoundary presenter) {
        this.wardrobe = wardrobe;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewClosetInputData inputData) {
        ViewClosetOutputData outputData =
                new ViewClosetOutputData(wardrobe.getAllItems());
        presenter.prepareSuccessView(outputData);
    }
}