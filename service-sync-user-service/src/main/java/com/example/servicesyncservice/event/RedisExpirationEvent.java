package com.example.servicesyncservice.event;

import com.example.servicesyncservice.entity.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisExpirationEvent {

    @EventListener
    public void handleRedisKeyExpiredEvent(RedisKeyExpiredEvent<RefreshToken> event) {
        RefreshToken expiredRefreshToken = (RefreshToken) event.getValue();

        if(expiredRefreshToken == null) {
            throw new RuntimeException("Токен обновления в функции handleRedisKeyExpiredEvent является пустым");
        }

        log.info("Токен обновления с ключем = {} истек! Токен обновления: {}", expiredRefreshToken.getId(), expiredRefreshToken.getToken());
    }

}
