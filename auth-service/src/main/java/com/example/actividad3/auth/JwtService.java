package com.example.actividad3.auth;

import com.example.actividad3.auth.model.LocalUser;
import com.nimbusds.jose.jwk.RSAKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.security.interfaces.RSAPrivateKey;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final RSAKey rsaKey;
    private final RSAPrivateKey signingKey;
    private final String issuer;

    public JwtService(RSAKey rsaKey, RSAPrivateKey signingKey, @Value("${app.issuer}") String issuer) {
        this.rsaKey = rsaKey;
        this.signingKey = signingKey;
        this.issuer = issuer;
    }

    public String generateToken(LocalUser user) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(user.username())
                .claim("role", user.role())
                .claim("scope", "api.read")
                .setIssuer(issuer)
                .setAudience("business-api")
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(900)))
                .setHeaderParam("kid", rsaKey.getKeyID())
                .signWith(signingKey, SignatureAlgorithm.RS256)
                .compact();
    }
}
