package com.sketchandguess.entities;

import java.time.LocalDateTime;

public class GameSession {
    private String sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String promptUsed;
    private boolean isWin;
    private double finalScore;
    private double timeLimitUsed;
    private String drawingReference;
}