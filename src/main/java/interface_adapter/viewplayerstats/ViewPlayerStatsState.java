package interface_adapter.viewplayerstats;

import java.util.List;
import java.util.Objects;

public final class ViewPlayerStatsState {


    private final String playerId;
    private final String gamesPlayedText;
    private final String gameWinText;
    private final String winRateText;
    private final String unoCallsMadeText;
    private final List<String> achievements;
    private final String errorMessage;


    public ViewPlayerStatsState() {
        this("",
                "",
                "",
                "",
                "",
                List.of(),
                "");
    }

    public ViewPlayerStatsState(String playerId,
                                String gamesPlayedText,
                                String gameWinText,
                                String winRateText,
                                String unoCallsMadeText,
                                List<String> achievements,
                                String errorMessage) {
        this.playerId = Objects.requireNonNull(playerId);
        this.gamesPlayedText = Objects.requireNonNull(gamesPlayedText);
        this.gameWinText = Objects.requireNonNull(gameWinText);
        this.winRateText = Objects.requireNonNull(winRateText);
        this.unoCallsMadeText = Objects.requireNonNull(unoCallsMadeText);
        this.achievements = List.copyOf(achievements);
        this.errorMessage = Objects.requireNonNull(errorMessage);
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getGamesPlayedText() {
        return gamesPlayedText;
    }

    public String getGameWinText() {
        return gameWinText;
    }

    public String getWinRateText() {
        return winRateText;
    }

    public String getUnoCallsMadeText() {
        return unoCallsMadeText;
    }

    public List<String> getAchievements() {
        return achievements;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
