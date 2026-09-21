#!/usr/bin/env bash

set -euo pipefail

for database in clientes_vehiculos_db citas_db notificaciones_db historial_db; do
  /opt/mssql-tools18/bin/sqlcmd \
    -C \
    -S "${SQLSERVER_HOST:-sqlserver}" \
    -U sa \
    -P "${MSSQL_SA_PASSWORD}" \
    -Q "IF DB_ID(N'${database}') IS NULL CREATE DATABASE [${database}];" \
    -b
done
