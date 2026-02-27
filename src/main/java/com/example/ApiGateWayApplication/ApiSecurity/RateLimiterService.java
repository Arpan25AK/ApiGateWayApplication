package com.example.ApiGateWayApplication.ApiSecurity;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class RateLimiterService {
    private final StringRedisTemplate redisTemplate;

    private static final int reqLimit = 5;
    private static final Duration keyExpiration = Duration.ofMinutes(1);

    public RateLimiterService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * Checks if a user is allowed to make a request.
     * @param userId The unique identifier (username from JWT)
     * @return true if allowed, false if rate-limited
     */
    public Boolean isValid(String userId){
        String key = "Rate Limit for the :" + userId;

        Long currentCount = redisTemplate.opsForValue().increment(key);

        if(currentCount != null && currentCount == 1){
            redisTemplate.expire(key,keyExpiration);
        }

        return currentCount != null && currentCount <= reqLimit;
    }
}
