package data_access;

import entity.Event;
import entity.EventList;
import use_case.save_event_to_list.SaveEventToListDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserDataAccessObject implements SaveEventToListDataAccessInterface {
    // eventLists is an arrayList that stored the eventList that we already created by use case of create_event_list
    private final List<EventList> eventLists = new ArrayList<>();

    @Override
    public void save(Event event, EventList eventList) {
        // save includes two parts:
        // 1 save in an ArrayList of eventList
        // 2 save write content of ArrayList in a persistent file outside

        // I meet the same problem on eventList, missing of .equals()
        // firstly I assume eventLists.contains() works normally

        // below code must be fixed after override .equals()s in Event and EventList
        int index = eventLists.indexOf(eventList);
        // do not deal with situation that eventList is not found
        // since it is impossible, and if it does not contain st eventList, return of indexOf is -1
        if (index != -1) {
            EventList found = eventLists.get(index);
            found.addEvent(event);
            System.out.println("some methods to store eventList into a persistent file");
        }
    }
}
