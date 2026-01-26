-- dcl.sql - creacion de usuario y base de datos
-- proyecto asur - asociacion de sordos del uruguay

-- crear usuario
CREATE USER PROYECTO WITH PASSWORD 'proyecto123';

-- crear base de datos
CREATE DATABASE db_asur OWNER PROYECTO;

-- conectar a la base
\c db_asur

-- crear schema
CREATE SCHEMA IF NOT EXISTS proyecto AUTHORIZATION PROYECTO;

-- permisos
GRANT ALL PRIVILEGES ON SCHEMA proyecto TO PROYECTO;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA proyecto TO PROYECTO;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA proyecto TO PROYECTO;

-- establecer schema por defecto
ALTER USER PROYECTO SET search_path TO proyecto;

