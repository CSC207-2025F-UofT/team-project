package main.interface_adapters.lobby;

public class LobbyController {

    private final LobbyPresenter lobbyPresenter;

    public LobbyController(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
    }

    public void switchToMainMenuView() {
        lobbyPresenter.switchToMainMenuView();
    }

    public void switchToGameView(int num_players) {
        lobbyPresenter.switchToGameView(num_players);
    }

    public void addPlayer() {lobbyPresenter.addPlayer();}

    public void removePlayer() {lobbyPresenter.removePlayer();}

    public void increaseMinBet() {lobbyPresenter.increaseMinBet();}

    public void decreaseMinBet() {lobbyPresenter.decreaseMinBet();}
 }
