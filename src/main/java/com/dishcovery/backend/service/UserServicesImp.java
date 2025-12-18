package com.dishcovery.backend.service;


import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.dto.UserDto;
import com.dishcovery.backend.model.RefreshToken;
import com.dishcovery.backend.model.Token;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.repo.RefreshTokenRepo;
import com.dishcovery.backend.repo.TokenRepo;
import com.dishcovery.backend.repo.UserRepo;
import com.dishcovery.backend.response.MyResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
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

    @Autowired
    private JWTService jwtService;

    @Autowired
    private RefreshTokenImpl refreshTokenService;

    private static final String DEFAULT_PROFILE_IMAGE = "https://cdn-icons-png.flaticon.com/512/3135/3135715.png";
    private TokenRepo tokenRepo;

    @Autowired
    private RefreshTokenRepo refreshTokenRepo;

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

    public  Map<String, String> verify(LoginDto userDto) {
        Map<String, String> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword())
            );
            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(userDto.getUsername());

                Users u = userRepo.findByUsername(userDto.getUsername());
                RefreshToken oldRefreshToken = refreshTokenRepo.findByUser(u);
                if(oldRefreshToken != null ) {
                    response.put("refreshToken", oldRefreshToken.getToken());
                    response.put("token", token);
                    response.put("user_id", String.valueOf(u.getId()));
                    return response;
                }
                String refreshToken = refreshTokenService.createRefreshToken(u.getId()).getToken();
                response.put("token", token);
                response.put("refreshToken", refreshToken);
                return response;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public ResponseEntity<Object> verifyEmailAndSendOTP(String email) {
        try {
            Users user = userRepo.findByEmail(email);
            if(user == null) {
                throw new IllegalArgumentException("No such user found");
            }

            //Generate OTP token and save it
            Token token = new Token(user,tokenService.generateToken(), LocalDateTime.now().plusMinutes(2), LocalDateTime.now());
            tokenService.saveToken(token);

            // send otp to recover change password
            Map<String, String> msg =new HashMap<>();
            msg.put("expires_at", "2min");
            mailService.sendMail(user.getEmail(), token.getToken());
            return MyResponseHandler.responseBuilder(HttpStatus.OK, "An OTP is sent to your Email", msg);
        }catch(Exception e) {
            throw new IllegalArgumentException("Please verify your email", e);
        }
    }

    public boolean updateUserPassword(String token, String newPassword) {

        Map<String, Object> response = new HashMap<>();

        //check if the token exists
        Token tokenOpt = tokenService.getTokenByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found"));

        // check if the token is expired
        LocalDateTime expireTime = tokenOpt.getExpiresAt();
        if(expireTime.isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Token is expired");
        }

        //check if token is confirmed and if not confirm it
        if(tokenOpt.getConfirmAt() != null) {
            throw new IllegalArgumentException("Token is already confirmed");
        }

        // update users new password
        Users user = tokenOpt.getUser();
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepo.save(user);

        response.put("status", HttpStatus.OK);
        return true;
    }
}
