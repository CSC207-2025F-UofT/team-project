package usecase.castspell;

import entity.PendingCast;
import entity.Player;
import entity.board.Board;
import entity.board.Direction;
import entity.card.CharacterCard;
import entity.card.SpellCardFactory;
import entity.card.SpellCard;

public class PendingCastStorageCheck {
    public static void main(String[] args) {
        Player player = new Player("Yuhang");
        Board board = new Board();

        CharacterCard character =
                new CharacterCard(player, "Aki", 200, 3);

        SpellCard spell =
                new SpellCardFactory().create(player, "Fireball");

        InMemoryCastGameDataAccess dataAccess =
                new InMemoryCastGameDataAccess(board, player);

        PendingCast firstCast = new PendingCast(
                player, character, 2, 3, spell, Direction.RIGHT
        );

        PendingCast secondCast = new PendingCast(
                player, character, 2, 3, spell, Direction.LEFT
        );

        boolean firstSaved = dataAccess.trySavePendingCast(firstCast);
        boolean secondSaved = dataAccess.trySavePendingCast(secondCast);
        boolean originalPreserved =
                dataAccess.getPendingCast() == firstCast;

        if (!firstSaved || secondSaved || !originalPreserved) {
            throw new AssertionError(
                    "Pending cast storage did not behave as expected."
            );
        }

        System.out.println("Pending cast storage checks passed.");
    }
}
