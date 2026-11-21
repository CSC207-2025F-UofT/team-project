package entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class GameResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String player1Name;
    private final String player2Name;
    private final int player1Score;
    private final int player2Score;
    private final String winnerName;    // 平局时为 null
    private final LocalDateTime endedAt;

    public GameResult(String player1Name, String player2Name,
                      int player1Score, int player2Score,
                      String winnerName, LocalDateTime endedAt) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.winnerName = winnerName;
        this.endedAt = endedAt;
    }

    public String getPlayer1Name() { return player1Name; }
    public String getPlayer2Name() { return player2Name; }
    public int getPlayer1Score() { return player1Score; }
    public int getPlayer2Score() { return player2Score; }
    public String getWinnerName() { return winnerName; }
    public LocalDateTime getEndedAt() { return endedAt; }
    public boolean isDraw() { return winnerName == null; }
}