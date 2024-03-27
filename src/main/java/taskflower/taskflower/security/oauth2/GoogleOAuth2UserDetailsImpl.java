package taskflower.taskflower.security.oauth2;

import java.util.Map;

public class GoogleOAuth2UserDetailsImpl extends OAuth2UserDetails {
    public GoogleOAuth2UserDetailsImpl(Map<String, Object> attributes) {
        super(attributes);
    }
}
