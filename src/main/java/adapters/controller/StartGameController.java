package adapters.controller;

import usecase.startgame.StartGameInputBoundary;

public class StartGameController {
    private final StartGameInputBoundary input;

    public StartGameController(StartGameInputBoundary input) {
        this.input = input;
    }

    /// View 的 “Start Match” 按钮回调。
    public void onStartMatchClicked(String player1Name, String player2Name) {
        input.startGame(player1Name, player2Name);
    }
}