package com.bank.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
public class JwtSecretConfig {

    @Bean
    public SecretKey jwtSecretKey(@Value("${bank.jwt.secret}") String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("BANK_JWT_SECRET must be set and at least 32 characters long. Set it via the " +
                    "BANK_JWT_SECRET environment variable.");
        }
        return new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    }
}
