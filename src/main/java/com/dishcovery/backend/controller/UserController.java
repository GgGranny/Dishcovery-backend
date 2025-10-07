package com.dishcovery.backend.controller;


import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.response.ResponseHandler;
import com.dishcovery.backend.service.UserServicesImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserServicesImp userServicesImp;

    @PostMapping("/register")
    public ResponseEntity<Object> registerUser(@RequestBody Users user) {
        Map<String, String> msg = userServicesImp.registerUser(user);
        if(msg.get("status").equals("400")) {
            return ResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, msg.get("message"), null);
        }
        return  ResponseHandler.responseBuilder(HttpStatus.CREATED, msg.get("message"), user);
    }


    @PostMapping("/login")
    public String loginUser(@RequestBody LoginDto userDto) {
        System.out.println("hello login");
        return userServicesImp.loginUser(userDto);
    }


}
