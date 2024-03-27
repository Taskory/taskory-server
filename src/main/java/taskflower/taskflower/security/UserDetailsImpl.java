package taskflower.taskflower.security;


import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import taskflower.taskflower.user.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Data
public class UserDetailsImpl implements UserDetails, OAuth2User {

    private long id;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Map<String, Object> attribute;

    public UserDetailsImpl(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
//        this.authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    public UserDetailsImpl(User user, Map<String, Object> attribute) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.attribute = attribute;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.attribute;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
