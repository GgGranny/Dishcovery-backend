package com.dishcovery.backend.service;


import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.model.Token;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.response.MyResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServicesImp {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenService tokenService;

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";

    public Map<String, String> registerUser(Users user) {
        Map<String, String>  message = new HashMap<>();
        if(!usernameExists(user.getUsername())) {
            message.put("message", "username alredy taken");
            message.put("status", "400");
            return message;
        }

        if(!emailExists(user.getEmail())) {
            message.put("message", "Email alredy taken");
            message.put("status", "400");
            return message;
        }
        user.setEnabled(false);
        user.setProfilePicture(DEFAULT_PROFILE_IMAGE);
        user.setRole("USER");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepo.save(user);
        message.put("message", "Please confirm your email");
        message.put("status", "200");

        //Save the token
        Token token = new Token(user,tokenService.generateToken(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now());
        System.out.println("token:" + token);
        tokenService.saveToken(token);

        //send mail
        mailService.sendMail(user.getEmail(), token.getToken());
        return message;
    }

    //check if username already exists
    private boolean usernameExists(String username) {
        Users userPresent = userRepo.findByUsername(username);
        if(userPresent == null) {
            return true;
        }
        return false;
    }

    //check if email already exists
    private boolean emailExists(String email) {
        Users userPresent = userRepo.findByEmail(email);
        if(userPresent == null) {
            return true;
        }
        return false;
    }

    public ResponseEntity<Object> verify(LoginDto userDto) {
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
            if(authentication.isAuthenticated()) {
                return MyResponseHandler.responseBuilder(HttpStatus.OK, "Login Successful", null);
            }
            return MyResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, "Login Failed", null);
        }catch(Exception e) {
            return MyResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, "Please verify your email", null);
        }
    }
}
