package com.example.actividad3.business;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HolaMundoV1Controller {
    @GetMapping
    public String holaMundo(@AuthenticationPrincipal Jwt jwt) {
        return "hola mundo v1.0.0 - bug corregido v1.1.1 " + jwt.getSubject();
    }

    @PostMapping
    public String despedida(@AuthenticationPrincipal Jwt jwt) {
        return "despedida V1.1.0 " + jwt.getSubject();
    }

    @GetMapping("/public")
    public String endpointLibre() {
        return "endpoint sin validacion";
    }
}
