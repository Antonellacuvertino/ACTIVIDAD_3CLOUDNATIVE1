package com.example.actividad3.auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OidcController {
    private final RSAKey rsaKey;
    private final String issuer;

    public OidcController(RSAKey rsaKey, @Value("${app.issuer}") String issuer) {
        this.rsaKey = rsaKey;
        this.issuer = issuer;
    }

    @GetMapping("/.well-known/openid-configuration")
    public Map<String, Object> discovery() {
        return Map.of("issuer", issuer, "jwks_uri", issuer + "/oauth2/jwks",
                "token_endpoint", issuer + "/api/auth/login", "id_token_signing_alg_values_supported", List.of("RS256"));
    }

    @GetMapping("/oauth2/jwks")
    public Map<String, Object> jwks() {
        return new JWKSet(rsaKey.toPublicJWK()).toJSONObject();
    }
}
