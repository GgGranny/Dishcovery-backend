package com.dishcovery.backend.interfaces;


import com.dishcovery.backend.model.RefreshToken;

public interface RefreshTokenService {

    // Create Refresh Token
    RefreshToken createRefreshToken(int id);
    // Check Is the Token Expired
    boolean isTokenExpired(RefreshToken token);
    // Check if the token Exists For a User

}
