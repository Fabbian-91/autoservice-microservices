# AutoService

AutoService reúne los servicios usados para administrar un taller mecánico.
Está hecho con Java y Spring Boot y se divide en microservicios.

## Funcionalidades

- Gestión de usuarios y roles.
- Registro de clientes y vehículos.
- Administración de citas.
- Gestión de órdenes de trabajo.
- Control de trabajos y repuestos utilizados.
- Gestión de inventario.
- Generación de facturas.
- Notificaciones mediante RabbitMQ.
- Historial y auditoría mediante Apache Kafka.
- Seguridad mediante JWT.
- API Gateway como punto único de acceso.

## Tecnologías

- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- JWT
- Spring Cloud Gateway
- OpenFeign
- RabbitMQ
- Apache Kafka
- Maven
- H2 / MySQL / PostgreSQL
- Docker

## Arquitectura

El sistema utiliza una arquitectura de microservicios donde cada servicio
es independiente y posee su propia base de datos.

La comunicación entre servicios se realiza mediante:

- **OpenFeign:** comunicación síncrona.
- **RabbitMQ:** procesamiento de tareas asíncronas.
- **Apache Kafka:** publicación y consumo de eventos.

## Servicios

- Gateway
- Usuarios
- Clientes y Vehículos
- Citas
- Órdenes de Trabajo
- Inventario
- Facturación
- Notificaciones
- Historial

## Objetivo

El flujo cubre el seguimiento de un vehículo desde la creación de la cita
hasta la reparación, la facturación y la entrega al cliente.

## Usuarios y seguridad

Esta rama agrega `usuarios-service`. El servicio usa la base de datos
`usuarios_db`, emite JWT compatibles con el Gateway y expone las operaciones
de registro, consulta, edición, roles, desactivación y validación de tokens.

## Integración

La guía de conexión local y Docker está en [`docs/integracion.md`](docs/integracion.md).
Incluye las variables de entorno, la red compartida, los comandos de arranque y
las decisiones pendientes para integrar órdenes y facturación.
