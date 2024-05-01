package taskflower.taskflower.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@Service
public class GoogleCalendarService {

    public void getListEvents(String accessToken) throws GeneralSecurityException, IOException {
        Calendar service = getCalendarService(accessToken);
        Events events = service.events().list("primary")
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        log.info("[LOG - GoogleCalendarService.getListEvents] items : {}", items.get(0));
        log.info("[LOG - GoogleCalendarService.getListEvents] items : {}", items.get(0).get);
    }

    private static GoogleCredentials getCredential(String accessToken) {
        return GoogleCredentials.newBuilder().setAccessToken(new AccessToken(accessToken, null)).build();
    }

    private static Calendar getCalendarService(String accessToken) throws GeneralSecurityException, IOException {
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                new HttpCredentialsAdapter(getCredential(accessToken))
        ).setApplicationName("taskflower")
                .build();
    }

}
