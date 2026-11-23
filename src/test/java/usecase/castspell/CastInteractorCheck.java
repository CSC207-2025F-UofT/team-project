package usecase.castspell;

import entity.PendingCast;
import entity.Player;
import entity.board.Board;
import entity.board.Direction;
import entity.board.Tile;
import entity.card.CharacterCard;
import entity.card.SpellCard;
import entity.card.SpellCardFactory;
import adapters.controller.CastController;
import yuhangusecase.castspell.CastInputData;
import yuhangusecase.castspell.CastInteractor;
import yuhangusecase.castspell.CastOutputData;

import java.util.ArrayList;

public class CastInteractorCheck {
    public static void main(String[] args) {
        // Prepare an initialized board.
        Tile[][] tiles = new Tile[6][6];

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                tiles[x][y] = new Tile();
                tiles[x][y].setSpells(new ArrayList<>());
            }
        }

        Board board = new Board(tiles);

        // Place a character and equip a spell.
        Player player = new Player("Yuhang");
        CharacterCard character =
                new CharacterCard(player, "Aki", 200, 3);
        SpellCard spell =
                new SpellCardFactory().create(player, "Fireball");

        board.playCard(2, 3, character);
        board.playCard(2, 3, spell);

        // Place an opponent's character.
        Player opponent = new Player("Opponent");
        CharacterCard target =
                new CharacterCard(opponent, "Kia", 200, 3);

        board.playCard(3, 3, target);

        // Connect the interactor to the test implementations.
        InMemoryCastGameDataAccess dataAccess =
                new InMemoryCastGameDataAccess(board, player);
        RecordingCastOutput output = new RecordingCastOutput();

        CastInteractor interactor =
                new CastInteractor(dataAccess, output);

        // Reject a request from a player whose turn has not started.
        checkRejected(
                board,
                player,
                new CastInputData("Opponent", 2, 3, 0, "RIGHT"),
                "It is not your turn."
        );

        // Reject selecting an opponent's character.
        checkRejected(
                board,
                player,
                new CastInputData("Yuhang", 3, 3, 0, "RIGHT"),
                "The selected character does not belong to you."
        );

        // Reject an invalid direction.
        checkRejected(
                board,
                player,
                new CastInputData("Yuhang", 2, 3, 0, "INVALID"),
                "The selected casting direction is invalid."
        );

        // Submit a valid cast request through the controller.
        CastController controller = new CastController(interactor);

        controller.execute("Yuhang", 2, 3, 0, "RIGHT");

        // Verify the saved cast.
        PendingCast pending = dataAccess.getPendingCast();

        if (pending == null
                || pending.getPlayer() != player
                || pending.getCharacter() != character
                || pending.getSpell() != spell
                || pending.getX() != 2
                || pending.getY() != 3
                || pending.getDirection() != Direction.RIGHT) {
            throw new AssertionError(
                    "The expected pending cast was not saved."
            );
        }

        // Verify the successful output.
        CastOutputData result = output.getOutputData();

        if (output.getErrorMessage() != null
                || result == null
                || !"Yuhang".equals(result.getPlayerName())
                || !"Fireball".equals(result.getSpellName())
                || !"RIGHT".equals(result.getDirection())
                || result.getX() != 2
                || result.getY() != 3) {
            throw new AssertionError(
                    "The expected success output was not produced."
            );
        }

        // Reject a second cast while the first one is still pending.
        interactor.execute(
                new CastInputData("Yuhang", 2, 3, 0, "LEFT")
        );

        if (!"Another spell is awaiting resolution."
                .equals(output.getErrorMessage())
                || output.getOutputData() != null
                || dataAccess.getPendingCast() != pending) {
            throw new AssertionError(
                    "A repeated cast must be rejected without replacing the original."
            );
        }

        // Damage must wait until cast resolution.
        if (character.getHP() != 200 || target.getHP() != 200) {
            throw new AssertionError(
                    "Casting should not apply damage before resolution."
            );
        }

        System.out.println("Cast interactor checks passed.");
    }
    private static void checkRejected(Board board, Player player,
                                      CastInputData input,
                                      String expectedError) {
        InMemoryCastGameDataAccess dataAccess =
                new InMemoryCastGameDataAccess(board, player);
        RecordingCastOutput output = new RecordingCastOutput();

        CastInteractor interactor =
                new CastInteractor(dataAccess, output);

        interactor.execute(input);

        if (!expectedError.equals(output.getErrorMessage())
                || output.getOutputData() != null
                || dataAccess.getPendingCast() != null) {
            throw new AssertionError(
                    "Expected rejection without saving a cast: "
                            + expectedError
            );
        }
    }
}
