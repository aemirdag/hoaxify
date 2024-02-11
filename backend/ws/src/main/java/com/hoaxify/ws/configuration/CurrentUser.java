package com.hoaxify.ws.configuration;

import com.hoaxify.ws.user.User;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@NoArgsConstructor
@ToString
@Setter
public class CurrentUser implements UserDetails {
    @Getter
    private long id;
    private boolean active;
    private String username;
    @Getter
    private String email;
    private String password;

    public CurrentUser(User user) {
        id = user.getId();
        active = user.isActive();
        username = user.getUsername();
        email = user.getEmail();
        password = user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return active;
    }
}
