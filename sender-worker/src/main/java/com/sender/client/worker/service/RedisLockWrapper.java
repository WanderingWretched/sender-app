package com.sender.client.worker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisLockWrapper {
    private final RedisLock redisLock;

    @Autowired
    RedisLockWrapper(RedisLock redisLock) {
        this.redisLock = redisLock;
    }

    public void lockAndExecuteTask(String key, Duration duration, Runnable runnable) {

        try {

            if (!redisLock.acquireLock(key, duration)) {
                return;
            }

            runnable.run();

        } finally {
            redisLock.releaseLock(key);
        }
    }
}
