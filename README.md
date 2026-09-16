# AutoService

AutoService es un sistema de gestión para talleres mecánicos desarrollado con
Java y Spring Boot bajo una arquitectura de microservicios.

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

Centralizar y automatizar los principales procesos de un taller mecánico,
facilitando el seguimiento de vehículos desde la creación de una cita hasta
la reparación, facturación y entrega al cliente.

## Usuarios y seguridad

Esta rama agrega `usuarios-service`. El servicio usa la base de datos
`usuarios_db`, emite JWT compatibles con el Gateway y expone las operaciones
de registro, consulta, edición, roles, desactivación y validación de tokens.
