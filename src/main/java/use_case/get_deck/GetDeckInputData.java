package use_case.get_deck;

/**
 * The Input Data for the GetDeck Use Case.
 */
public class GetDeckInputData {
    private final String name;
    private final String viewName;

    public GetDeckInputData(String name, String viewName) {
        this.name = name;
        this.viewName = viewName;
    }

    String getName() { return name; }

    String getViewName() { return viewName; }
}
