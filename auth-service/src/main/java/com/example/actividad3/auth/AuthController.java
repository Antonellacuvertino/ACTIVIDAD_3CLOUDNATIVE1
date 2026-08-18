package com.example.actividad3.auth;

import com.example.actividad3.auth.dto.LoginRequest;
import com.example.actividad3.auth.dto.TokenResponse;
import com.example.actividad3.auth.model.LocalUser;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, LocalUser> users;

    public AuthController(JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.users = Map.of(
                "estudiante", new LocalUser("estudiante", passwordEncoder.encode("clave123"), "USER"),
                "admin", new LocalUser("admin", passwordEncoder.encode("admin123"), "ADMIN"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LocalUser user = users.get(request.username());
        if (user == null || !passwordEncoder.matches(request.password(), user.password())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "invalid_credentials", "message", "Usuario o contraseña inválidos"));
        }
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(user), "Bearer", 900));
    }

}
