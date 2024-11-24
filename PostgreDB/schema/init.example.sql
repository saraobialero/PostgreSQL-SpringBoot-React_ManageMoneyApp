-- Script per la creazione del database e degli utenti
-- Da eseguire come utente postgres o un utente con privilegi di creazione DB

-- Database Development
CREATE DATABASE managemoney_dev;
COMMENT ON DATABASE managemoney_dev IS 'Development environment for ManageMoney application';

-- Database Test
CREATE DATABASE managemoney_test;
COMMENT ON DATABASE managemoney_test IS 'Test environment for ManageMoney application';

-- Database Production
CREATE DATABASE managemoney_prod;
COMMENT ON DATABASE managemoney_prod IS 'Production environment for ManageMoney application';

-- Creazione utente applicativo con password sicura
CREATE USER managemoney_app WITH PASSWORD 'your_secure_password_here';

- Grant privileges per development
GRANT ALL PRIVILEGES ON DATABASE managemoney_dev TO managemoney_app;
\c managemoney_dev
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO managemoney_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO managemoney_app;

-- Grant privileges per test
GRANT ALL PRIVILEGES ON DATABASE managemoney_test TO managemoney_app;
\c managemoney_test
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO managemoney_app;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO managemoney_app;

-- Grant privileges per production (più restrittivi)
GRANT CONNECT ON DATABASE managemoney_prod TO managemoney_app;
\c managemoney_prod
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO managemoney_app;
GRANT USAGE ON ALL SEQUENCES IN SCHEMA public TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO managemoney_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE ON SEQUENCES TO managemoney_app;