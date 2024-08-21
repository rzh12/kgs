package com.example.kgs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void saveKeyToCache(String key) {

        redisTemplate.opsForValue().set(key, "", 86400, TimeUnit.SECONDS);
    }

    public boolean isKeyInCache(String key) {
        return redisTemplate.hasKey(key);
    }

    public long getKeyCount() {
        return redisTemplate.getConnectionFactory().getConnection().dbSize();
    }

    public String getKeyFromCache(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteKeyFromCache(String key) {
        redisTemplate.delete(key);
    }
}

