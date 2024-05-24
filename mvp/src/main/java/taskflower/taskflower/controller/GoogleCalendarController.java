package taskflower.taskflower.controller;

import com.google.api.services.calendar.model.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskflower.taskflower.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("${app.api-base-url}/google/calendar")
public class GoogleCalendarController {
    private final GoogleCalendarService googleCalendarService;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @Autowired
    public GoogleCalendarController(GoogleCalendarService googleCalendarService, OAuth2AuthorizedClientService authorizedClientService) {
        this.googleCalendarService = googleCalendarService;
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping
    public void findEvents(Authentication authentication) throws GeneralSecurityException, IOException {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("google", authentication.getName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        googleCalendarService.getListEvents(accessToken);
    }
}
