package com.gmg.travice.feature.login.oAuth.user;

import com.gmg.travice.domain.user.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    private final UserDTO user;

    public CustomOAuth2User(UserDTO user) {
        this.user = user;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        return collection;
    }

    @Override
    public String getName() {
        return user.getName();
    }

    public String getToken(){
        return user.getToken();
    }

    public String getEmail(){return user.getEmail();}
}
