package taskflower.taskflower.security.oauth2;

import taskflower.taskflower.security.OAuth2AuthenticationProcessingException;

import java.util.Map;

public class OAuth2UserDetailsFactory {

    private OAuth2UserDetailsFactory() {
        throw new IllegalStateException("This is Factory class. Not Instance");
    }

    public static OAuth2UserDetails getOAuth2UserDetails(String registrationId, Map<String, Object> attributes) throws OAuth2AuthenticationProcessingException {
        if (registrationId.equalsIgnoreCase(AuthProviderType.Google.toString())) {
            return new GoogleOAuth2UserDetailsImpl(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
