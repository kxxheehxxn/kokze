package org.ozea.security.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ozea.security.util.blacklist.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtProcessor {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long tokenExpiration;

    @Value("${jwt.refresh-expiration:86400000}")
    private long refreshExpiration;

    @Value("${jwt.issuer:ozea}")
    private String issuer;

    @Value("${jwt.audience:ozea-users}")
    private String audience;

    private final TokenBlacklistService blacklistService;

    private SecretKey getSigningKey() {
        if (secretKey == null || secretKey.trim().isEmpty()) {
            throw new IllegalStateException("JWT secret key is not configured");
        }
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException("JWT secret key must be at least 256 bits (32 bytes)");
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(String username) {
        return generateToken(username, tokenExpiration, "access", true);
    }

    public String generateRefreshToken(String username) {
        return generateToken(username, refreshExpiration, "refresh", false);
    }

    private String generateToken(String username, long expirationMs, String type, boolean includeAudience) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMs);

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", type);
        claims.put("username", username);
        claims.put("jti", UUID.randomUUID().toString());

        JwtBuilder builder = Jwts.builder()
                .header()               // header builder 열고
                .type(Header.JWT_TYPE)  // typ: JWT
                .and()                  // 다시 main builder로
                .claims(claims)
                .subject(username)
                .issuer(issuer)
                .issuedAt(now)
                .expiration(exp);

        if (includeAudience) {
            builder.audience().add(audience).and();
        }

        return builder
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private JwtParser baseParser() {
        return Jwts.parser()                      // 예전 parserBuilder() → 이제 parser()
                .verifyWith(getSigningKey())      // 예전 setSigningKey() 대체
                .requireIssuer(issuer)
                .clockSkewSeconds(120)            // 예전 setAllowedClockSkewSeconds(...)
                .build();
    }

    public String getUsernameAllowExpired(String token) {
        if (token == null || token.trim().isEmpty()) return null;
        try {
            Claims c = baseParser()
                    .parseSignedClaims(token)     // 예전 parseClaimsJws → parseSignedClaims
                    .getPayload();
            String sub = c.getSubject();
            return (sub != null) ? sub : c.get("username", String.class);
        } catch (ExpiredJwtException e) {
            // 만료된 토큰은 여기서 claims를 꺼낼 수 있음
            Claims c = e.getClaims();
            if (c == null) return null;
            String sub = c.getSubject();
            return (sub != null) ? sub : c.get("username", String.class);
        } catch (JwtException e) {
            log.warn("JWT getUsernameAllowExpired fail: {}", e.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) return false;
        try {
            Claims c = baseParser()
                    .parseSignedClaims(token)
                    .getPayload();
            if ("access".equals(c.get("type", String.class))) {
                String aud = c.getAudience().toString();
                if (!audience.equals(aud)) {
                    log.warn("[JWT FAIL] audience mismatch exp={} act={}", audience, aud);
                    return false;
                }
            }
            return true;
        } catch (ExpiredJwtException e) {
            log.warn("[JWT FAIL] expired exp={} now={}", e.getClaims().getExpiration(), new Date());
        } catch (SignatureException e) {
            log.warn("[JWT FAIL] bad signature");
        } catch (MissingClaimException | IncorrectClaimException e) {
            log.warn("[JWT FAIL] claim issue {} {}", e.getClass().getSimpleName(), e.getClaimName());
        } catch (JwtException e) {
            log.warn("[JWT FAIL] {}", e.getMessage());
        }
        return false;
    }

    public String getUsername(String token) {
        try {
            Claims c = baseParser()
                    .parseSignedClaims(token)
                    .getPayload();
            String sub = c.getSubject();
            return sub != null ? sub : c.get("username", String.class);
        } catch (JwtException e) {
            log.warn("JWT getUsername fail: {}", e.getMessage());
            return null;
        }
    }

    public String getTokenType(String token) {
        try {
            return baseParser()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("type", String.class);
        } catch (JwtException e) {
            log.warn("JWT getTokenType fail: {}", e.getMessage());
            return null;
        }
    }

    public String getJti(String token) {
        try {
            return baseParser()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get("jti", String.class);
        } catch (JwtException e) {
            log.warn("JWT getJti fail: {}", e.getMessage());
            return null;
        }
    }

    public Date getExpirationDate(String token) {
        try {
            return baseParser()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();
        } catch (JwtException e) {
            log.warn("JWT getExpirationDate fail: {}", e.getMessage());
            return null;
        }
    }

    public long getRemainingTime(String token) {
        Date exp = getExpirationDate(token);
        return exp == null ? 0 : Math.max(0, exp.getTime() - System.currentTimeMillis());
    }

    public boolean isTokenBlacklisted(String token) {
        String jti = getJti(token);
        return jti != null && blacklistService.isBlacklisted(jti);
    }

    public void blacklistToken(String token) {
        String jti = getJti(token);
        long ttlMs = getRemainingTime(token);
        if (jti != null && ttlMs > 0) {
            blacklistService.blacklist(jti, ttlMs);
            log.info("blacklist jti={} ttlMs={}", jti, ttlMs);
        }
    }
}