package taskflower.taskflower.security.oauth2;

import lombok.Data;

import java.util.Map;

@Data
public class OAuth2UserDetails {
    public Map<String, Object> attributes;

    private String id;

    private String name;

    private String email;

    private String image;

    public OAuth2UserDetails(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.id = (String) attributes.get("sub");
        this.name = (String) attributes.get("name");
        this.email = (String) attributes.get("email");
        this.image = (String) attributes.get("picture");
    }
}
