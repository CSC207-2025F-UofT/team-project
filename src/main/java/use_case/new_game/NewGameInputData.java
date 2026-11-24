package use_case.new_game;

/**
 * Input for starting a new game. It is empty in Story 2 because a new game
 * needs no input yet. Later stories add fields here (e.g. the username from
 * Story 1) without changing NewGameInputBoundary.execute(...), so the
 * controller and interactor signatures stay stable.
 */
public class NewGameInputData {
}
