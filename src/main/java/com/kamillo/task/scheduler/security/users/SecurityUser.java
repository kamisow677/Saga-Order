package com.kamillo.task.scheduler.security.users;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class SecurityUser implements UserDetails {

    private final CustomeUser customeUser;

    public SecurityUser(CustomeUser customeUser) {
        this.customeUser = customeUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return customeUser.getRoles()
                .stream().map(it -> new SimpleGrantedAuthority(it.getName().name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return customeUser.getPassword();
    }

    @Override
    public String getUsername() {
        return customeUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
