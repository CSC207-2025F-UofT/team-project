package use_case.save_event_to_list;

import entity.Event;
import entity.EventList;

public class SaveEventToListOutputData {
    // output of save is told use which eventList that event fail to save in, with a message

    private final Event event;
    private final EventList[] eventLists;
    private final String message;

    public SaveEventToListOutputData(Event event, EventList[] eventLists, String message) {
        this.event = event;
        this.eventLists = eventLists;
        this.message = message;
    }

    public Event getEvent() {
        return event;
    }

    public EventList[] getEventLists() {
        return eventLists;
    }

    public String getMessage() {
        return message;
    }
}
