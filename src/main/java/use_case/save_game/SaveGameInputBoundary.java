package use_case.save_game;

public interface SaveGameInputBoundary {
    void execute(String username);

    void execute(String username, String name);
}
