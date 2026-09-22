# Inventario Service

Microservicio de AutoService responsable de los repuestos, el stock disponible y los movimientos de entrada y salida.

## Ejecución local

La aplicación utiliza Java 21, Spring Boot y H2 en archivo.

```text
http://localhost:8085
```

Consola H2 de desarrollo:

```text
http://localhost:8085/h2-console
```

Salud del servicio:

```text
GET /actuator/health
```

## Endpoints de repuestos

```text
POST   /api/repuestos
GET    /api/repuestos
GET    /api/repuestos/{id}
PUT    /api/repuestos/{id}
DELETE /api/repuestos/{id}
```

## Endpoints de inventario

```text
POST /api/inventario/repuestos/{id}/entradas
POST /api/inventario/repuestos/{id}/salidas
GET  /api/inventario/repuestos/{id}/disponibilidad?cantidad=1
GET  /api/inventario/movimientos
GET  /api/inventario/repuestos/{id}/movimientos
GET  /api/inventario/ordenes/{ordenId}/movimientos
```

Una salida comprueba el stock dentro de una transacción y bloquea el registro del repuesto mientras se realiza el descuento. Así se evita usar más unidades que las disponibles incluso cuando llegan solicitudes simultáneas.
