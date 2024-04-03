package taskflower.taskflower.task.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.stereotype.Service;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
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
