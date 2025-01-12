package codearchitect99.taskory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;

import java.util.List;

@Configuration
public class GoogleOAuth2Config {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.google.scope}")
    private List<String> scopes;
    @Value("${spring.security.oauth2.uris.authorization-uri}")
    private String authorizationUri;
    @Value("${spring.security.oauth2.uris.token-uri}")
    private String tokenUri;
    @Value("${spring.security.oauth2.uris.userinfo-uri}")
    private String userInfoUri;
    @Value("${spring.security.oauth2.uris.jwt-set-uri}")
    private String jwtSetUri;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    @Bean
    public ClientRegistration googleClientRegistration() {
        String providerId = "google";
        return ClientRegistration.withRegistrationId(providerId)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri(redirectUri)
                .scope(scopes)
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .userInfoUri(userInfoUri)
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri(jwtSetUri)
                .clientName(providerId)
                .build();
    }

}
