package com.bank.auth.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bank.user.User;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class JWTService {
    private final String secret;

    public JWTService(String secret) {
        this.secret = secret;
    }

    // generates a jwt token that expires after 15 mins
    /**
     * @author Derek Homel
     * @summary
     * @param user the user that requests the token to be generated
     * @return returns a signed JWT token that expires after 15 minutes
     */
    public String generateToken(User user) {
        return JWT.create()
                .withSubject(user.getUserID().toString())
                .withClaim("role", user.getRole().name())
                .withClaim("username", user.getUsername())
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plus(15,
                        ChronoUnit.MINUTES))
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * @author Derek Homel
     * @summary
     * @param token the token to verify
     * @return returns a VerificationBuilder using the specified algorithm to
     * verify the token
     * @throws JWTVerificationException throws if JWTVerification failed
     */
    // validates jwt token is the correct token
    public DecodedJWT verifyToken(String token) throws JWTVerificationException {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }
}
