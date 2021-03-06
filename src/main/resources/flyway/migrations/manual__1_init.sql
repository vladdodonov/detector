DROP ROLE IF EXISTS detector;
DROP SCHEMA IF EXISTS detector cascade;

CREATE ROLE detector
    PASSWORD 'detector'
    SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

CREATE DATABASE detector
    WITH
    OWNER = detector
    ENCODING = 'UTF8'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1;

GRANT ALL ON DATABASE detector TO detector;

