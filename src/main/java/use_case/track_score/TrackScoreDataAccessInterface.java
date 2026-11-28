package use_case.track_score;

import entity.Team;

public interface TrackScoreDataAccessInterface {
    /**
     * Gets the team with the given name.
     * @param teamName the name of the team to look up
     * @return the Team, or null if no team has that name
     */
    Team getTeam(String teamName);

    /**
     * Saves the given team and its score.
     * @param team the team to save
     */
    void saveTeam(Team team);
}









