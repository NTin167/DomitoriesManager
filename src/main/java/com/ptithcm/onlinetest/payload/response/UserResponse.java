package com.ptithcm.onlinetest.payload.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private Collection<? extends GrantedAuthority> roles;

    public UserResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserResponse(String accessToken, String username, Collection<? extends GrantedAuthority> roles) {
        this.accessToken = accessToken;
        this.username = username;
        this.roles = roles;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return roles;
    }

    public void setRoles(Collection<? extends GrantedAuthority> roles) {
        this.roles = roles;
    }
}
