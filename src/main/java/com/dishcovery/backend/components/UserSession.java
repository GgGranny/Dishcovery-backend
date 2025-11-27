package com.dishcovery.backend.components;


import com.dishcovery.backend.model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserSession {

    private UserDetails user = null;

    public UserSession() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                this.user = (UserDetails) authentication.getPrincipal();
            }
        }
    }

    public UserDetails getUser() {
        return user;
    }


}
