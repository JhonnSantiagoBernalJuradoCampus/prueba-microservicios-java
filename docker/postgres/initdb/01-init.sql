CREATE DATABASE products_db;
CREATE DATABASE inventory_db;

-- Opcional: crear extensiones comunes en ambas
\connect products_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

\connect inventory_db;
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
