package com.pblgllgs.sb3jwt.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class JwtUtils {
    private JwtUtils() {
    }

    private static final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    private static final String ISSUER = "auth_service";

    public static boolean validateToken(String jwtToken) {
        return Objects.requireNonNull(parseToken(jwtToken)).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken)
                    .getPayload());
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("JWT exception");
        }
        return Optional.empty();
    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
        Optional<Claims> claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(Claims::getSubject);
    }

    public static String generateToken(String username) {
        Date currentDate = new Date();
        Date expirationDate = DateUtils.addMinutes(currentDate, 30);
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .signWith(secretKey)
                .subject(username)
                .issuedAt(currentDate)
                .expiration(expirationDate)
                .compact();
    }
}
