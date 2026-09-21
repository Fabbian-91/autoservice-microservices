# Integración del entorno

## Objetivo

El Gateway es el único punto de entrada de la aplicación. Los clientes consumen las rutas de `gateway-service` en el puerto `8080`; los microservicios se comunican entre sí usando variables de entorno y no valores fijos dentro del código.

## Configuración local

1. Copiar `.env.example` a `.env` y cambiar las claves de ejemplo.
2. Levantar la infraestructura compartida:

   ```bash
   docker compose up -d rabbitmq kafka kafka-ui
   ```

3. Iniciar las aplicaciones con Maven desde cada módulo. En local, los valores por defecto apuntan a `localhost`.

## Configuración en Docker

Dentro de la red `autoservice-net` no se debe usar `localhost` para alcanzar otro contenedor. Se deben configurar los nombres de servicio:

```dotenv
USUARIOS_SERVICE_URL=http://usuarios-service:8081
CLIENTES_VEHICULOS_SERVICE_URL=http://clientes-vehiculos-service:8082
CITAS_SERVICE_URL=http://citas-service:8083
ORDENES_SERVICE_URL=http://ms-ordenes-core:8084
INVENTARIO_SERVICE_URL=http://inventario-service:8085
FACTURACION_SERVICE_URL=http://facturacion-service:8086
NOTIFICACIONES_SERVICE_URL=http://notificaciones-service:8087
HISTORIAL_SERVICE_URL=http://historial-service:8088
RABBITMQ_HOST=rabbitmq
KAFKA_BOOTSTRAP_SERVERS=kafka:29092
```

El Gateway expone el puerto `8080`. Los puertos de los microservicios deben quedar internos en el despliegue y publicarse solo cuando se necesiten para pruebas.

### Despliegue completo y evidencias

Para probar la seguridad sin ocupar el `api-gateway` que ya esté ejecutándose en el equipo, el Compose completo publica el Gateway en el puerto `18000`:

```bash
./mvnw -q -DskipTests package
docker compose up -d --build
docker compose ps
```

Con el `docker-compose.yml` del proyecto quedan conectados el Gateway, los ocho microservicios disponibles y sus dependencias de SQL Server, MySQL, RabbitMQ, Kafka y H2. El servicio `sqlserver-init` crea automáticamente las bases de datos necesarias antes de iniciar los microservicios que usan SQL Server. No depende de contenedores ni redes creados previamente. La colección de Postman usa `http://localhost:18000` como `baseUrl` para este despliegue.

El SQL Server se publica en `localhost:11433` para consultas manuales. La clave de `sa` se configura mediante `DB_PASSWORD` en `.env`; si no se define, Compose utiliza el valor de desarrollo indicado en el archivo.

```bash
cp .env.example .env
docker compose up -d --build
```

Los Dockerfiles compilan cada microservicio dentro de su propia imagen; no es necesario tener artefactos `target/` generados antes de ejecutar Compose.

El módulo `ms-ordenes-core` de `develop` concentra las rutas de órdenes, trabajos, mecánicos y repuestos en el puerto `8084`. El Gateway enruta todos esos endpoints al mismo microservicio.

## Seguridad

- `usuarios-service` genera y valida JWT usando la clave definida en `JWT_SECRET`.
- `gateway-service` valida el token antes de enrutar y propaga los datos del usuario en `X-User-Email`, `X-User-Role` y `X-User-Id`.
- No se deben versionar `.env` ni credenciales reales. El archivo `.env.example` solo contiene nombres y valores de referencia.
- Las rutas públicas son login, registro, actuator y health check. El resto requiere `Bearer token`.

## Facturación

`facturacion-service` forma parte del stack y se levanta en el puerto `8086` dentro de la red Docker. Consulta las órdenes en `ms-ordenes-core`, publica eventos en Kafka y notifica facturas generadas mediante RabbitMQ.

## Verificación mínima

```bash
./mvnw -q test
curl http://localhost:8080/actuator/health
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"usuario":"admin@autoservice.local","password":"admin123"}'
```

Las pruebas de endpoints protegidos deben ejecutarse usando el token devuelto por el login.

## Colección de Postman

Importar [`AutoService-Gateway.postman_collection.json`](AutoService-Gateway.postman_collection.json).
La variable `baseUrl` viene configurada para el Gateway completo en
`http://localhost:18000`; si se levanta en el puerto normal, cambiarla a
`http://localhost:8080`.

Orden recomendado:

1. `00 - Salud y disponibilidad`.
2. `01 - Autenticación > Login administrador - guarda JWT`.
3. Ejecutar las carpetas protegidas usando los IDs guardados en las variables.
4. Ejecutar `12 - Casos de seguridad y error` para las evidencias de 401, 400 y 404.
