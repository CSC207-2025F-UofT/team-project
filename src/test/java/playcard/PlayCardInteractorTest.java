package playcard;

import entity.Deck;
import entity.Direction;
import entity.Game;
import entity.GameFactory;
import entity.Player;
import entity.gameSetting;
import entity.card.*;
import use_case.playcard.*;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayCardInteractorTest {

    @Test
    void successPlayMatchingColorCardTest() {
        final NumberCard firstDiscard = new NumberCard(CardColor.RED, 2);
        final NumberCard matchingCard = new NumberCard(CardColor.RED, 1);
        final Player alice = new Player("alice", List.of(
                matchingCard, new NumberCard(CardColor.BLUE, 3)));
        final Player bob = new Player("bob", List.of(new NumberCard(CardColor.GREEN, 4)));
        final Game game = new GameFactory().create(
                new gameSetting(0, 2), List.of(alice, bob), new Deck(),
                firstDiscard, CardColor.RED);
        final PlayCardDataAccessInterface gameDataAccess = new PlayCardDataAccessInterface() {
            private Game currentGame = game;

            @Override
            public Game getCurrentGame() {
                return currentGame;
            }

            @Override
            public void saveGame(Game updatedGame) {
                currentGame = updatedGame;
            }
        };

        final PlayCardInputData inputData =
                new PlayCardInputData("alice", 0);

        final PlayCardOutputBoundary successPresenter =
                new PlayCardOutputBoundary() {
                    @Override
                    public void prepareSuccessView(PlayCardOutputData outputData) {
                        assertEquals("bob", outputData.getNextPlayerId());
                        assertEquals(CardColor.RED, outputData.getCurrentColor());
                        assertFalse(outputData.isGameFinished());
                        assertEquals(1, outputData.getUpdatedHand().size());
                        assertEquals(CardColor.RED,
                                ((NumberCard) outputData.getPlayedCard()).getColor());
                        assertEquals(1,
                                ((NumberCard) outputData.getPlayedCard()).getNumber());
                    }

                    @Override
                    public void prepareFailView(String errorMessage) {
                        fail("Use case failure is unexpected: " + errorMessage);
                    }
                };


        PlayCardInputBoundary interactor =
                new PlayCardInteractor(gameDataAccess, successPresenter);

        // Act
        interactor.execute(inputData);
    }

    @Test
    void failurePlayMatchingColorCardTest() {
        final NumberCard firstDiscard = new NumberCard(CardColor.RED, 2);

        final Player alice = new Player("alice", List.of(new NumberCard(CardColor.BLUE, 1),
                new NumberCard(CardColor.GREEN, 2)));
        final Player bob = new Player("bob", List.of(new NumberCard(CardColor.GREEN, 4)));

        final Game game = new GameFactory().create(
                new gameSetting(0, 2), List.of(alice, bob), new Deck(),
                firstDiscard, CardColor.RED);
        final PlayCardDataAccessInterface gameDataAccess = new PlayCardDataAccessInterface() {
            private Game currentGame = game;

            @Override
            public Game getCurrentGame() {
                return currentGame;
            }

            @Override
            public void saveGame(Game updatedGame) {
                currentGame = updatedGame;
            }
        };

        final PlayCardInputData inputData =
                new PlayCardInputData("alice", 0);

        final PlayCardOutputBoundary successPresenter =
                new PlayCardOutputBoundary() {
                    @Override
                    public void prepareSuccessView(PlayCardOutputData outputData) {
                        fail("Use case success is unexpected");
                    }

                    @Override
                    public void prepareFailView(String error) {
                        assertEquals("That card cannot be played.", error);
                    }
                };


        PlayCardInputBoundary interactor =
                new PlayCardInteractor(gameDataAccess, successPresenter);

        // Act
        interactor.execute(inputData);
    }

    @Test
    void successPlayMatchingNumberCardTest() {
        final NumberCard firstDiscard = new NumberCard(CardColor.RED, 2);
        final NumberCard matchingCard = new NumberCard(CardColor.BLUE, 2);
        final Player alice = new Player("alice", List.of(matchingCard,
                new NumberCard(CardColor.GREEN, 3)));
        final Player bob = new Player("bob", List.of(new NumberCard(CardColor.YELLOW, 4)));
        final Game game = new GameFactory().create(new gameSetting(0, 2),
                List.of(alice, bob), new Deck(), firstDiscard, CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                assertEquals(CardColor.BLUE, ((NumberCard) outputData.getPlayedCard()).getColor());
                assertEquals(2, ((NumberCard) outputData.getPlayedCard()).getNumber());
                assertEquals(1, outputData.getUpdatedHand().size());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        }).execute(new PlayCardInputData("alice", 0));
    }

    @Test
    void failurePlayMatchingNumberCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new NumberCard(CardColor.BLUE, 1))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 4)))), new Deck(),
                new NumberCard(CardColor.RED, 2), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), failurePresenter())
                .execute(new PlayCardInputData("alice", 0));
        assertEquals(1, game.getCurrentPlayer().getHand().size());
        assertEquals(1, game.getDiscardPile().size());
    }

    @Test
    void successPlayReverseCardTest() {
        final ReverseCard reverse = new ReverseCard(CardColor.BLUE);
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(reverse, new NumberCard(CardColor.YELLOW, 1))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2))),
                new Player("carol", List.of(new NumberCard(CardColor.RED, 3)))), new Deck(),
                new ReverseCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                assertInstanceOf(ReverseCard.class, outputData.getPlayedCard());
                assertEquals("bob", outputData.getNextPlayerId());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        }).execute(new PlayCardInputData("alice", 0));

        assertEquals(Direction.ANTICLOCKWISE, game.getDirection());
        game.advanceTurn();
        assertEquals("carol", game.getCurrentPlayer().getId());
    }

    @Test
    void failurePlayReverseCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new ReverseCard(CardColor.BLUE))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2))),
                new Player("carol", List.of(new NumberCard(CardColor.YELLOW, 3)))), new Deck(),
                new SkipCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), failurePresenter())
                .execute(new PlayCardInputData("alice", 0));
        assertEquals(Direction.CLOCKWISE, game.getDirection());
        game.advanceTurn();
        assertEquals("bob", game.getCurrentPlayer().getId());
    }

    @Test
    void successPlaySkipCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new SkipCard(CardColor.BLUE),
                        new NumberCard(CardColor.YELLOW, 1))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2))),
                new Player("carol", List.of(new NumberCard(CardColor.RED, 3)))), new Deck(),
                new SkipCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                assertInstanceOf(SkipCard.class, outputData.getPlayedCard());
                assertEquals("bob", outputData.getNextPlayerId());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        }).execute(new PlayCardInputData("alice", 0));

        game.advanceTurn();
        assertEquals("carol", game.getCurrentPlayer().getId());
    }

    @Test
    void failurePlaySkipCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new SkipCard(CardColor.BLUE))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2))),
                new Player("carol", List.of(new NumberCard(CardColor.YELLOW, 3)))), new Deck(),
                new ReverseCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), failurePresenter())
                .execute(new PlayCardInputData("alice", 0));
        game.advanceTurn();
        assertEquals("bob", game.getCurrentPlayer().getId());
    }

    @Test
    void successPlayWildCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new WildCard(), new NumberCard(CardColor.YELLOW, 1))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2)))), new Deck(),
                new SkipCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                assertInstanceOf(WildCard.class, outputData.getPlayedCard());
                assertEquals(CardColor.BLUE, outputData.getCurrentColor());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        }).execute(new PlayCardInputData("alice", 0, CardColor.BLUE));

        assertEquals(CardColor.BLUE, game.getCurrentColor());
    }

    @Test
    void successPlayDrawTwoCardTest() {
        final DrawTrackingGame game = new DrawTrackingGame(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new DrawTwoCard(CardColor.RED),
                        new NumberCard(CardColor.YELLOW, 1))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2)))), new Deck(),
                new NumberCard(CardColor.RED, 4), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                assertInstanceOf(DrawTwoCard.class, outputData.getPlayedCard());
                assertEquals("bob", outputData.getNextPlayerId());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected: " + errorMessage);
            }
        }).execute(new PlayCardInputData("alice", 0));

        assertEquals(2, game.cardsDrawnForNextPlayer);
    }

    // failures because color doesn't match.
    @Test
    void failureColorUnmatchPlayDrawTwoCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new DrawTwoCard(CardColor.BLUE))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2)))), new Deck(),
                new NumberCard(CardColor.RED, 4), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), failurePresenter())
                .execute(new PlayCardInputData("alice", 0));
        assertEquals(1, game.getCurrentPlayer().getHand().size());
    }

    // failures because symbol doesn't match.
    @Test
    void failureSymbolUnmatchPlayDrawTwoCardTest() {
        final Game game = new GameFactory().create(new gameSetting(0, 2), List.of(
                new Player("alice", List.of(new DrawTwoCard(CardColor.BLUE))),
                new Player("bob", List.of(new NumberCard(CardColor.GREEN, 2)))), new Deck(),
                new ReverseCard(CardColor.RED), CardColor.RED);

        new PlayCardInteractor(new TestGameDataAccess(game), failurePresenter())
                .execute(new PlayCardInputData("alice", 0));
        assertEquals(1, game.getDiscardPile().size());
    }

    private PlayCardOutputBoundary failurePresenter() {
        return new PlayCardOutputBoundary() {
            @Override
            public void prepareSuccessView(PlayCardOutputData outputData) {
                fail("Use case success is unexpected");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals("That card cannot be played.", errorMessage);
            }
        };
    }

    private static class TestGameDataAccess implements PlayCardDataAccessInterface {
        private Game currentGame;

        TestGameDataAccess(Game currentGame) {
            this.currentGame = currentGame;
        }

        @Override
        public Game getCurrentGame() {
            return currentGame;
        }

        @Override
        public void saveGame(Game updatedGame) {
            currentGame = updatedGame;
        }
    }

    private static class DrawTrackingGame extends Game {
        private int cardsDrawnForNextPlayer;

        DrawTrackingGame(gameSetting settings, List<Player> players, Deck drawPile,
                         Card firstDiscard, CardColor initialColor) {
            super(settings, players, drawPile, firstDiscard, initialColor);
        }

        @Override
        public void drawCardsForNextPlayer(int count) {
            cardsDrawnForNextPlayer += count;
        }
    }
}
