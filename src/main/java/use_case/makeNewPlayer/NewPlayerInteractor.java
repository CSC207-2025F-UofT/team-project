package use_case.makeNewPlayer;

import entity.Player;
import entity.PlayerFactory;
import use_case.welcomeMenu.ReturningPlayerDataAccessInterface;

public class NewPlayerInteractor implements NewPlayerInputBoundary{
    private final NewPlayerDataAccessInterface playerDataAccessObject;
    private final NewPlayerOutputBoundary playerPresenter;
    private final PlayerFactory playerFactory;
    private final ReturningPlayerDataAccessInterface returningPlayerDataAccessObject;


    public NewPlayerInteractor(NewPlayerDataAccessInterface newPlayerDataAccessInterface,
                            NewPlayerOutputBoundary newPlayerOutputBoundary,
                            PlayerFactory playerFactory, ReturningPlayerDataAccessInterface returningPlayerDataAccessObject) {
        this.playerDataAccessObject = newPlayerDataAccessInterface;
        this.playerPresenter = newPlayerOutputBoundary;
        this.playerFactory = playerFactory;
        this.returningPlayerDataAccessObject = returningPlayerDataAccessObject;
    }

    @Override
    public void execute(NewPlayerInputData newPlayerInputData) {
        if ("".equals(newPlayerInputData.getUsername())) {
            playerPresenter.prepareFailView("Username cannot be empty");
        }
        else if (playerDataAccessObject.existsByName(newPlayerInputData.getUsername())) {
            final Player returningPlayer = returningPlayerDataAccessObject.get(newPlayerInputData.getUsername());
            final NewPlayerOutputData newPlayerOutputData = new NewPlayerOutputData(returningPlayer.getName());
            playerPresenter.prepareSuccessView(newPlayerOutputData);
        }
        else {
            final Player player = playerFactory.create(newPlayerInputData.getUsername());
            playerDataAccessObject.save(player);

            final NewPlayerOutputData newPlayerOutputData = new NewPlayerOutputData(player.getName());
            playerPresenter.prepareSuccessView(newPlayerOutputData);
        }
    }

    @Override
    public void switchToWelcomeView() {
        playerPresenter.switchToWelcomeView();
    }
}
