package use_case.get_deck;

/**
 * The Input Data for the GetDeck Use Case.
 */
public class GetDeckInputData {
    private final String name;

    public GetDeckInputData(String name) { this.name = name; }

    String getName() { return name; }
}
