package com.dishcovery.backend.components;

import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WebsocketAuthInterceptor implements ChannelInterceptor {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(stompHeaderAccessor == null) {
            throw new RuntimeException("stomp header accessor is null");
        }
        if(StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())) {
            String tokenData = stompHeaderAccessor.getFirstNativeHeader("Authorization");
            if(tokenData == null || !tokenData.startsWith("Bearer ")) {
                throw new RuntimeException("authorization is null");
            }
            String token = tokenData.substring(7);
            String username = jwtService.extractUsername(token);
            System.out.println(" this is intercepted : "+username );
            List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority("USER"));
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
            stompHeaderAccessor.setUser(authentication);
        }
        return message;
    }
}
