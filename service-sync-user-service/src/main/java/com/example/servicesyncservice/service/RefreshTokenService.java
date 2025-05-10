package com.example.servicesyncservice.service;

import com.example.servicesyncservice.entity.RefreshToken;
import com.example.servicesyncservice.exception.RefreshTokenException;
import com.example.servicesyncservice.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${app.jwt.refreshTokenExpiration}")
    private Duration refreshTokenExpiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByRefreshToken(String token) {
        Optional<RefreshToken> refreshTokenOptional = refreshTokenRepository.findByToken(token);

        if (refreshTokenOptional.isPresent()) {
            RefreshToken refreshToken = refreshTokenOptional.get();
            if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
                log.warn("Refresh Token с ID {} истек. Удаление...", refreshToken.getId());
                refreshTokenRepository.delete(refreshToken);
                throw new RefreshTokenException(token, "Время жизни токена истекло");
            }
        }

        return refreshTokenOptional;
    }

    public RefreshToken createRefreshToken(Long userId) {
        log.info("Создание нового Refresh Token для пользователя с ID: {}", userId);

        refreshTokenRepository.deleteByUserId(userId);

        var refreshToken = RefreshToken.builder()
                .userId(userId)
                .expiryDate(Instant.now().plusMillis(refreshTokenExpiration.toMillis()))
                .token(UUID.randomUUID().toString())
                .build();

        return refreshTokenRepository.save(refreshToken);
    }
    
    public RefreshToken checkRefreshToken(RefreshToken token) {
        if (token == null) {
            throw new RefreshTokenException("Пустой токен", "Токен не может быть null");
        }

        if (token.getExpiryDate().isBefore(Instant.now())) {
            log.warn("Refresh Token с ID {} истек. Удаление...", token.getId());
            refreshTokenRepository.delete(token);
            throw new RefreshTokenException(token.getToken(), "Время жизни токена истекло");
        }

        log.info("Refresh Token с ID {} успешно проверен", token.getId());
        return token;
    }

    public void deleteByUserId(Long userId) {
        log.info("Удаление всех Refresh Token для пользователя с ID: {}", userId);
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Scheduled(cron = "0 0 * * * ?") // Каждый час
    public void cleanupExpiredTokens() {
        log.info("Запуск очистки просроченных Refresh Token...");
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
    }
}