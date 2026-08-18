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
        return "Acceso autorizado. Usuario autenticado: " + jwt.getSubject();
    }

    @PostMapping
    public String despedida(@AuthenticationPrincipal Jwt jwt) {
        return "Solicitud procesada correctamente para el usuario: " + jwt.getSubject();
    }

    @GetMapping("/public")
    public String endpointLibre() {
        return "endpoint sin validacion";
    }
}
