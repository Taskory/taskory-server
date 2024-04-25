package taskflower.taskflower.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import taskflower.taskflower.annotation.CurrentUser;
import taskflower.taskflower.security.model.UserPrincipal;
import taskflower.taskflower.security.service.TokenService;
import taskflower.taskflower.service.GoogleCalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Slf4j
@RestController
@RequestMapping("${app.api-base-url}/google/calendar")
public class GoogleCalendarController {
    private final GoogleCalendarService googleCalendarService;
    private final TokenService tokenService;

    @Autowired
    public GoogleCalendarController(GoogleCalendarService googleCalendarService, TokenService tokenService) {
        this.googleCalendarService = googleCalendarService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public void findEvents(@CurrentUser UserPrincipal userPrincipal, HttpServletRequest request) throws IOException, GeneralSecurityException {
        String token = tokenService.getTokenFromRequest(request);
//        googleCalendarService.getCalendarEvents(token);

        String result = googleCalendarService.getListEvents(userPrincipal, token);
        log.info("[LOG] result : {}", result);
    }
}
