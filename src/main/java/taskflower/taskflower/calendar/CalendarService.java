package taskflower.taskflower.calendar;

import java.io.IOException;
import java.util.List;

public interface CalendarService {
    Object getEventById(String id);

    Object createEvent(Object event);

    Object updateEvent(Object event);

    void deleteById(String id);

    List<Object> findAllEvents();

    List<Object> synchronize() throws IOException;
}
