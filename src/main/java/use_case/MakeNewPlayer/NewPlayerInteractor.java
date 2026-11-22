package use_case.MakeNewPlayer;

import entity.Player;
import entity.PlayerFactory;

public class NewPlayerInteractor implements NewPlayerInputBoundary{
    private final NewPlayerDataAccessInterface playerDataAccessObject;
    private final NewPlayerOutputBoundary playerPresenter;
    private final PlayerFactory playerFactory;

    public NewPlayerInteractor(NewPlayerDataAccessInterface newPlayerDataAccessInterface,
                            NewPlayerOutputBoundary newPlayerOutputBoundary,
                            PlayerFactory playerFactory) {
        this.playerDataAccessObject = newPlayerDataAccessInterface;
        this.playerPresenter = newPlayerOutputBoundary;
        this.playerFactory = playerFactory;
    }

    @Override
    public void execute(NewPlayerInputData newPlayerInputData) {
        if (playerDataAccessObject.existsByName(newPlayerInputData.getUsername())) {
            playerPresenter.prepareFailView("User already exists.");
        }
        else if ("".equals(newPlayerInputData.getUsername())) {
            playerPresenter.prepareFailView("Username cannot be empty");
        }
        else {
            final Player player = playerFactory.create(newPlayerInputData.getUsername(),
            playerDataAccessObject.save(player);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName());
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }
}
