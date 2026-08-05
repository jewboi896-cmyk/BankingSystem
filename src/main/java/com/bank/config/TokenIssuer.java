package com.bank.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bank.user.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class TokenIssuer {
    private final Algorithm algorithm;

    public TokenIssuer(SecretKey key) {
        this.algorithm = Algorithm.HMAC256(key.getEncoded());
    }

    // generates a JWT Token that expires after 15 minutes
    public String generateToken(@NotNull User user) {
        return JWT.create().withSubject(user.getUserID().toString()).withClaim("role", user.getRole().name())
                .withClaim("username", user.getUsername()).withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15, ChronoUnit.MINUTES)).sign(algorithm);
    }
}
