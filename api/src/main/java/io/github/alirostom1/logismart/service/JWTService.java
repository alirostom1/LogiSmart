package io.github.alirostom1.logismart.service;

import io.github.alirostom1.logismart.dto.response.common.TokenPair;
import io.github.alirostom1.logismart.model.entity.User;
import io.github.alirostom1.logismart.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.AeadAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.h2.command.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JWTService {

    @Value("${jwt.secret}")
    private String JWT_SECRET;

    @Value("${jwt.access.ttl}")
    private long JWT_ACCESS_TTL;

    @Value("${jwt.refresh.ttl}")
    private long JWT_REFRESH_TTL;

    private final UserRepo userRepo;


    public TokenPair generateTokenPair(User user){
        // ACCESS TOKEN GENERATION
        HashMap<String,Object> accessClaims = new HashMap<>();
        accessClaims.put("role",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("USER"));
        accessClaims.put("email",user.getEmail());
        accessClaims.put("type","access");
        String accessToken = buildToken(accessClaims,user.getId().toString(),JWT_ACCESS_TTL);

        // REFRESH TOKEN GENERATION
        HashMap<String,Object> refreshClaims = new HashMap<>();
        refreshClaims.put("role",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).findFirst().orElse("USER"));
        refreshClaims.put("email",user.getEmail());
        refreshClaims.put("type","refresh");
        String refreshToken = buildToken(refreshClaims,user.getId().toString(),JWT_REFRESH_TTL);

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(JWT_ACCESS_TTL)
                .refreshTokenExpiresIn(JWT_REFRESH_TTL)
                .build();
    }

    private String buildToken(Map<String,Object> extraClaims, String subject, long expiration){
        return Jwts.builder()
                .claims(extraClaims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long extractUserId(String token){
        return Long.valueOf(parseClaims(token).getSubject());
    }
    public Claims extractClaims(String token){
        return parseClaims(token);
    }

    private Key getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateAccessToken(String token){
        try{
            Claims claims = parseClaims(token);
            return claims.get("type").equals("access") && !isExpired(claims);
        }catch(Exception e){
            return false;
        }
    }
    public boolean validateRefreshToken(String token){
        try{
            Claims claims = parseClaims(token);
            return claims.get("type").equals("refresh") && !isExpired(claims);
        }catch (Exception e){
            return false;
        }
    }
}
