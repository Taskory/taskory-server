package taskflower.taskflower.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import taskflower.taskflower.security.model.UserPrincipal;
import taskflower.taskflower.security.service.TokenService;

@Service
public class GoogleCalendarService {

    private static final String CALENDAR_API_URL = "https://www.googleapis.com/calendar/v3/calendars/primary/events";

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final RestTemplate restTemplate;
    private final TokenService tokenService;

    @Autowired
    public GoogleCalendarService(OAuth2AuthorizedClientService authorizedClientService, RestTemplate restTemplate, TokenService tokenService) {
        this.authorizedClientService = authorizedClientService;
        this.restTemplate = restTemplate;
        this.tokenService = tokenService;
    }

    public String getListEvents(UserPrincipal userPrincipal, String authorizationCode) {
        String accessToken = getAccessToken(authorizationCode);
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient("google", userPrincipal.getName());

        // Set OAuth2 access token in request header
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        // Make request to Google Calendar API
        ResponseEntity<String> response = restTemplate.exchange(CALENDAR_API_URL, HttpMethod.GET, new HttpEntity<>(headers), String.class);
        return response.getBody();
    }


    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${app.oauth2.token-uri}")
    private String tokenUri;

    private String getAccessToken(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", String.valueOf(AuthorizationGrantType.AUTHORIZATION_CODE));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }
}
