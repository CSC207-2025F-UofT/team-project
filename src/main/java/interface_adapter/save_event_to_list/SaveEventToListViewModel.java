package interface_adapter.save_event_to_list;

import entity.Event;
import entity.EventList;

public class SaveEventToListViewModel {
    // for information that affect creation of UI, they are:
    // 1 the current eventList in memory
    // 2 message from Interactor
    // 3 the event is saved
    private Event event;
    private EventList[] eventLists;
    private String message;

    public Event getEvent() {
        return event;
    }

    public EventList[] getEventList() {
        return eventLists;
    }

    public String getMessage() {
        return message;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEventList(EventList[] eventLists) {
        this.eventLists = eventLists;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
