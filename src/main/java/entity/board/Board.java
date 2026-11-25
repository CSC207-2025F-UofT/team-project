package entity.board;

import entity.Player;
import entity.card.Card;
import entity.card.CharacterCard;
import entity.card.SorceryCard;
import entity.card.SpellCard;

public class Board {
    private Tile[][] board = new Tile[6][6];
    public Board() {
        this.board = new Tile[6][6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                this.board[i][j] = new Tile();
            }
        }
    }
    /// Moves all cards on a tile from position (x, y) to position (i, j) on the board
    /// @param x The x position of the tile to be moved
    /// @param y The y position of the tile to be moved
    /// @param i The x position of the target tile
    /// @param j The y position of the target tile
    public void moveTile(int x, int y, int i, int j) {
        this.board[i][j].setTile(board[x][y]);
        this.board[x][y].clearTile();
    }

    /// Attempts to Play a Card onto the tile at (x, y)
    /// @param x The x position of the target tile
    /// @param y The y position of the target tile
    /// @param card The card to be played onto this tile
    public void playCard(int x, int y, Card card) {
        Tile tile = this.board[x][y];
        if (card instanceof SpellCard) {
            if (tile.getCharacterCard() != null) {
                tile.addSpell((SpellCard) card);
            } else {
                throw new IllegalMoveException("Can't add a Spell Card to a Tile with no Character.");
            }
        } else if (tile.isEmpty() && card instanceof CharacterCard) {
            tile.setCharacterCard((CharacterCard) card);
        } else if (tile.isEmpty() && card instanceof SorceryCard) {
            tile.setSorceryCard((SorceryCard) card);
        } else {
            // This is not perfect, but I'm not sure how to make it better per se
            if (!tile.isEmpty()) {
                throw new IllegalMoveException("Target space is occupied.");
            }
            throw new IllegalArgumentException("Unknown Card Type: " + card.getClass().getName());
        }
    }

    /// Calculates the HP score on the board of all Character cards that belong to this player
    /// @param player The name of the player for which the score is calculated
    /// @return The score, in current HP of all character cards.
    public int calculateScore(Player player){
        int score = 0;
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 6; j++){
                CharacterCard card = this.board[i][j].getCharacterCard();
                if (card != null && player.equals(card.getOwner())) {
                    score += card.getHP();
                }
            }
        }
        return score;
    }

    /// Make damage checks against everything that's in the explosion zone of the spell
    /// @param x Explosion origin x
    /// @param y Explosion origin y
    /// @param spellCard Spell that has been cast
    private void checkExplosion(int x, int y, SpellCard spellCard) {
        // TODO: Implement this (May need to be an additional feature for later).

    }

    /// Step the spell cast's movement (part of Resolve Cast)
    /// @param gridTransform The GridTransform that describes the position and direction of the spell in flight
    /// @param spellCard The spell card that is being cast
    /// @param traveled How far, in tiles this spell has traveled so far
    private void stepSpell(GridTransform gridTransform, SpellCard spellCard, int traveled) {
        int x = gridTransform.x();
        int y = gridTransform.y();

        // Do damage to any card that is present on this tile
        CharacterCard characterCard = this.board[x][y].getCharacterCard();
        if (characterCard != null) {
            characterCard.takeDamage(spellCard.getDamage());

            // Non-Piercing cards stop when hitting a character, so we return
            if (!spellCard.isPiercing()) {
                return;
            }
        }

        // Respect Sorcery Card Shenanigans
        SorceryCard sorceryCard = this.board[x][y].getSorceryCard();
        if (sorceryCard != null) {
            // Mirror reverses flight path and resets travel distance
            if (sorceryCard.getName().equals("Mirror")) {
                gridTransform = new GridTransform(x, y, Directions.reverse(gridTransform.direction()));
                traveled = 0;
            }
        }

        // Check if spell has reached the end of its flight
        if (traveled >= spellCard.getRange()-1 && Directions.isDiagonal(gridTransform.direction())) {
            // Diagonal flight goes 1 tile less far
//            checkExplosion(x, y, spellCard);
            return;
        } else if (traveled >= spellCard.getRange() && !Directions.isDiagonal(gridTransform.direction())) {
            // Non-diagonal flight
//            checkExplosion(x, y, spellCard);
            return;
        }

        stepSpell(gridTransform.step(), spellCard, traveled + 1);
    }

    // Getters
    public Tile getTile(int x, int y) {
        return this.board[x][y];
    }
}
