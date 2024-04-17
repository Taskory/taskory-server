package taskflower.taskflower.security.data;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import taskflower.taskflower.model.entity.User;
import taskflower.taskflower.model.enums.Role;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserPrincipal implements UserDetails, OAuth2User {

    private long id;
    private String name;
    private String email;
    private String password;
    private Set<GrantedAuthority> authorities = new HashSet<>();
    private Map<String, Object> attributes;

    public UserPrincipal(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getDescription()));
        }
    }

    public UserPrincipal(User user, Map<String, Object> attributes) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getDescription()));
        }
        this.attributes = attributes;
    }


    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
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
        return this.name;
    }

    public boolean isOfficialUser() {
        for (GrantedAuthority authority : this.authorities) {
            if (Objects.equals(Role.TEMP_USER.getDescription(), authority.getAuthority())) {
                return false;
            }
        }
        return true;
    }
}
