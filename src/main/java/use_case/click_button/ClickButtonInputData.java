package use_case.click_button;

/**
 * Input data for the Click Button use case.
 */
public class ClickButtonInputData {

    private final int buttonNumber;

    public ClickButtonInputData(int buttonNumber) {
        this.buttonNumber = buttonNumber;
    }

    public int getButtonNumber() {
        return buttonNumber;
    }
}
