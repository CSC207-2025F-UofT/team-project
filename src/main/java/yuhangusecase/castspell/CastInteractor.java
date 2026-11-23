package yuhangusecase.castspell;

import entity.Player;
import entity.board.Board;
import entity.board.Tile;
import entity.card.CharacterCard;
import entity.board.Direction;
import entity.card.SpellCard;
import java.util.List;
import entity.PendingCast;

public class CastInteractor implements CastInputBoundary {
    private final CastGameDataAccess gameDataAccess;
    private final CastOutputBoundary outputBoundary;

    public CastInteractor(CastGameDataAccess gameDataAccess,
                          CastOutputBoundary outputBoundary) {
        this.gameDataAccess = gameDataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(CastInputData inputData) {
        if (inputData == null) {
            outputBoundary.prepareFailView("Cast request is missing.");
            return;
        }

        int x = inputData.getX();
        int y = inputData.getY();

        if (x < 0 || x >= 6 || y < 0 || y >= 6) {
            outputBoundary.prepareFailView(
                    "Character coordinates are outside the board."
            );
            return;
        }

        if (inputData.getSpellIndex() < 0) {
            outputBoundary.prepareFailView(
                    "Spell index cannot be negative."
            );
            return;
        }

        Player currentPlayer = gameDataAccess.getCurrentPlayer();

        if (currentPlayer == null) {
            outputBoundary.prepareFailView("No game is currently active.");
            return;
        }

        String playerName = inputData.getPlayerName();

        if (playerName == null || playerName.isBlank()
                || !playerName.equals(currentPlayer.getName())) {
            outputBoundary.prepareFailView("It is not your turn.");
            return;
        }

        Board board = gameDataAccess.getBoard();

        if (board == null) {
            outputBoundary.prepareFailView("The game board is unavailable.");
            return;
        }

        Tile tile = board.getTile(x, y);

        if (tile == null || tile.getCharacterCard() == null) {
            outputBoundary.prepareFailView(
                    "There is no character on the selected tile."
            );
            return;
        }

        CharacterCard character = tile.getCharacterCard();

        if (character.getOwner() != currentPlayer) {
            outputBoundary.prepareFailView(
                    "The selected character does not belong to you."
            );
            return;
        }

        if (character.getHP() <= 0) {
            outputBoundary.prepareFailView(
                    "A knocked-out character cannot cast a spell."
            );
            return;
        }

        List<SpellCard> spells = tile.getSpells();

        if (spells == null || spells.isEmpty()) {
            outputBoundary.prepareFailView(
                    "The selected character has no equipped spells."
            );
            return;
        }

        int spellIndex = inputData.getSpellIndex();

        if (spellIndex >= spells.size()) {
            outputBoundary.prepareFailView(
                    "The selected spell does not exist."
            );
            return;
        }

        SpellCard spell = spells.get(spellIndex);

        if (spell == null) {
            outputBoundary.prepareFailView(
                    "The selected spell is unavailable."
            );
            return;
        }

        String directionName = inputData.getDirection();

        if (directionName == null || directionName.isBlank()) {
            outputBoundary.prepareFailView(
                    "Please select a casting direction."
            );
            return;
        }

        Direction direction;

        try {
            direction = Direction.valueOf(directionName);
        } catch (IllegalArgumentException exception) {
            outputBoundary.prepareFailView(
                    "The selected casting direction is invalid."
            );
            return;
        }

        PendingCast pendingCast = new PendingCast(
                currentPlayer,
                character,
                x,
                y,
                spell,
                direction
        );

        if (!gameDataAccess.trySavePendingCast(pendingCast)) {
            outputBoundary.prepareFailView(
                    "Another spell is awaiting resolution."
            );
            return;
        }

        CastOutputData outputData = new CastOutputData(
                currentPlayer.getName(),
                x,
                y,
                spell.getName(),
                direction.name()
        );

        outputBoundary.prepareSuccessView(outputData);
    }
}
