package use_case.talk_to_npc;

import entity.DialogueNode;
import entity.DialogueOption;
import entity.GameState;
import entity.GameStatus;
import entity.NPC;
import entity.Player;
import use_case.GameStateDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * The rules for talking to a character.
 *
 * Starting a conversation only looks things up. Choosing a response is
 * the part that changes the game: the response's Outcome is handed to
 * GameState.apply(), which is the one place where stats change. That is
 * the pattern every later action in the game follows.
 */
public class TalkToNpcInteractor implements TalkToNpcInputBoundary {

    private final GameStateDataAccessInterface gameStateDataAccess;
    private final DialogueDataAccessInterface dialogueDataAccess;
    private final TalkToNpcOutputBoundary presenter;

    public TalkToNpcInteractor(GameStateDataAccessInterface gameStateDataAccess,
                               DialogueDataAccessInterface dialogueDataAccess,
                               TalkToNpcOutputBoundary presenter) {
        this.gameStateDataAccess = gameStateDataAccess;
        this.dialogueDataAccess = dialogueDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void start(StartTalkInputData inputData) {
        GameState gameState = gameStateDataAccess.getGameState();
        String problem = findProblemWithGame(gameState);
        if (problem != null) {
            presenter.prepareFailView(problem);
            return;
        }

        NPC npc = dialogueDataAccess.getNpc(inputData.getNpcId());
        if (npc == null) {
            presenter.prepareFailView("There is nobody here to talk to.");
            return;
        }

        DialogueNode node = npc.getStartNode();
        if (node == null) {
            presenter.prepareFailView(npc.getName() + " has nothing to say right now.");
            return;
        }

        // Split the options into two lists of plain Strings for the presenter.
        List<String> optionIds = new ArrayList<String>();
        List<String> optionTexts = new ArrayList<String>();
        List<DialogueOption> options = node.getOptions();
        for (int i = 0; i < options.size(); i++) {
            optionIds.add(options.get(i).getId());
            optionTexts.add(options.get(i).getText());
        }

        StartTalkOutputData outputData = new StartTalkOutputData(
                npc.getId(), npc.getName(), node.getId(), node.getText(),
                optionIds, optionTexts);
        presenter.prepareDialogueView(outputData);
    }

    @Override
    public void choose(ChooseOptionInputData inputData) {
        GameState gameState = gameStateDataAccess.getGameState();
        String problem = findProblemWithGame(gameState);
        if (problem != null) {
            presenter.prepareFailView(problem);
            return;
        }

        NPC npc = dialogueDataAccess.getNpc(inputData.getNpcId());
        if (npc == null) {
            presenter.prepareFailView("There is nobody here to talk to.");
            return;
        }

        DialogueNode node = npc.getNode(inputData.getNodeId());
        if (node == null) {
            presenter.prepareFailView(npc.getName() + " has lost track of the conversation.");
            return;
        }

        DialogueOption option = node.getOption(inputData.getOptionId());
        if (option == null) {
            presenter.prepareFailView("That is not something you can say here.");
            return;
        }

        // This is where the game actually changes.
        GameStatus status = gameState.apply(option.getOutcome());
        gameStateDataAccess.saveGameState(gameState);

        Player player = gameState.getPlayer();
        boolean gameOver = (status == GameStatus.LOST);
        ChooseOptionOutputData outputData = new ChooseOptionOutputData(
                option.getOutcome().getMessage(),
                player.getEnergy(),
                player.getSanity(),
                player.getAcademicSurvival(),
                player.getMoney(),
                gameOver);
        presenter.prepareResultView(outputData);
    }

    @Override
    public void leave() {
        presenter.prepareGameView();
    }

    /**
     * Checks that there is a game and that it is still being played.
     *
     * @return a message for the player if they cannot talk right now,
     *         or null if everything is fine
     */
    private String findProblemWithGame(GameState gameState) {
        if (gameState == null) {
            return "Start a new game before talking to anyone.";
        }
        if (gameState.getStatus() == GameStatus.LOST || gameState.getStatus() == GameStatus.WON) {
            return "This game is over. Start a new game to keep playing.";
        }
        return null;
    }
}
