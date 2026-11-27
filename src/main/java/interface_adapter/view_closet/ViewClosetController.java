package interface_adapter.view_closet;

import use_case.view_closet.ViewClosetInputBoundary;
import use_case.view_closet.ViewClosetInputData;
import use_case.view_closet.ViewClosetFilter;

public class ViewClosetController {
    private final ViewClosetInputBoundary interactor;

    public ViewClosetController(ViewClosetInputBoundary interactor) {
        this.interactor = interactor;
    }
    public void execute(ViewClosetFilter filter) {
        interactor.execute(new ViewClosetInputData(filter));
    }
}
