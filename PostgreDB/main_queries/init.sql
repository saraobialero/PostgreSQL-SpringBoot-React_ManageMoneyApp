-- Script per la creazione dei database e degli utenti
-- Da eseguire come utente postgres o un utente con privilegi di creazione DB

-- Creazione utente applicativo con password sicura
CREATE USER money_manager WITH PASSWORD 'tvt6YUs7C7eC6k';

-- Database Development
CREATE DATABASE managemoney_dev
    WITH
    OWNER = money_manager
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE template0;
COMMENT ON DATABASE managemoney_dev IS 'Development environment for ManageMoney application';

-- Database Test
CREATE DATABASE managemoney_test
    WITH
    OWNER = money_manager
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE template0;
COMMENT ON DATABASE managemoney_test IS 'Test environment for ManageMoney application';

-- Database Production
CREATE DATABASE managemoney_prod
    WITH
    OWNER = money_manager
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    TEMPLATE template0;
COMMENT ON DATABASE managemoney_prod IS 'Production environment for ManageMoney application';

-- Connettiti a ciascun database e imposta gli schema e i privilegi
\c managemoney_dev
CREATE SCHEMA IF NOT EXISTS managemoney AUTHORIZATION money_manager;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA managemoney TO money_manager;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA managemoney TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT ALL ON TABLES TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT ALL ON SEQUENCES TO money_manager;

\c managemoney_test
CREATE SCHEMA IF NOT EXISTS managemoney AUTHORIZATION money_manager;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA managemoney TO money_manager;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA managemoney TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT ALL ON TABLES TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT ALL ON SEQUENCES TO money_manager;

\c managemoney_prod
CREATE SCHEMA IF NOT EXISTS managemoney AUTHORIZATION money_manager;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA managemoney TO money_manager;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA managemoney TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO money_manager;
ALTER DEFAULT PRIVILEGES IN SCHEMA managemoney GRANT USAGE ON SEQUENCES TO money_manager;