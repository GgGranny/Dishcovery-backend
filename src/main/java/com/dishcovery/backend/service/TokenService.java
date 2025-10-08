package com.dishcovery.backend.service;


import com.dishcovery.backend.model.Token;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.TokenRepo;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    private UserRepo userRepo;

    public Optional<Token> getTokenByToken(String token) {
        return tokenRepo.findByToken(token);
    }

    public Token saveToken(Token token) {
        return tokenRepo.save(token);
    }

    public void validate(String token) {
        // check if the token exists
        Token tokenConfirmed = tokenRepo.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        // check if the token is confirmed
        if(tokenConfirmed.getConfirmAt()== null) {
            throw new IllegalArgumentException("Token is already confirmed");
        }

        // check if the token is expired
        LocalDateTime expireDateTime = tokenConfirmed.getConfirmAt();
        if(expireDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token is expired");
        }

        //update the user Email and token
        tokenConfirmed.setConfirmAt(LocalDateTime.now());
        tokenRepo.save(tokenConfirmed);

        // Enable the user
        enableUser(tokenConfirmed.getUser());
    }

    private void enableUser(Users user) {
        user.setEnabled(true);
        userRepo.save(user);
    }
}
