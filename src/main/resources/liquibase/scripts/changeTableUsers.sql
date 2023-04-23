-- liquibase formatted sql

-- changeset sergey:1
ALTER TABLE users RENAME COLUMN email TO username;
ALTER TABLE users ADD COLUMN enabled BOOLEAN;
ALTER TABLE users ADD PRIMARY KEY (username);
ALTER TABLE users ALTER COLUMN id TYPE BIGSERIAL;

-- changeset sergey:2
CREATE TABLE IF NOT EXIST authorities
(
    id BIGSERIAL,
    username VARCHAR REFERENCES users(username),
    autority VARCHAR
    );

-- changeset sergey:3
CREATE TABLE IF NOT EXIST images
(
    id BIGSERIAL PRIMARY KEY,
    media_type VARCHAR,
    file_path VARCHAR,
    file_size VARCHAR,
    ads_id BIGINT REFERENCES ads(id)
    )

/**
  Это SQL-скрипт в формате Liquibase, который содержит несколько изменений базы данных:

changeset sergey:1:

Изменение имени столбца email на username в таблице users.
Добавление столбца enabled с типом данных BOOLEAN в таблицу users.
Установка первичного ключа на столбец username в таблице users.
Изменение типа столбца id на BIGSERIAL в таблице users.

changeset sergey:2:

Создание таблицы authorities со следующими столбцами:
id - автоинкрементирующийся идентификатор (тип данных BIGSERIAL).
username - внешний ключ, ссылается на столбец username таблицы users (тип данных VARCHAR).
authority - столбец с правами доступа (тип данных VARCHAR).

changeset sergey:3:

Создание таблицы images со следующими столбцами:
id - автоинкрементирующийся идентификатор (тип данных BIGSERIAL), является первичным ключом.
media_type - тип медиа-файла (тип данных VARCHAR).
file_path - путь к файлу (тип данных VARCHAR).
file_size - размер файла (тип данных VARCHAR).
ads_id - внешний ключ, ссылается на столбец id таблицы ads (тип данных BIGINT).
 */