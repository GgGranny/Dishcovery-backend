package com.dishcovery.backend.controller;


import com.dishcovery.backend.components.RecipeComponent;
import com.dishcovery.backend.components.UserSession;
import com.dishcovery.backend.dto.ForgotPasswordDto;
import com.dishcovery.backend.dto.LoginDto;
import com.dishcovery.backend.dto.UserBioDto;
import com.dishcovery.backend.dto.UserResponseDto;
import com.dishcovery.backend.model.Users;
import com.dishcovery.backend.response.MyResponseHandler;
import com.dishcovery.backend.service.TokenService;
import com.dishcovery.backend.service.UserServicesImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserServicesImp userServicesImp;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RecipeComponent recipeComponent;

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
        Map<String, String> tokens = userServicesImp.verify(userDto);
        if(!tokens.isEmpty()){
            tokens.put("message", "Login Successful");
            return ResponseEntity.ok().body(tokens);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("login Failed");
    }


    @GetMapping("/register/verify-email/{token}")
    public String confirmToken(@PathVariable String token) {
        tokenService.validate(token);
        return "success";
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        System.out.println(forgotPasswordDto.getEmail());
        return userServicesImp.verifyEmailAndSendOTP(forgotPasswordDto.getEmail());
    }

    @PostMapping("/forgot-password/verify/{token}")
    public ResponseEntity<?> forgotPassword(@PathVariable String token, @RequestBody ForgotPasswordDto forgotPasswordDto) {
        try {
            if(userServicesImp.updateUserPassword(token, forgotPasswordDto.getNewPassword())){
                return MyResponseHandler.responseBuilder(HttpStatus.OK, "password updated successfully", null);
            }
        }catch(Exception e){
            throw  new RuntimeException(e);
        }
        return MyResponseHandler.responseBuilder(HttpStatus.BAD_REQUEST, "password update failed", null);
    }

    @PostMapping("/upload/profile/{userId}")
    public ResponseEntity<?> uploadProfile(@RequestParam("file") MultipartFile file, @PathVariable int userId) {
        if(file == null) throw new RuntimeException("No profile found");
        Map<String, String> response = new HashMap<>();
        try {
            String profile = userServicesImp.uploadUserProfile(file, userId);
            response.put("message", "profile uploaded successfully");
            response.put("data", profile);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch(Exception e) {
            e.printStackTrace();
            response.put("message", "failed to upload profile");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/profile/{userId}")
    public ResponseEntity<?> userProfile(@PathVariable("userId") int id) throws IOException {
        String profile = userServicesImp.fetchProfile(id);
        Map<String, String> response = new HashMap<>();
        if(profile.isEmpty()) {
            response.put("data", "no user profile");
            response.put("userId", String.valueOf(id));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        response.put("data", profile);
        response.put("userId", String.valueOf(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/edit/{userId}")
    public ResponseEntity<?> fetchUserData(@PathVariable int userId)  {
        UserResponseDto responseDto = null;
        try{
        responseDto = userServicesImp.fetchUser(userId);
            if(responseDto == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.FOUND).body(responseDto);
    }

    @PostMapping("/user/edit/{userId}")
    public ResponseEntity<?> userBio(@RequestBody UserBioDto userBioDto , @PathVariable int userId){
        Users user = userServicesImp.updateUserBio(userBioDto, userId);
        if(user == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String profile = null;
        try {
            profile = userServicesImp.fetchProfile(userId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        UserResponseDto response = new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                profile != null ? profile: user.getProfilePicture(),
                user.getRole(),
                user.getEnabled(),
                user.getDisplayName(),
                user.getBio(),
                user.getRecipeCount(),
                user.getFollowersCount(),
                user.getCuisinePreferences()
        );
        return ResponseEntity.ok().body(response);
    }

}
