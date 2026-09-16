# AutoService - Usuarios y Seguridad

Proyecto independiente correspondiente a la parte de **Yesenia** en la transcripción del profesor Walter Alpizar.

## Alcance

- Registrar usuarios con contraseña cifrada mediante BCrypt.
- Iniciar sesión y emitir JWT.
- Consultar y actualizar usuarios.
- Cambiar roles y desactivar usuarios con baja lógica.
- Exponer `POST /api/usuarios/validar-token` para que el Gateway valide un token.
- Mantener una base de datos propia (`usuarios_db`), separada de los demás servicios.

## Ejecución

```bash
cd usuarios-service
cp .env.example .env
mvn spring-boot:run
```

Para ejecutar con MySQL:

```bash
mvn clean package -DskipTests
docker compose up --build
```

Servicio: `http://localhost:8081` (el Gateway de la rama base lo consume en este puerto)

## Usuarios iniciales

| Correo | Contraseña | Rol |
| --- | --- | --- |
| `admin@autoservice.local` | `admin123` | `ADMINISTRADOR` |
| `recepcion@autoservice.local` | `recepcion123` | `RECEPCION` |
| `mecanico@autoservice.local` | `mecanico123` | `MECANICO` |
| `facturacion@autoservice.local` | `facturacion123` | `FACTURACION` |

Las contraseñas se almacenan únicamente como hash BCrypt.

## Endpoints principales

| Método | Ruta | Permiso |
| --- | --- | --- |
| POST | `/api/usuarios/login` | Público |
| POST | `/api/auth/login` | Público; alias para el Gateway |
| POST | `/api/auth/register` | Público; alias para el Gateway |
| POST | `/api/usuarios` | ADMINISTRADOR |
| GET | `/api/usuarios` | ADMINISTRADOR |
| GET | `/api/usuarios/{id}` | ADMINISTRADOR |
| PUT | `/api/usuarios/{id}` | ADMINISTRADOR |
| PATCH | `/api/usuarios/{id}/rol?rol=RECEPCION` | ADMINISTRADOR |
| PATCH | `/api/usuarios/{id}/desactivar` | ADMINISTRADOR |
| POST | `/api/usuarios/validar-token` | Interno |

El proyecto se puede integrar después con el Gateway del trabajo grupal. El endpoint de validación usa el mismo contrato de JWT y está pensado para el consumo interno del Gateway.
