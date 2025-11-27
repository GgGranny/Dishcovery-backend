package com.dishcovery.backend.service;

import com.dishcovery.backend.interfaces.RefreshTokenService;
import com.dishcovery.backend.model.RefreshToken;
import com.dishcovery.backend.repo.RefreshTokenRepo;
import com.dishcovery.backend.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;


@Service
public class RefreshTokenImpl implements RefreshTokenService {

    @Value("${jwt.refresh.token.expiry.ms}")
    private Long refreshTokenDuration;

    private RefreshTokenRepo refreshTokenRepo;
    private UserRepo userRepo;

    public RefreshTokenImpl(RefreshTokenRepo refreshTokenRepo, UserRepo userRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    @Override
    public RefreshToken createRefreshToken(int id) {
        var refreshToken = new RefreshToken();
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDuration));
        refreshToken.setUser(userRepo.findById(id).get());
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepo.save(refreshToken);
    }

    @Override
    public boolean isTokenExpired(RefreshToken token) {
        return token.getExpiryDate().isBefore(Instant.now());
    }
}
