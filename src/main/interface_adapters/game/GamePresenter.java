package main.interface_adapters.game;

import main.interface_adapters.ViewManagerModel;
import main.interface_adapters.settings.SettingsViewModel;

import javax.swing.Timer;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GamePresenter {

    private final GameViewModel gameViewModel;
    private final SettingsViewModel settingsViewModel;
    private final ViewManagerModel viewManagerModel;

    // Temporary mock game-loop state (replace with Use Case Interactor later)
    private int currentTurnSeat;
    private int actionsThisStreet;
    private int streetIndex; // 0 = preflop, 1 = flop, 2 = turn, 3 = river
    private boolean betOpen;

    public GamePresenter(GameViewModel gameViewModel, SettingsViewModel settingsViewModel,
                         ViewManagerModel viewManagerModel) {
        this.gameViewModel = gameViewModel;
        this.settingsViewModel = settingsViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void gameStarted() {
        Random random = new Random();
        int numPlayers = gameViewModel.getPlayers();
        int dealer = random.nextInt(numPlayers);
        gameViewModel.setDealer(dealer);

        // --- SWAP THIS SECTION WITH USE CASE INTERACTOR OUTPUT LATER ---
        List<String> myCards = List.of("/cards/card_spades_14.png", "/cards/card_hearts_14.png");
        List<List<String>> oppCards = List.of(
                List.of("/cards/card_clubs_02.png", "/cards/card_diamonds_07.png"),
                List.of("/cards/card_hearts_11.png", "/cards/card_spades_11.png"),
                List.of("/cards/card_diamonds_04.png", "/cards/card_clubs_09.png"),
                List.of("/cards/card_spades_13.png", "/cards/card_hearts_12.png"),
                List.of("/cards/card_clubs_10.png", "/cards/card_diamonds_10.png")
        );
        List<String> boardCards = List.of(
                "/cards/card_clubs_04.png",
                "/cards/card_diamonds_12.png",
                "/cards/card_hearts_13.png",
                "/cards/card_spades_09.png",
                "/cards/card_diamonds_10.png"
        );
        // ---------------------------------------------------------------

        prepareNewHand(myCards, oppCards, boardCards);

        // Start pre-flop action (seat after Big Blind)
        streetIndex = 0;
        actionsThisStreet = 0;
        betOpen = false;
        currentTurnSeat = (numPlayers == 2) ? dealer : (dealer + 3) % numPlayers;
        advanceTurn();
    }

    public void prepareNewHand(List<String> playerHoleCards,
                               List<List<String>> opponentHoleCards,
                               List<String> communityCards) {
        gameViewModel.setPlayerCards(playerHoleCards.get(0), playerHoleCards.get(1), "revealed");

        int numOpponents = gameViewModel.getPlayers() - 1;
        for (int i = 0; i < numOpponents; i++) {
            List<String> pair = opponentHoleCards.get(i);
            gameViewModel.setOpponentCards(i, pair.get(0), pair.get(1), "hidden");
        }

        gameViewModel.setCommunityCardsHidden(communityCards);
        gameViewModel.beginDealing();
    }

    public void endPlayerTurn() {
        gameViewModel.playerActionSubmitted();
    }

    // --- Player Actions (Swap bodies to call Use Case Interactor later) ---
    public void playerCheck() {
        endPlayerTurn();
        onActionCompleted(false);
    }

    public void playerCall() {
        endPlayerTurn();
        onActionCompleted(false);
    }

    public void playerRaise(int amount) {
        endPlayerTurn();
        // Raising re-opens action so every bot around the table gets to call
        betOpen = true;
        actionsThisStreet = 0;
        onActionCompleted(true);
    }

    public void playerReraise(int amount) {
        playerRaise(amount);
    }

    public void playerFold() {
        endPlayerTurn();
        gameViewModel.setPlayerCardStatus("folded");
    }

    // --- Temporary Bot Turn Simulation (Always Checks or Calls) ---
    private void onActionCompleted(boolean justRaised) {
        int totalPlayers = gameViewModel.getPlayers();
        actionsThisStreet++;

        if (actionsThisStreet >= totalPlayers) {
            advanceStreet();
            return;
        }

        currentTurnSeat = (currentTurnSeat + 1) % totalPlayers;
        advanceTurn();
    }


    private void advanceTurn() {
        if (currentTurnSeat == 0) {
            promptPlayerTurn(betOpen);
            return;
        }

        final int actingSeat = currentTurnSeat;
        Timer botTimer = new Timer(600, e -> {
            if (betOpen) {
                int oppIdx = actingSeat - 1;
                gameViewModel.deductOpponentChips(oppIdx, gameViewModel.getCurrentBetToMatch());
            }
            onActionCompleted(false);
        });
        botTimer.setRepeats(false);
        botTimer.start();
    }

    private void advanceStreet() {
        streetIndex++;
        actionsThisStreet = 0;
        betOpen = false;

        int totalPlayers = gameViewModel.getPlayers();
        int dealer = gameViewModel.getDealer();
        currentTurnSeat = (dealer + 1) % totalPlayers;

        List<String> board = gameViewModel.getCommunityCards();
        if (streetIndex == 1) {
            gameViewModel.setFlop(board.get(0), board.get(1), board.get(2));
        } else if (streetIndex == 2) {
            gameViewModel.setTurn(board.get(3));
        } else if (streetIndex == 3) {
            gameViewModel.setRiver(board.get(4));
        } else {
            // Showdown: reveal all opponents' cards
            for (int i = 0; i < totalPlayers - 1; i++) {
                gameViewModel.setOpponentCardStatus(i, "revealed");
            }
            return;
        }

        advanceTurn();
    }

    public void promptPlayerTurn(boolean facingBet) {
        int minBet = facingBet
                ? Math.max(gameViewModel.getCurrentBetToMatch() * 2, gameViewModel.getChipValues().get(0))
                : gameViewModel.getChipValues().get(0);
        gameViewModel.setCurrentBetToMatch(minBet);
        gameViewModel.setTurnState(facingBet ? "facingBet" : "unopened");
    }

    public void playerRaise(Map<Integer, Integer> betChips, int totalAmount) {
        endPlayerTurn();
        gameViewModel.deductPlayerChips(betChips, totalAmount);
        gameViewModel.setCurrentBetToMatch(totalAmount);

        betOpen = true;
        actionsThisStreet = 0;
        onActionCompleted(true);
    }

    public void playerReraise(Map<Integer, Integer> betChips, int totalAmount) {
        playerRaise(betChips, totalAmount);
    }
}