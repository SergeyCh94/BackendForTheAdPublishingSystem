-- liquibase formatted sql

-- changeset sergey:2
CREATE TABLE IF NOT EXISTS image(
    id              SERIAL PRIMARY KEY,
    media_type      VARCHAR(255),
    file_size       BIGINT,
    data            BYTEA
);