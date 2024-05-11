package com.sender.client.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Component
public class RedisLock {

    private final ValueOperations<String, Long> valueOperations;

    private final RedisTemplate redisTemplate;

    @Autowired
    RedisLock(
            ValueOperations<String, Long> valueOperations,
            RedisTemplate redisTemplate
    ) {
        this.valueOperations = valueOperations;
        this.redisTemplate = redisTemplate;
    }

    private static final String LOCK_FORMAT = "%s::lock";

    public boolean acquireLock(String key, Duration duration) {
        String lockKey = getLockKey(key);

        Long expiresAtMillis = valueOperations.get(lockKey);

        long currentTimeMillis = System.currentTimeMillis();

        if (Objects.nonNull(expiresAtMillis)) {
            if (currentTimeMillis <= expiresAtMillis) {
                return false;
            }

            redisTemplate.delete(lockKey);
        }


        return Optional
                .ofNullable(
                        valueOperations.setIfAbsent(
                                lockKey,
                                currentTimeMillis + duration.toMillis()
                        )
                ).orElse(false);

    }

    public void releaseLock(String key) {
        String lockKey = getLockKey(key);

        redisTemplate.delete(lockKey);
    }

    private static String getLockKey(String key) {
        return LOCK_FORMAT.formatted(key);
    }
}
