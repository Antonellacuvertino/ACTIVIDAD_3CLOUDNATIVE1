# Actividad 3 - JWT y OAuth 2.0

Proyecto organizado en dos microservicios Spring Boot:

- **auth-service (8081):** valida usuarios simulados y genera JWT firmados con HS256.
- **business-service (8082):** funciona como OAuth 2.0 Resource Server y valida los JWT mediante `NimbusJwtDecoder`.

Los DTO están en el paquete `dto`, el usuario simulado en `model` y la generación de tokens en `JwtService`. Ambos servicios usan la misma propiedad `jwt.secret`, tal como indica la guía de la actividad.

## Ejecutar localmente

En dos terminales, desde la carpeta raíz:

```powershell
.\gradlew.bat :auth-service:bootRun
.\gradlew.bat :business-service:bootRun
```

Usuarios de prueba:

| Usuario | Contraseña | Permisos |
|---|---|---|
| estudiante | clave123 | `products:read`, rol `USER` |
| admin | admin123 | `products:read products:write`, rol `ADMIN` |

Solicitar token:

```powershell
$body = @{ username = 'estudiante'; password = 'clave123' } | ConvertTo-Json
$token = (Invoke-RestMethod -Method Post -Uri http://localhost:8081/api/auth/login -ContentType 'application/json' -Body $body).accessToken
```

Consumir el recurso protegido:

```powershell
Invoke-RestMethod -Uri http://localhost:8082/api/v1 -Headers @{ Authorization = "Bearer $token" }
```

## Endpoints importantes

- `POST /api/auth/login`: autenticación y entrega del `accessToken`.
- `GET /api/v1/public`: endpoint libre, no requiere token.
- `GET /api/v1`: endpoint protegido, responde el usuario obtenido desde el JWT.
- `POST /api/v1`: endpoint protegido, responde el usuario obtenido desde el JWT.

> Nota: para enfocarse en la actividad, los usuarios se mantienen en memoria. En una aplicación real, la clave secreta no se compartiría en texto plano; se usarían variables de entorno y un IdP con claves asimétricas.
