package use_case.get_deck;

import java.util.List;
import java.util.Map;

/**
 * Output Data for the GetDeck Use Case.
 */
public class GetDeckOutputData {

    private final String name;

    // A list where each value is a map of a flashcards
    // question as the key and answer as the value
    private final List<Map<String, String>> flashcards;

    public GetDeckOutputData(String name, List<Map<String, String>> flashcards) {
        this.name = name;
        this.flashcards = flashcards;
    }

    public String getName() { return name; }

    public List<Map<String, String>> getFlashcards() { return flashcards; }
}
