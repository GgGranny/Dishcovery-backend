package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.ForgotPasswordDto;
import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.response.MyResponseHandler;
import com.dishcovery.backend.service.TokenService;
import com.dishcovery.backend.service.UserServicesImp;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserServicesImp userServicesImp;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Users user) {
        Map<String, String> msg = userServicesImp.registerUser(user);
        if(msg.get("status").equals("400")) {
            return MyResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, msg.get("message"), null);
        }
        return  MyResponseHandler.responseBuilder(HttpStatus.CREATED, msg.get("message"), user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto userDto) {
        return userServicesImp.verify(userDto);
    }


    @GetMapping("/register/verify-email/{token}")
    public String confirmToken(@PathVariable("token") String token) {
        tokenService.validate(token);
        return "success";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        System.out.println(forgotPasswordDto.getEmail());
        return userServicesImp.verifyEmailAndSendOTP(forgotPasswordDto.getEmail());
    }

    @PostMapping("/forgot-password/verify/{token}")
    public ResponseEntity<?> forgotPassword(@PathVariable("token") String token, @RequestBody ForgotPasswordDto forgotPasswordDto) {
        try {
            if(userServicesImp.updateUserPassword(token, forgotPasswordDto.getNewPassword())){
                return MyResponseHandler.responseBuilder(HttpStatus.OK, "password updated successfully", null);
            }
        }catch(Exception e){
            throw  new RuntimeException(e);
        }
        return MyResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, "password update failed", null);
    }


//    @GetMapping("/user/logout")
//    public ResponseEntity<?> logoutUser(HttpSession session) {
//        session.invalidate();
//        return MyResponseHandler.responseBuilder(HttpStatus.OK, "user logged out successfully", null);
//    }

}
