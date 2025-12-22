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
import java.util.List;
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
    private final TokenBlacklistService blacklistService;


    public TokenPair generateTokenPair(User user,String familyId){
        // ACCESS TOKEN GENERATION
        HashMap<String,Object> accessClaims = new HashMap<>();
        String role = user.getAuthorities().stream()
                .filter(auth -> auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("ROLE_USER");
        accessClaims.put("role", role);
        accessClaims.put("email",user.getEmail());
        accessClaims.put("type","access");
        
        // Add permissions to token
        List<String> permissions = user.getAuthorities().stream()
                .filter(auth -> !auth.getAuthority().startsWith("ROLE_"))
                .map(GrantedAuthority::getAuthority)
                .toList();
        accessClaims.put("permissions", permissions);
        
        String accessToken = buildToken(accessClaims,user.getId().toString(),JWT_ACCESS_TTL);

        // REFRESH TOKEN GENERATION
        String family = familyId != null ? familyId : UUID.randomUUID().toString();
        String jti = UUID.randomUUID().toString();
        HashMap<String,Object> refreshClaims = new HashMap<>();
        refreshClaims.put("role", role);
        refreshClaims.put("email",user.getEmail());
        refreshClaims.put("family",family);
        refreshClaims.put("jti",jti);
        refreshClaims.put("type","refresh");
        String refreshToken = buildToken(refreshClaims,user.getId().toString(),JWT_REFRESH_TTL);
        blacklistService.rotateRefreshToken(user.getId(),family,jti);

        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .accessTokenExpiresIn(JWT_ACCESS_TTL)
                .refreshTokenExpiresIn(JWT_REFRESH_TTL)
                .build();
    }
    public User extractUser(String token){
        Long userId = extractUserId(token);
        return userRepo.findById(userId)
                .orElseThrow(() -> new JwtException("User in token doesnt exist anymore!"));
    }

    public TokenPair rotateRefreshToken(String refreshToken,User user){
        validateRefreshToken(refreshToken);
        Claims claims = extractClaims(refreshToken);
        String family = claims.get("family", String.class);
        return generateTokenPair(user,family);
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
    public void validateRefreshToken(String token){
        try{
            Claims claims = parseClaims(token);
            if(!claims.get("type").equals("refresh") || isExpired(claims)){
                throw new JwtException("Invalid or expired token!");
            }
            Long userId = extractUserId(token);
            String family = claims.get("family",String.class);
            String jti = claims.get("jti",String.class);
            blacklistService.validateRefreshTokenIsStillActive(userId,family,jti);
        }catch (Exception e){
            throw new JwtException("Invalid token");
        }
    }
}
