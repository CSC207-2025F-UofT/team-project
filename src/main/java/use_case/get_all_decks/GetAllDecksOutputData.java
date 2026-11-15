package use_case.get_all_decks;

import java.util.Map;

/**
 * Output Data for the GetAllDecks Use Case.
 */
public class GetAllDecksOutputData {

    private final Map<String, Integer> decks;

    public GetAllDecksOutputData(Map<String, Integer> decks) {
        this.decks = decks;
    }

    public Map<String, Integer> getDecks() { return this.decks; }
}
