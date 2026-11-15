package interface_adapter.get_all_decks;

import java.util.Map;

/**
 * The state for the GetAllDecks View Model.
 */
public class GetAllDecksState {

    private Map<String, Integer> decks;
    private String getAllDecksError;

    public Map<String, Integer> getDecks() {
        return decks;
    }

    public String getGetAllDecksError() {
        return  getAllDecksError;
    }

    public void setDecks(Map<String, Integer> decks) {
        this.decks = decks;
    }

    public void setGetAllDecksError(String getAllDecksError) {
        this.getAllDecksError = getAllDecksError;
    }
}
