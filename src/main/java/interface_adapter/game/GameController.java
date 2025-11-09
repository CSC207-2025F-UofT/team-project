package interface_adapter.game;

import use_case.click_button.ClickButtonInputBoundary;
import use_case.click_button.ClickButtonInputData;

/**
 * Controller for the Game View.
 */
public class GameController {

    private final ClickButtonInputBoundary clickButtonInteractor;

    public GameController(ClickButtonInputBoundary clickButtonInteractor) {
        this.clickButtonInteractor = clickButtonInteractor;
    }

    /**
     * Executes the click button use case.
     * @param buttonNumber the button number that was clicked
     */
    public void clickButton(int buttonNumber) {
        final ClickButtonInputData inputData = new ClickButtonInputData(buttonNumber);
        clickButtonInteractor.execute(inputData);
    }
}
