package interface_adapter;

import java.util.Objects;
import java.util.Optional;
import use_case.*;

/** Routes every player command through the injected serial command dispatcher. */
public final class GameController {
    private final StartGameInputBoundary startGame;
    private final InteractInputBoundary interactObject;
    private final UseItemInputBoundary useItemOnLock;
    private final TriviaInputBoundary trivia;
    private final MoveInputBoundary moveRoom;
    private final SaveGameInputBoundary saveGame;
    private final LoadGameInputBoundary loadGame;
    private final CommandDispatcher commands;

    public GameController(StartGameInputBoundary startGame, InteractInputBoundary interactObject,
                          UseItemInputBoundary useItemOnLock, TriviaInputBoundary trivia,
                          MoveInputBoundary moveRoom, SaveGameInputBoundary saveGame,
                          LoadGameInputBoundary loadGame, CommandDispatcher commands) {
        this.startGame = Objects.requireNonNull(startGame, "startGame");
        this.interactObject = Objects.requireNonNull(interactObject, "interactObject");
        this.useItemOnLock = Objects.requireNonNull(useItemOnLock, "useItemOnLock");
        this.trivia = Objects.requireNonNull(trivia, "trivia");
        this.moveRoom = Objects.requireNonNull(moveRoom, "moveRoom");
        this.saveGame = Objects.requireNonNull(saveGame, "saveGame");
        this.loadGame = Objects.requireNonNull(loadGame, "loadGame");
        this.commands = Objects.requireNonNull(commands, "commands");
    }

    public void listStories() { commands.dispatch(startGame::listStories); }
    public void start(String templateId) { commands.dispatch(() -> startGame.start(templateId)); }
    public void interact(String objectId, String actionId, Optional<String> itemId) {
        commands.dispatch(() -> interactObject.execute(objectId, actionId, itemId));
    }
    public void useItems(String lockId) { commands.dispatch(() -> useItemOnLock.execute(lockId)); }
    public void openTrivia(String lockId) { commands.dispatch(() -> trivia.open(lockId)); }
    public void submitAnswer(String lockId, String answer) {
        commands.dispatch(() -> trivia.submit(lockId, answer));
    }
    public void move() { commands.dispatch(moveRoom::execute); }
    public void save() { commands.dispatch(saveGame::save); }
    public void load() { commands.dispatch(loadGame::load); }
}
