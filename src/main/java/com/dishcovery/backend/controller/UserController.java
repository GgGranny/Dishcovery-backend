package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.response.MyResponseHandler;
import com.dishcovery.backend.service.TokenService;
import com.dishcovery.backend.service.UserServicesImp;
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

}
