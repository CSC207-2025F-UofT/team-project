package use_case.create_event_list;

/**
 * Output data for the Create Event List use case.
 * Sent from the interactor to the presenter after a list is successfully created.
 */
public class CreateEventListOutputData {

    private final String listName;

    public CreateEventListOutputData(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}