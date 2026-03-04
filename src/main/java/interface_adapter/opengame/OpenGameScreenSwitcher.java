package interface_adapter.opengame;
import interface_adapter.ViewManagerModel;
import use_case.openGame.ScreenSwitchBoundary;


public class OpenGameScreenSwitcher implements ScreenSwitchBoundary {
    private final ViewManagerModel viewManager;

    public OpenGameScreenSwitcher(ViewManagerModel viewManager) {
        this.viewManager = viewManager;
    }

    @Override
    public void switchToMoveScreen() {
        viewManager.setState("move"); // MUST match MoveView.getViewName()
        viewManager.firePropertyChange();
    }

    @Override
    public void switchToResultScreen() {
        viewManager.setState("Results");
        viewManager.firePropertyChange();
    }

}
