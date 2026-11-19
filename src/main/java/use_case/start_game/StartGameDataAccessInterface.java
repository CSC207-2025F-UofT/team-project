package use_case.start_game;

import entity.Team;

public interface StartGameDataAccessInterface {
    /**
     * Checks if the given team exists.
     * @param teamName the team name to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean teamExists(String teamName);

    /**
     * Gets the team entity with the given team name.
     * @param teamName the team to look up
     * @return the team entity with the given team name.
     */
    Team getTeam(String teamName);
}
