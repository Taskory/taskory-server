package taskflower.taskflower.calendar;

import com.google.api.services.calendar.model.Event;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleCalendarService implements CalendarService {

    @Override
    public Object getEventById(String id) {
        return null;
    }

    @Override
    public Object createEvent(Object event) {
        return null;
    }

    @Override
    public Object updateEvent(Object event) {
        return null;
    }

    @Override
    public void deleteById(String id) {
        /* delete function does not need a type */
    }

    @Override
    public List<Object> findAllEvents() {
        return null;
    }

    @Override
    public List<Object> synchronize() throws IOException {
        List<Event> events;
        try {
            events = CalendarFactory.googleCalendar().events().list("primary")
//                    .setMaxResults(1)
//                    .setTimeMin(now)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute()
                    .getItems();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e.getMessage());
        }
        for (Event event : events) {

        }

        return null;
    }

}
