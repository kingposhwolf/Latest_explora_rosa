package com.example.demo.Services.RedisService;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RedisService {
    private RedisTemplate<String, Object> redisTemplate;

    public void saveDataWithDynamicExpiration(String key, Object value, Duration expiration) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        valueOps.set(key, value, expiration);
    }

    public Object getDataByKey(String key) {
        ValueOperations<String, Object> valueOps = redisTemplate.opsForValue();
        return valueOps.get(key);
    }

    public void deleteDataByKey(String key) {
        redisTemplate.delete(key);
    }

    public void updateExpiration(String key, Duration newExpiration) {
        redisTemplate.expire(key, newExpiration);
    }
    
}
