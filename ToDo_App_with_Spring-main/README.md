# ToDo App with Spring

Este proyecto es una aplicación de tareas (ToDo) construida con Spring Boot, Spring Security, JPA, Thymeleaf y H2 en memoria.

## Cómo arrancar

1. Instala Java 17 y configura el `JAVA_HOME`.
2. En el directorio del proyecto:
   ```bash
   ./mvnw clean package
   ./mvnw spring-boot:run
   ```
3. Abre en el navegador:
   ```text
   http://localhost:8080
   ```

## Login y registro

- Página de login: `/login`
- Página de registro: `/auth/register`
- Registro de usuario: POST `/auth/register/submit`

El formulario de login acepta:
- `username` (nombre de usuario o email)
- `password`

## Usuarios iniciales cargados al arrancar

El proyecto crea automáticamente dos usuarios en memoria:

1. Usuario normal:
   - `username`: `user`
   - `email`: `user@user.com`
   - `password`: `1234`
   - `fullname`: `The user`
   - rol: `USER`

2. Administrador:
   - `username`: `admin`
   - `email`: `admin@openwebinars.net`
   - `password`: `1234`
   - `fullname`: `Administrador`
   - rol: `ADMIN`

## Seguridad

- Las rutas públicas son:
  - `/login`
  - `/logout`
  - `/auth/register`
  - `/auth/register/submit`
  - `/h2-console/**`
  - `/img/**`
  - `/css/**`
- El resto de rutas requiere autenticación.
- Los roles se gestionan mediante `ROLE_USER` y `ROLE_ADMIN`.
