package com.example.actividad3.auth;

import com.example.actividad3.auth.model.LocalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final Key signingKey;

    public JwtService(@Value("${jwt.secret}") String secretString) {
        this.signingKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(LocalUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.username())
                .claim("role", user.role())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(900)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
