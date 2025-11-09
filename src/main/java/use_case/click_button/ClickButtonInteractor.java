package use_case.click_button;

/**
 * The Click Button Interactor.
 */
public class ClickButtonInteractor implements ClickButtonInputBoundary {
    private final ClickButtonOutputBoundary presenter;

    public ClickButtonInteractor(ClickButtonOutputBoundary clickButtonOutputBoundary) {
        this.presenter = clickButtonOutputBoundary;
    }

    @Override
    public void execute(ClickButtonInputData clickButtonInputData) {
        final int buttonNumber = clickButtonInputData.getButtonNumber();
        final String message = "button " + buttonNumber + " clicked";
        final ClickButtonOutputData outputData = new ClickButtonOutputData(message);
        presenter.prepareView(outputData);
    }
}
