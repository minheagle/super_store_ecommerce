package com.shopee.clone.util;

import com.shopee.clone.entity.RefreshTokenEntity;
import com.shopee.clone.repository.RefreshTokenRepository;
import com.shopee.clone.repository.UserRepository;
import com.shopee.clone.security.impl.UserDetailImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class JWTProvider {
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwt.key.secret}")
    private String secret;

    @Value("${app.jwt.access_token.expiration.milliseconds}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh_token.expiration.milliseconds}")
    private long refreshTokenExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserDetailImpl userPrincipal = (UserDetailImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + accessTokenExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateJwtTokenFromUserName(String userName){
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + accessTokenExpiration))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    @Transactional
    public RefreshTokenEntity createRefreshToken(Long userId){
        RefreshTokenEntity refreshToken = RefreshTokenEntity
                                            .builder()
                                            .user(userRepository.findById(userId).get())
                                            .expiryDatetime(Instant.now().plusMillis(refreshTokenExpiration))
                                            .refreshToken(UUID.randomUUID().toString())
                                            .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional
    public RefreshTokenEntity verifyRefreshTokenExpiration(RefreshTokenEntity refreshToken){
        if(refreshToken.getExpiryDatetime().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh Token expired !");
        }
        return refreshToken;
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken){
        refreshTokenRepository.deleteByRefreshToken(refreshToken);
    }
}
