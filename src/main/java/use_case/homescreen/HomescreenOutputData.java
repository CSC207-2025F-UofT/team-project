package use_case.homescreen;

public class HomescreenOutputData {
    private final String viewToNavigateTo;
    private final boolean success;

    public HomescreenOutputData(String viewToNavigateTo, boolean success) {
        this.viewToNavigateTo = viewToNavigateTo;
        this.success = success;
    }

    public String getViewToNavigateTo() {
        return viewToNavigateTo;
    }

    public boolean isSuccess() {
        return success;
    }
}