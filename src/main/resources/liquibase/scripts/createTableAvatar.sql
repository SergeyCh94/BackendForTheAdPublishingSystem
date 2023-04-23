-- liquibase formatted sql

-- changeSet sergey:4
CREATE TABLE IF NOT EXISTS avatars
(
    id BIGSERIAL PRIMARY KEY,
    photo OID,
    media_type VARCHAR,
    file_path VARCHAR,
    file_size BIGINT,
    pet_id BIGINT,
    report_id BIGINT
)

/**
  Это SQL-скрипт в формате Liquibase для создания таблицы avatars в базе данных. Таблица содержит следующие столбцы:

id: автоинкрементирующийся идентификатор аватара (тип данных BIGSERIAL), является первичным ключом.
photo: идентификатор фото аватара (тип данных OID), предполагается использование бинарных данных (например, BLOB) для хранения фото.
media_type: тип медиа-файла аватара (тип данных VARCHAR).
file_path: путь к файлу аватара (тип данных VARCHAR).
file_size: размер файла аватара (тип данных BIGINT).
pet_id: идентификатор связанного питомца, если есть (тип данных BIGINT), является внешним ключом.
report_id: идентификатор связанного отчета, если есть (тип данных BIGINT), является внешним ключом.
 */