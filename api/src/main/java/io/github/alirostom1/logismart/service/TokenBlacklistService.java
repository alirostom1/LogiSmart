package io.github.alirostom1.logismart.service;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.sqm.TemporalUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService{
    private final RedisTemplate<String ,String> redisTemplate;

    @Value("${jwt.refresh.ttl}")
    private long JWT_REFRESH_TTL;

    private static final String ACTIVE_PREFIX="refresh:active:"; // refresh:active:{userId}:{family} -> jti
    private static final String BLACKLIST_PREFIX="refresh:blacklist:"; // refresh:blacklist:{userId}:{family}

    public void validateRefreshTokenIsStillActive(Long userId,String family,String jti){
        String activeKey = ACTIVE_PREFIX + userId + ":" + family;
        String blacklistKey = BLACKLIST_PREFIX + userId + ":" + family;
        if(redisTemplate.hasKey(blacklistKey)){
            throw new JwtException("Session has been blacklisted");
        }
        String currentJti = redisTemplate.opsForValue().get(activeKey);
        if(!jti.equals(currentJti)){
            revokeAllFamilies(userId);
            redisTemplate.delete(activeKey);
            throw new JwtException("Refresh token reuse detected!");
        }
    }
    public void rotateRefreshToken(Long userId,String family,String newJti){
        String activeKey = ACTIVE_PREFIX + userId + ":" + family;
        redisTemplate.opsForValue().set(activeKey,newJti,Duration.ofSeconds(JWT_REFRESH_TTL));
    }
    public void revokeFamily(Long userId,String family){
        String activeKey = ACTIVE_PREFIX + userId + ":" + family;
        String blacklistKey = BLACKLIST_PREFIX + userId + ":" + family;
        redisTemplate.delete(activeKey);
        redisTemplate.opsForValue().set(blacklistKey,"1",Duration.ofSeconds(JWT_REFRESH_TTL));
    }
    public void revokeAllFamilies(Long userId){
        Set<String> keys = redisTemplate.keys(ACTIVE_PREFIX + userId + ":*");
        for(String key : keys){
            String family = key.substring(key.lastIndexOf(":") + 1);
            revokeFamily(userId,family);
        }
    }

}
