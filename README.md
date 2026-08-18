# Actividad 3 - OIDC, OAuth 2.0 y AWS

Proyecto organizado en dos microservicios Spring Boot:

- **auth-service (8081):** valida usuarios simulados, firma JWT con RS256 y publica OIDC discovery/JWKS.
- **business-service (8082):** funciona como OAuth 2.0 Resource Server y valida los JWT mediante la clave pública del IdP.

Los DTO están en el paquete `dto`, el usuario simulado en `model` y la generación de tokens en `JwtService`. RS256 permite que API Gateway valide el token con la clave pública, sin exponer la clave privada.

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

## Fase 3

Los `Dockerfile` y `docker-compose.yml` permiten contenerizar los servicios. La guía completa para EC2 y API Gateway está en [docs/FASE_3_AWS.md](docs/FASE_3_AWS.md).
