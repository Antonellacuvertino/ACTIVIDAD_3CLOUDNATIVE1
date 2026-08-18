# Fase 3: Docker, EC2 y API Gateway

## 1. Preparar el despliegue

Esta versión firma los JWT con `RS256`, publica la clave pública en `/oauth2/jwks` y expone el documento OIDC en `/.well-known/openid-configuration`. Es necesario porque el autorizador JWT de API Gateway solo admite algoritmos RSA y obtiene la clave pública desde `jwks_uri`.

Antes de desplegar, consigue un dominio para el IdP, por ejemplo `auth.tu-dominio.com`, y configúralo con HTTPS. El valor de `AUTH_ISSUER` debe ser exactamente esa URL, sin `/` al final.

## 2. EC2

1. Crea una instancia Ubuntu 24.04 o Amazon Linux 2023. Para la demostración sirve `t3.micro`.
2. En el Security Group permite SSH (22) solo desde tu IP. Para una prueba simple permite TCP 8081 y 8082 solo temporalmente desde tu IP; en la entrega final el 8082 debe recibir tráfico únicamente desde el balanceador/VPC Link.
3. Conéctate por SSH, instala Docker y el plugin Compose según la distribución.
4. Clona el repositorio y entra a su carpeta:

```bash
git clone https://github.com/Antonellacuvertino/ACTIVIDAD_3CLOUDNATIVE1.git
cd ACTIVIDAD_3CLOUDNATIVE1
```

5. Crea el archivo `.env` a partir del ejemplo y escribe el dominio HTTPS real:

```bash
cp .env.example .env
nano .env
docker compose up -d --build
docker compose ps
```

6. Comprueba desde la instancia:

```bash
curl $AUTH_ISSUER/.well-known/openid-configuration
curl $AUTH_ISSUER/oauth2/jwks
```

## 3. API Gateway HTTP API

1. En API Gateway selecciona **Create API** > **HTTP API**.
2. Crea la integración HTTP hacia la URL pública HTTPS del servicio de negocio: `https://business.tu-dominio.com`.
3. Crea las rutas `GET /api/v1` y `POST /api/v1` con esa integración. Crea `GET /api/v1/public` sin autorizador.
4. En **Authorization** > **Create and manage authorizers**, crea un autorizador JWT:
   - Identity source: `$request.header.Authorization`
   - Issuer URL: `https://auth.tu-dominio.com`
   - Audience: `business-api`
5. Adjunta el autorizador a `GET /api/v1` y `POST /api/v1`; en ambos configura el scope `api.read`.
6. Crea el stage `$default` y copia el Invoke URL.

## 4. Prueba final con Postman

1. Solicita el token en `POST https://auth.tu-dominio.com/api/auth/login` con el usuario `estudiante` y contraseña `clave123`.
2. En Tests guarda el valor: `pm.environment.set("accessToken", pm.response.json().accessToken);`.
3. Llama `GET https://<api-id>.execute-api.<region>.amazonaws.com/api/v1` con `Authorization: Bearer {{accessToken}}`.
4. Sin token, la misma ruta debe responder `401`. La ruta `/api/v1/public` debe responder `200` sin token.

## Nota de arquitectura

Para una entrega académica puede usarse una integración HTTP pública. Para evitar que alguien salte el Gateway llamando directo a EC2, usa un Application Load Balancer interno y un VPC Link de API Gateway; así `business-service` queda en una subred privada y solo API Gateway puede alcanzarlo.
