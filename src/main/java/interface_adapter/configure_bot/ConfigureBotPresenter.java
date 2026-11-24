package interface_adapter.configure_bot;

import use_case.configure_bot.ConfigureBotOutputBoundary;
import use_case.configure_bot.ConfigureBotOutputData;

public class ConfigureBotPresenter implements ConfigureBotOutputBoundary{

    @Override
    public void prepareSuccessView(ConfigureBotOutputData outputData) {

    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
