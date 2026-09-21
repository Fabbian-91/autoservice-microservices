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
ORDENES_SERVICE_URL=http://ordenes-service:8084
INVENTARIO_SERVICE_URL=http://inventario-service:8085
FACTURACION_SERVICE_URL=http://facturacion-service:8086
NOTIFICACIONES_SERVICE_URL=http://notificaciones-service:8087
HISTORIAL_SERVICE_URL=http://historial-service:8088
RABBITMQ_HOST=rabbitmq
KAFKA_BOOTSTRAP_SERVERS=kafka:29092
```

El Gateway expone el puerto `8080`. Los puertos de los microservicios deben quedar internos en el despliegue y publicarse solo cuando se necesiten para pruebas.

### Gateway de evidencias

Para probar la seguridad sin ocupar el `api-gateway` que ya esté ejecutándose en el equipo, se puede usar el puerto `18080`:

```bash
bash gateway-service/mvnw -q -f gateway-service/pom.xml -DskipTests package
docker build -t autoservice-gateway-evidence:latest gateway-service
docker run -d --name autoservice-gateway-evidence \
  --network autoservice-usuarios-evidence \
  -p 18080:8080 \
  -e JWT_SECRET="$JWT_SECRET" \
  -e USUARIOS_SERVICE_URL=http://autoservice-usuarios-evidence:8081 \
  autoservice-gateway-evidence:latest
```

Con este contenedor quedan conectados el Gateway y `usuarios-service`. Las demás rutas aparecen como no disponibles hasta que sus microservicios se levanten en la misma red. La colección de Postman ya usa `http://localhost:18080` como `baseUrl` para estas evidencias.

## Seguridad

- `usuarios-service` genera y valida JWT usando la clave definida en `JWT_SECRET`.
- `gateway-service` valida el token antes de enrutar y propaga los datos del usuario en `X-User-Email`, `X-User-Role` y `X-User-Id`.
- No se deben versionar `.env` ni credenciales reales. El archivo `.env.example` solo contiene nombres y valores de referencia.
- Las rutas públicas son login, registro, actuator y health check. El resto requiere `Bearer token`.

## Pendientes de integración

Antes de levantar todo el sistema en un único Compose se deben resolver dos decisiones del repositorio:

1. `ms-ordenes-core` y `ordenes-service` usan el mismo puerto `8084` y comparten rutas `/api/ordenes`. Debe definirse cuál será el módulo principal o separar claramente sus responsabilidades y puertos.
2. El Gateway tiene una ruta para facturación, pero `facturacion-service` todavía no forma parte de `develop`. La rama debe integrarse cuando el servicio tenga su implementación completa.

## Verificación mínima

```bash
./mvnw -q test
curl http://localhost:8080/actuator/health
curl -X POST http://localhost:8080/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"correo":"admin@autoservice.local","contrasena":"admin123"}'
```

Las pruebas de endpoints protegidos deben ejecutarse usando el token devuelto por el login.

## Colección de Postman

Importar [`AutoService-Gateway.postman_collection.json`](AutoService-Gateway.postman_collection.json).
La variable `baseUrl` viene configurada para el Gateway de evidencias en
`http://localhost:18080`; si se levanta en el puerto normal, cambiarla a
`http://localhost:8080`.

Orden recomendado:

1. `00 - Salud y disponibilidad`.
2. `01 - Autenticación > Login administrador - guarda JWT`.
3. Ejecutar las carpetas protegidas usando los IDs guardados en las variables.
4. Ejecutar `11 - Casos de seguridad y error` para las evidencias de 401, 400 y 404.
