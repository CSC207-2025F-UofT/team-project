package use_case.create_event_list;


public class CreateEventListOutputData {

    private final String listName;

    public CreateEventListOutputData(String listName) {
        this.listName = listName;
    }

    public String getListName() {
        return listName;
    }
}