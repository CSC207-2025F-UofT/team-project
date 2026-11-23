package use_case.view_closet;

/**
 * TODO: RIE
 */
public class ViewClosetInputData {
    private final ViewClosetFilter filter;
    public ViewClosetInputData(ViewClosetFilter filter) {
        this.filter = filter;
    }

    public ViewClosetFilter getFilter() {
        return filter;
    }
}