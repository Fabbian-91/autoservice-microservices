/*Scrip para creación de las bases de datos de los MS*/

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'usuarios_db')
   BEGIN
      CREATE DATABASE usuarios_db;
   END;

   GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'clientes_vehiculos_db')
    BEGIN
        CREATE DATABASE clientes_vehiculos_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'citas_db')
    BEGIN
        CREATE DATABASE citas_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'ordenes_db')
    BEGIN
        CREATE DATABASE ordenes_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'inventario_db')
    BEGIN
        CREATE DATABASE inventario_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'facturacion_db')
    BEGIN
        CREATE DATABASE facturacion_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'notificaciones_db')
    BEGIN
        CREATE DATABASE notificaciones_db;
    END;

    GO

IF NOT EXISTS (SELECT NAME FROM sys.databases WHERE name = 'historial_db')
    BEGIN
        CREATE DATABASE historial_db;
    END;

    GO