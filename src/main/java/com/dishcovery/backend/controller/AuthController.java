package com.dishcovery.backend.controller;


import com.dishcovery.backend.interfaces.RefreshTokenService;
import com.dishcovery.backend.repo.RefreshTokenRepo;
import com.dishcovery.backend.service.JWTService;
import com.dishcovery.backend.service.RefreshTokenImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private RefreshTokenRepo refreshTokenRepo;
    private RefreshTokenImpl refreshTokenImpl;
    private JWTService jwtService;

    public AuthController(RefreshTokenRepo refreshTokenRepo, RefreshTokenImpl refreshTokenImpl, JWTService jwtService) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.jwtService = jwtService;
        this.refreshTokenImpl = refreshTokenImpl;

    }
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("refreshToken");
        return refreshTokenRepo.findByToken(token)
                .map(t -> {
                    if(refreshTokenImpl.isTokenExpired(t)) {
                        refreshTokenRepo.delete(t);
                        return ResponseEntity.badRequest().body("Refresh Token Expired. Please Login Again");
                    }
                    String newJwt = jwtService.generateToken(t.getUser().getUsername());
                    return ResponseEntity.ok().body(Map.of("token", newJwt));
                })
                .orElse(ResponseEntity.badRequest().body("Invalid Refresh Token"));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> payload) {
        String refreshToken = payload.get("refreshToken");
        if(refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body("Refresh token is required");
        }
        return ResponseEntity.ok().body(refreshTokenRepo.findByToken(refreshToken).map(t -> {
            refreshTokenRepo.delete(t);
            return ResponseEntity.ok().body("Logout Success");
        }).orElse(ResponseEntity.badRequest().body("Invalid Token")));
    }
}
