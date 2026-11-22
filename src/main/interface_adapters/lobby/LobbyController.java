package main.interface_adapters.lobby;

public class LobbyController {

    private final LobbyPresenter lobbyPresenter;

    public LobbyController(LobbyPresenter lobbyPresenter) {
        this.lobbyPresenter = lobbyPresenter;
    }

    public void switchToMainMenuView() {
        lobbyPresenter.switchToMainMenuView();
    }
 }
