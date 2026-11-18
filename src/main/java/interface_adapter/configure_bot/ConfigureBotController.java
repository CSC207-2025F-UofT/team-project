package interface_adapter.configure_bot;

import use_case.configure_bot.ConfigureBotInputBoundary;
import use_case.configure_bot.ConfigureBotInputData;

public class ConfigureBotController {

    private final ConfigureBotInputBoundary ConfigureBotInteractor;

    public ConfigureBotController(ConfigureBotInputBoundary ConfigureBotInteractor) {
        this.ConfigureBotInteractor = ConfigureBotInteractor;
    }

    /**
     * Executes the Configure Bot Use Case.
     *
     */
    public void execute() {
        //TODO: implement this after implementing ConfigureBotInteractor
    }
}
