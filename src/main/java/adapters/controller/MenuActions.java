package adapters.controller;

/** Controller entry points for menu actions that require work outside the view. */
public interface MenuActions {
    // TODO: Supply a production implementation and wire Rules and Quit into the application lifecycle.
    void showRules();
    void quit();

    // TODO: Wire host and join requests to lobby services and publish the code, roster and any errors.
    void hostGame(String nickname);
    void joinGame(String nickname, String code);

    // TODO: Open save-slot selection and restore the selected match before showing the game screen.
    void loadSave(String nickname);

    // TODO: Disconnect and ignore late callbacks from the abandoned lobby.
    /** Cancels a pending lobby request or leaves the current lobby. */
    void leaveLobby();

    // TODO: Verify host readiness and open the game for both players when the start is accepted.
    void startGame();
}
