package use_case.enter_name.makeNewPlayer;

import entity.Player;
import entity.PlayerFactory;
import use_case.enter_name.welcomeMenu.ReturningPlayerDataAccessInterface;
import use_case.shared.CurrentPlayerDataAccessInterface;

public class NewPlayerInteractor implements NewPlayerInputBoundary{
    private final NewPlayerDataAccessInterface playerDataAccessObject;
    private final NewPlayerOutputBoundary playerPresenter;
    private final PlayerFactory playerFactory;
    private final ReturningPlayerDataAccessInterface returningPlayerDataAccessObject;
    private final CurrentPlayerDataAccessInterface currentPlayer;


    public NewPlayerInteractor(NewPlayerDataAccessInterface newPlayerDataAccessInterface,
                            NewPlayerOutputBoundary newPlayerOutputBoundary,
                            PlayerFactory playerFactory, ReturningPlayerDataAccessInterface returningPlayerDataAccessObject,
                            CurrentPlayerDataAccessInterface currentPlayer) {
        this.playerDataAccessObject = newPlayerDataAccessInterface;
        this.playerPresenter = newPlayerOutputBoundary;
        this.playerFactory = playerFactory;
        this.returningPlayerDataAccessObject = returningPlayerDataAccessObject;
        this.currentPlayer = currentPlayer;
    }

    @Override
    public void execute(NewPlayerInputData newPlayerInputData) {
        if ("".equals(newPlayerInputData.getUsername())) {
            playerPresenter.prepareFailView("Username cannot be empty");
        }
        else if (playerDataAccessObject.existsByName(newPlayerInputData.getUsername())) {
            final Player returningPlayer = returningPlayerDataAccessObject.get(newPlayerInputData.getUsername());
            currentPlayer.setCurrentPlayer(returningPlayer);
            final NewPlayerOutputData newPlayerOutputData = new NewPlayerOutputData(returningPlayer.getName());
            playerPresenter.prepareSuccessView(newPlayerOutputData);
        }
        else {
            final Player player = playerFactory.create(newPlayerInputData.getUsername());
            playerDataAccessObject.save(player);
            currentPlayer.setCurrentPlayer(player);

            final NewPlayerOutputData newPlayerOutputData = new NewPlayerOutputData(player.getName());
            playerPresenter.prepareSuccessView(newPlayerOutputData);
        }
    }

    @Override
    public void switchToWelcomeView() {
        playerPresenter.switchToWelcomeView();
    }
}
