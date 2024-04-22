package taskflower.taskflower.security.model;

import java.util.Map;

public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    protected OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getSubId();

    public abstract String getEmail();

    public abstract String getName();

}
