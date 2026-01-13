package use_case.openGame;
//Is the user trying to start a new game or continue an existing one?
public class OpenGameInputData {
    private final boolean newGame;        // true = new game, false = continue
    private final String startingLocation;
    private final String destination;

    public OpenGameInputData(boolean newGame,
                             String startingLocation,
                             String destination) {
        this.newGame = newGame;
        this.startingLocation = startingLocation;
        this.destination = destination;
    }

    public boolean isNewGame() {
        return newGame;
    }

    public String getStartingLocation() {
        return startingLocation;
    }

    public String getDestination() {
        return destination;
    }
}
